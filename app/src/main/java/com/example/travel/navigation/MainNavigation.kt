package com.example.travel.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.travel.ui.screen.DetailsScreen
import com.example.travel.ui.screen.HomeScreen
import com.example.travel.ui.screen.LoginScreen
import com.example.travel.ui.screen.SignupScreen
import com.example.travel.ui.screen.TimeManagerScreen
import com.example.travel.viewModel.AuthViewModel
import com.example.travel.viewModel.ProvinceDetailViewModel

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val user = AuthViewModel().user

    NavHost(
        navController = navController,
        startDestination = if(user == null){
                                "home"
                            } else {
                                "home"
                            }
    ) {
       composable("home"){
            HomeScreen(navController)
       }
        composable("login"){
            LoginScreen(navController = navController)
        }
        composable("signup"){
            SignupScreen(navController = navController)
        }
        composable("details/{id}", arguments = listOf(navArgument("id"){type = NavType.StringType})){
            DetailsScreen(navController)
        }
        composable("manager"){
            TimeManagerScreen(navController)
        }
    }
}