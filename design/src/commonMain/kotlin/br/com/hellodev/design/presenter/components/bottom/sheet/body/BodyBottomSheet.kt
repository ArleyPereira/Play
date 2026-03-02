package br.com.hellodev.design.presenter.components.bottom.sheet.body

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.hellodev.core.enums.theme.ThemeType
import br.com.hellodev.design.presenter.components.spacer.VerticalSpacer
import br.com.hellodev.design.presenter.theme.ColorScheme
import br.com.hellodev.design.presenter.theme.HelloTheme
import br.com.hellodev.design.presenter.theme.helloFontFamily
import br.com.hellodev.design.provider.preview.LightDarkModePreviewProvider

@Composable
fun BodyBottomSheet(
    modifier: Modifier = Modifier,
    message: String,
    color: Color = ColorScheme.colorScheme.text.primaryColor
) {
    Text(
        text = message,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        style = TextStyle(
            lineHeight = 22.4.sp,
            fontFamily = helloFontFamily(),
            color = color,
            textAlign = TextAlign.Center,
            letterSpacing = 0.2.sp
        )
    )

    VerticalSpacer(size = 24)
}

@Preview
@Composable
private fun BodyBottomSheetPreview(
    @PreviewParameter(LightDarkModePreviewProvider::class) type: ThemeType
) {
    HelloTheme(themeType = type) {
        BodyBottomSheet(
            message = "A senha informada está incorreta"
        )
    }
}
