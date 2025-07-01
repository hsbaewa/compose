package kr.co.hs.compose

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    contentAlignment: Alignment = Alignment.TopStart,
    enterTransition:
    (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
        slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth.div(4) },
            animationSpec = tween(300)
        ) + fadeIn(tween(300))
    },
    exitTransition:
    (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = { fadeOut(tween(300)) },
    popEnterTransition:
    (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
        slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth.div(4) },
            animationSpec = tween(300)
        ) + fadeIn(tween(300))
    },
    popExitTransition:
    (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = { fadeOut(tween(300)) },
    builder: NavGraphBuilder.() -> Unit
) {
    androidx.navigation.compose.NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        contentAlignment = contentAlignment,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        builder = builder
    )
}