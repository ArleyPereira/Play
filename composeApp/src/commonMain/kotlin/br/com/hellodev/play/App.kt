package br.com.hellodev.play

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.com.hellodev.design.presenter.theme.HelloTheme
import br.com.hellodev.play.list.screen.VideoListScreen

@Composable
@Preview
fun App() {
    HelloTheme {
        VideoListScreen()
    }
}
