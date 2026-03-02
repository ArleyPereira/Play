package br.com.hellodev.main.presenter.features.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.hellodev.design.presenter.components.bar.bottom.BottomBarItemUI
import br.com.hellodev.design.presenter.components.icon.illustration.getDrawableIllustration
import br.com.hellodev.design.presenter.theme.ColorScheme
import br.com.hellodev.design.presenter.theme.HelloTheme
import br.com.hellodev.core.enums.theme.ThemeType
import br.com.hellodev.design.provider.preview.LightDarkModePreviewProvider
import br.com.hellodev.main.items.navigation.bar.BottomAppBarItems
import br.com.hellodev.main.presenter.navigation.host.BottomAppBarNavHost
import br.com.hellodev.main.presenter.navigation.routes.BottomBarRoutes
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen() {
    MainContent()
}

@Composable
fun MainContent() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomAppBar(currentDestination),
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                content = {
                    BottomAppBar(
                        actions = {
                            BottomAppBarItems.items.forEach { item ->
                                val isSelect = currentDestination?.hierarchy?.any {
                                    it.route == item.route::class.qualifiedName
                                } == true

                                BottomBarItemUI(
                                    selectedIcon = getDrawableIllustration(item.selectedIcon),
                                    unselectedIcon = getDrawableIllustration(item.unselectedIcon),
                                    label = stringResource(item.label),
                                    isSelect = isSelect,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                        containerColor = ColorScheme.colorScheme.screen.backgroundSecondary
                    )
                }
            )
        },
        content = { paddingValues ->
            BottomAppBarNavHost(
                modifier = Modifier
                    .background(ColorScheme.colorScheme.screen.backgroundPrimary),
                navHostController = navController,
                paddingValues = PaddingValues(
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Rtl),
                    bottom = paddingValues.calculateBottomPadding() + 16.dp
                )
            )
        }
    )
}

fun isBottomAppBar(currentDestination: NavDestination?): Boolean {
    return when (currentDestination?.route) {
        BottomBarRoutes.Home::class.qualifiedName,
        BottomBarRoutes.Settings::class.qualifiedName -> true

        else -> false
    }
}

@Preview
@Composable
private fun MainPreview(
    @PreviewParameter(LightDarkModePreviewProvider::class) type: ThemeType
) {
    HelloTheme(themeType = type) {
        MainContent()
    }
}