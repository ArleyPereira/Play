package br.com.hellodev.play.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.hellodev.onboarding.presenter.navigation.host.onboardingNavHost
import br.com.hellodev.onboarding.presenter.navigation.routes.OnboardingRoutes

@Composable
fun AppNavHost(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = OnboardingRoutes.Graph
    ) {
        onboardingNavHost(navHostController = navHostController)
    }
}
