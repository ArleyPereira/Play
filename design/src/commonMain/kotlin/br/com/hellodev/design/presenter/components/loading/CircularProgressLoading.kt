package br.com.hellodev.design.presenter.components.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.hellodev.design.presenter.theme.ColorScheme
import br.com.hellodev.design.presenter.theme.HelloTheme
import br.com.hellodev.design.presenter.theme.ThemeType
import br.com.hellodev.design.provider.preview.LightDarkModePreviewProvider

@Composable
fun CircularProgressLoading(
    modifier: Modifier = Modifier,
    color: Color = ColorScheme.colorScheme.defaultColor,
    strokeWidth: Dp = 2.dp
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = color,
        strokeWidth = strokeWidth
    )
}

@Preview
@Composable
private fun CircularProgressLoadingPreview(
    @PreviewParameter(LightDarkModePreviewProvider::class) type: ThemeType
) {
    HelloTheme(themeType = type) {
        Column(
            modifier = Modifier
                .background(ColorScheme.colorScheme.screen.backgroundPrimary),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressLoading(
                modifier = Modifier
                    .padding(32.dp)
            )
        }
    }
}