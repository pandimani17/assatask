package com.assaabloy.task.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.assaabloy.task.presentation.lockconfig.edit.ui.LockConfigEditRoute
import com.assaabloy.task.presentation.lockconfig.list.ui.LockConfigListRoute


sealed class LockConfigScreen(val route: String) {
    object List : LockConfigScreen("lockconfig_list")
    object Edit : LockConfigScreen("lockconfig_edit/{paramKey}") {
        fun createRoute(paramKey: String) = "lockconfig_edit/$paramKey"
    }
}

@Composable
fun LockConfigNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = LockConfigScreen.List.route
    ) {
        composable(LockConfigScreen.List.route) {
            LockConfigListRoute { paramKey ->
                navController.navigate(LockConfigScreen.Edit.createRoute(paramKey))
            }
        }

        composable(
            route = LockConfigScreen.Edit.route, arguments = listOf(
            navArgument("paramKey") { type = NavType.StringType })) { backStackEntry ->
            val paramKey = backStackEntry.arguments!!.getString("paramKey")!!

            LockConfigEditRoute(
                paramKey = paramKey,
                onBack = { navController.popBackStack() })
        }
    }
}

@Composable
fun LockConfigNavigation() {
    val navController = rememberNavController()
    LockConfigNavGraph(navController)
}