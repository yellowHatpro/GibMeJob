package com.example.gibmejob.screens.user

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gibmejob.components.UserBottomNavigation
import com.example.gibmejob.model.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(userViewModel: UserViewModel) {
    val navController = rememberNavController()
    val sharedPreferences = LocalContext.current.getSharedPreferences("GibMeJob", Context.MODE_PRIVATE)
    val entity = sharedPreferences.getString("type", "sadge")!!
    val user by userViewModel.user.observeAsState()
    val company by userViewModel.company.observeAsState()

    LaunchedEffect(key1 = true) {
        if(entity == "User") {
            userViewModel.getUser()
        }
        else {
            userViewModel.getCompany()
        }
    }

    Scaffold(
        bottomBar = {
            UserBottomNavigation(navController = navController, type = entity)
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = if (entity == "User") Routes.SearchJobsScreen else Routes.CurrentJobs,
            modifier = Modifier.padding(it)
        ) {
            composable(Routes.SearchJobsScreen) {
                SearchJobsScreen(navHostController = navController)
            }
            composable(Routes.UserApplicationsScreen) {
                UserApplicationsScreen()
            }
            composable(Routes.ProfileScreen) {
                ProfileScreen(user, company)
            }
            composable(Routes.CurrentJobs) {
                CompanyJobScreen()
            }
            composable(
                route = "UserJobScreen/{jobId}",
                arguments = listOf(navArgument("jobId") {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("jobId")?.let { jobId ->
                    UserJobScreen(jobId = jobId)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun UserScreenPreview() {
    UserScreen(UserViewModel())
}