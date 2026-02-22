package br.com.hellodev.main.items.navigation.bar

import br.com.hellodev.core.enums.illustration.IllustrationType
import br.com.hellodev.main.presenter.navigation.routes.BottomBarRoutes
import org.jetbrains.compose.resources.StringResource
import play.features.main.generated.resources.label_home_bottom_app_bar
import play.features.main.generated.resources.Res
import play.features.main.generated.resources.label_settings_bottom_app_bar

sealed class BottomAppBarItems<T>(
    val route: T,
    val label: StringResource,
    val selectedIcon: IllustrationType,
    val unselectedIcon: IllustrationType
) {

    object Home : BottomAppBarItems<BottomBarRoutes.Home>(
        route = BottomBarRoutes.Home,
        label = Res.string.label_home_bottom_app_bar,
        selectedIcon = IllustrationType.IC_HOME_FILL,
        unselectedIcon = IllustrationType.IC_HOME_LINE
    )

    object Settings : BottomAppBarItems<BottomBarRoutes.Settings>(
        route = BottomBarRoutes.Settings,
        label = Res.string.label_settings_bottom_app_bar,
        selectedIcon = IllustrationType.IC_SETTINGS_FILL,
        unselectedIcon = IllustrationType.IC_SETTINGS_LINE
    )

    companion object {
        val items = listOf(Home, Settings)
    }

}