package br.com.hellodev.main.presenter.navigation.host

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import br.com.hellodev.core.extensions.long.orZero
import br.com.hellodev.domain.model.video.Video
import br.com.hellodev.main.presenter.features.settings.screen.SettingsScreen
import br.com.hellodev.main.presenter.features.video_list.screen.VideoListScreen
import br.com.hellodev.main.presenter.features.video_player.screen.VideoPlayerScreen
import br.com.hellodev.main.presenter.navigation.routes.BottomBarRoutes

@Composable
fun BottomAppBarNavHost(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    paddingValues: PaddingValues = PaddingValues()
) {
    NavHost(
        navController = navHostController,
        startDestination = BottomBarRoutes.Home,
        modifier = modifier
    ) {
        composable<BottomBarRoutes.Home> {
            VideoListScreen(
                paddingValues = paddingValues,
                navigateToVideoPlayerScreen = { video ->
                    navHostController.navigate(
                        BottomBarRoutes.VideoPlayer(
                            id = video.id.orEmpty(),
                            name = video.name.orEmpty(),
                            path = video.path.orEmpty(),
                            thumbnailPath = video.thumbnailPath,
                            sizeInBytes = video.sizeInBytes.orZero(),
                            durationMillis = video.durationMillis,
                        ),
                    )
                },
                onPermissionRequired = {
//                    navHostController.navigate(MainRoutes.Permission) {
//                        popUpTo(MainRoutes.VideoList) {
//                            inclusive = true
//                        }
//                    }
                },
            )
        }

        composable<BottomBarRoutes.Settings> {
            SettingsScreen(paddingValues = paddingValues)
        }

        composable<BottomBarRoutes.VideoPlayer> { backStackEntry ->
            val route = backStackEntry.toRoute<BottomBarRoutes.VideoPlayer>()

            val video = Video(
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