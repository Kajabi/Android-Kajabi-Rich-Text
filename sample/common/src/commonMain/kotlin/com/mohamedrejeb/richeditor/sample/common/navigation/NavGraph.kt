package com.mohamedrejeb.richeditor.sample.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohamedrejeb.richeditor.sample.common.home.HomeScreen
import com.kjcommunities.KJDemoScreen as KjCommunitiesKJDemoScreen

private const val HOME_ROUTE = "home"
private const val KJCOMMUNITIES_SLACK_ROUTE = "kjcommunitiesSlack"

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE
    ) {
        composable(HOME_ROUTE) {
            HomeScreen(
                navigateToKjCommunitiesSlack = { navController.navigate(KJCOMMUNITIES_SLACK_ROUTE) }
            )
        }

        composable(KJCOMMUNITIES_SLACK_ROUTE) {
            KjCommunitiesKJDemoScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}