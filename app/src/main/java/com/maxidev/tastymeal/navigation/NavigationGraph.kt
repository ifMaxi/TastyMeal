package com.maxidev.tastymeal.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: NavDestinations = NavDestinations.Home
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                topLevelRoutes.forEach { topLevelRoute ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { navRoute ->
                            navRoute.route?.equals(topLevelRoute.route) == true
                        } == true,
                        onClick = {
                            val popUp = navController.graph.findStartDestination().id

                            navController.navigate(topLevelRoute.route) {
                                popUpTo(popUp) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = topLevelRoute.icon,
                                contentDescription = topLevelRoute.name
                            )
                        },
                        label = {
                            Text(text = topLevelRoute.name)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            navController = navController,
            startDestination = startDestination
        ) {
            // Navigation bar destinations.
            homeScreenDestination(navController = navController)

            searchScreenDestination(navController = navController)

            bookmarkScreenDestination(navController = navController)

            settingsScreenDestination()

            // Other destinations
            myRecipesScreenDestination(navController = navController)

            newRecipeScreenDestination(navController = navController)

            mealDetailScreenDestination(navController = navController)

            mealDetailScreenOfflineDestination(navController = navController)

            recipeDetailScreenDestination(navController = navController)

            editRecipeDetailScreenDestination(navController = navController)

            filtersScreenDestination(navController = navController)
        }
    }
}