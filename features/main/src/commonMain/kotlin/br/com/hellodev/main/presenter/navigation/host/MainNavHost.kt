package br.com.hellodev.main.presenter.navigation.host

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import br.com.hellodev.domain.model.video.VideoItem
import br.com.hellodev.main.presenter.features.permission.screen.PermissionScreen
import br.com.hellodev.main.presenter.features.video_list.screen.VideoListScreen
import br.com.hellodev.main.presenter.features.video_player.screen.VideoPlayerScreen
import br.com.hellodev.main.presenter.navigation.routes.MainRoutes

fun NavGraphBuilder.mainNavHost(
    navHostController: NavHostController
) {
    navigation<MainRoutes.Graph>(
        startDestination = MainRoutes.Permission,
    ) {
        composable<MainRoutes.Permission> {
            PermissionScreen(
                onPermissionGranted = {
                    navHostController.navigate(MainRoutes.VideoList) {
                        popUpTo(MainRoutes.Permission) {
                            inclusive = true
                        }
                    }
                },
            )
        }

        composable<MainRoutes.VideoList> {
            VideoListScreen(
                navigateToVideoPlayerScreen = { video ->
                    navHostController.navigate(
                        MainRoutes.VideoPlayer(
                            id = video.id,
                            name = video.name,
                            path = video.path,
                            thumbnailPath = video.thumbnailPath,
                            sizeInBytes = video.sizeInBytes,
                            durationMillis = video.durationMillis,
                        ),
                    )
                },
                onPermissionRequired = {
                    navHostController.navigate(MainRoutes.Permission) {
                        popUpTo(MainRoutes.VideoList) {
                            inclusive = true
                        }
                    }
                },
            )
        }

        composable<MainRoutes.VideoPlayer> { backStackEntry ->
            val route = backStackEntry.toRoute<MainRoutes.VideoPlayer>()

            val video = VideoItem(
                id = route.id,
                name = route.name,
                path = route.path,
                thumbnailPath = route.thumbnailPath,
                sizeInBytes = route.sizeInBytes,
                durationMillis = route.durationMillis,
            )

            VideoPlayerScreen(
                video = video,
                onBack = { navHostController.popBackStack() },
            )
        }
    }
}
