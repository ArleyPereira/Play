package br.com.hellodev.onboarding.presenter.navigation.host

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.hellodev.main.presenter.navigation.host.mainNavHost
import br.com.hellodev.main.presenter.navigation.routes.MainRoutes
import br.com.hellodev.onboarding.presenter.features.permission.screen.PermissionScreen
import br.com.hellodev.onboarding.presenter.navigation.routes.OnboardingRoutes

fun NavGraphBuilder.onboardingNavHost(
    navHostController: NavHostController
) {
    navigation<OnboardingRoutes.Graph>(
        startDestination = OnboardingRoutes.Permissions
    ) {
        composable<OnboardingRoutes.Splash> {
//            SplashScreen(
//                navigateToHomeScreen = {
//                    navHostController.navigate(MainRoutes.Graph) {
//                        popUpTo(OnboardingRoutes.Graph) {
//                            inclusive = true
//                        }
//                    }
//                },
//                navigateToLoginScreen = {
//                    navHostController.navigate(AuthenticationRoutes.Graph) {
//                        popUpTo(OnboardingRoutes.Graph) {
//                            inclusive = true
//                        }
//                    }
//                }
//            )
        }

        composable<OnboardingRoutes.Permissions> {
            PermissionScreen(
                navigateToHomeScreen = {
                    navHostController.navigate(MainRoutes.Graph) {
                        popUpTo(OnboardingRoutes.Graph) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        mainNavHost()
    }
}