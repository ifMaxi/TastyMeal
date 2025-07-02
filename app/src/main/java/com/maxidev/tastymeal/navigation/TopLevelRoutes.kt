package com.maxidev.tastymeal.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

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