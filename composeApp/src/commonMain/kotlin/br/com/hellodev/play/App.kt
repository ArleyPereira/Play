package br.com.hellodev.play

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import br.com.hellodev.design.presenter.theme.HelloTheme
import br.com.hellodev.play.navigation.AppNavHost

@Composable
@Preview
fun App() {
    HelloTheme {
        AppNavHost(navHostController = rememberNavController())
    }
}
