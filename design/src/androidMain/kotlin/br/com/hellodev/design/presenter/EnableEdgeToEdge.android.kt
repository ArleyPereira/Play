package br.com.hellodev.design.presenter

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat

@Composable
actual fun ChangeSchemeColor(isDarkTheme: Boolean) {
    LocalActivity.current?.window?.let { window ->
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.isAppearanceLightStatusBars = !isDarkTheme
        controller.isAppearanceLightNavigationBars = !isDarkTheme
    }
}