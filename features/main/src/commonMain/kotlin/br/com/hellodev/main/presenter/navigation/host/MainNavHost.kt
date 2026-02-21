package br.com.hellodev.main.presenter.navigation.host

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.hellodev.main.presenter.features.main.MainScreen
import br.com.hellodev.main.presenter.navigation.routes.MainRoutes

fun NavGraphBuilder.mainNavHost() {
    navigation<MainRoutes.Graph>(
        startDestination = MainRoutes.Main,
    ) {
        composable<MainRoutes.Main> {
            MainScreen()
        }
    }
}
