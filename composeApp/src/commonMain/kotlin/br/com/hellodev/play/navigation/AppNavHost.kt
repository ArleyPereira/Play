package br.com.hellodev.play.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.hellodev.main.presenter.navigation.host.mainNavHost
import br.com.hellodev.main.presenter.navigation.routes.MainRoutes

@Composable
fun AppNavHost(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = MainRoutes.Graph
    ) {
        mainNavHost(navHostController = navHostController)
    }
}
