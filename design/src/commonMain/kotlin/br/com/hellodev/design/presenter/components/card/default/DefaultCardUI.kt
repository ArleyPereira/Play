package br.com.hellodev.design.presenter.components.card.default

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import br.com.hellodev.design.presenter.theme.ColorScheme
import br.com.hellodev.design.presenter.theme.ThemeType
import br.com.hellodev.design.provider.preview.LightDarkModePreviewProvider

@Composable
fun DefaultCardUI(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(28.dp),
    content: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = ColorScheme.colorScheme.screen.backgroundSecondary,
            disabledContainerColor = ColorScheme.colorScheme.screen.backgroundSecondary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        //border = borderStrokeDefault(),
        shape = shape,
        enabled = enabled,
        content = { content() }
    )
}

@Preview
@Composable
private fun DefaultCardUIPreview(
    @PreviewParameter(LightDarkModePreviewProvider::class) type: ThemeType
) {
    DefaultCardUI(
        content = {},
        onClick = {}
    )
}