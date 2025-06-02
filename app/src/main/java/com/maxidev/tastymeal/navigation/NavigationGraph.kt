package com.maxidev.tastymeal.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.maxidev.tastymeal.presentation.bookmark.BookmarkScreen
import com.maxidev.tastymeal.presentation.detail.MealDetailScreen
import com.maxidev.tastymeal.presentation.detail.MealDetailScreenOffline
import com.maxidev.tastymeal.presentation.home.HomeScreen
import com.maxidev.tastymeal.presentation.recipe.MyRecipesScreen
import com.maxidev.tastymeal.presentation.recipe.detail.RecipeDetailScreen
import com.maxidev.tastymeal.presentation.recipe.new_recipe.NewRecipeScreen
import com.maxidev.tastymeal.presentation.search.SearchScreen
import com.maxidev.tastymeal.presentation.settings.SettingsScreen
import kotlinx.serialization.Serializable

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
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = startDestination
        ) {
            composable<NavDestinations.Home> {
                HomeScreen(
                    navigateToDetail = { mealId ->
                        navController.navigate(NavDestinations.MealDetail(mealId))
                    }
                )
            }
            composable<NavDestinations.Search> {
                SearchScreen(
                    onClick = { mealId ->
                        navController.navigate(NavDestinations.MealDetail(mealId))
                    }
                )
            }
            composable<NavDestinations.Bookmarks> {
                BookmarkScreen(
                    navigateToDetailOffline = { mealId ->
                        navController.navigate(NavDestinations.MealDetailOffline(mealId))
                    }
                )
            }
            composable<NavDestinations.Settings> {
                SettingsScreen()
            }
            composable<NavDestinations.MyRecipes> {
                MyRecipesScreen(
                    navigateToCreate = { navController.navigate(NavDestinations.NewRecipe) },
                    navigateToDetail = { id ->
                        navController.navigate(NavDestinations.RecipeDetail(id))
                    }
                )
            }
            composable<NavDestinations.NewRecipe> {
                NewRecipeScreen(
                    popBackStack = { navController.popBackStack() }
                )
            }

            composable<NavDestinations.MealDetail> {
                MealDetailScreen(
                    navigateBack = { navController.popBackStack() }
                )
            }
            composable<NavDestinations.MealDetailOffline> {
                MealDetailScreenOffline(
                    navigateBack = { navController.popBackStack() }
                )
            }
            composable<NavDestinations.RecipeDetail> {
                RecipeDetailScreen(
                    navigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

sealed interface NavDestinations {
    @Serializable data object Home : NavDestinations
    @Serializable data object Search : NavDestinations
    @Serializable data object Bookmarks: NavDestinations
    @Serializable data object Settings : NavDestinations
    @Serializable data object MyRecipes: NavDestinations
    @Serializable data object NewRecipe: NavDestinations
    @Serializable data class MealDetail(val mealId: String) : NavDestinations
    @Serializable data class MealDetailOffline(val mealId: String): NavDestinations
    @Serializable data class RecipeDetail(val recipeId: Long): NavDestinations
}

data class TopLevelRoute<T: Any>(
    val route: T,
    val icon: ImageVector,
    val name: String
)

val topLevelRoutes = listOf(
    TopLevelRoute(
        route = NavDestinations.Home,
        icon = Icons.Rounded.Home,
        name = "Home"
    ),
    TopLevelRoute(
        route = NavDestinations.Search,
        icon = Icons.Rounded.Search,
        name = "Search"
    ),
    TopLevelRoute(
        route = NavDestinations.MyRecipes,
        icon = Icons.Rounded.Create,
        name = "My recipes"
    ),
    TopLevelRoute(
        route = NavDestinations.Bookmarks,
        icon = Icons.Rounded.Bookmarks,
        name = "Bookmarks"
    ),
    TopLevelRoute(
        route = NavDestinations.Settings,
        icon = Icons.Rounded.Settings,
        name = "Settings"
    )
)