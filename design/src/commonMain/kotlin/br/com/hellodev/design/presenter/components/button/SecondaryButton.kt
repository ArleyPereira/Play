package br.com.hellodev.design.presenter.components.button

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.hellodev.core.enums.theme.ThemeType
import br.com.hellodev.design.presenter.components.loading.CircularProgressLoading
import br.com.hellodev.design.presenter.theme.ColorScheme
import br.com.hellodev.design.presenter.theme.HelloTheme
import br.com.hellodev.design.presenter.theme.helloFontFamily
import br.com.hellodev.design.provider.preview.LightDarkModePreviewProvider

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(58.dp),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = ColorScheme.colorScheme.button.secondaryBackground,
            disabledContainerColor = ColorScheme.colorScheme.disabledDefaultColor
        ),
        content = {
            if (isLoading) {
                CircularProgressLoading(
                    modifier = Modifier.size(32.dp),
                    color = ColorScheme.colorScheme.button.secondaryText
                )
            } else {
                Text(
                    text = text,
                    style = TextStyle(
                        lineHeight = 22.4.sp,
                        fontFamily = helloFontFamily(),
                        fontWeight = FontWeight.Bold,
                        color = ColorScheme.colorScheme.button.secondaryText,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.2.sp
                    )
                )
            }
        }
    )
}

@Preview
@Composable
private fun SecondaryButtonPreview(
    @PreviewParameter(LightDarkModePreviewProvider::class) type: ThemeType
) {
    HelloTheme(themeType = type) {
        SecondaryButton(
            text = "Continuar",
            isLoading = false,
            enabled = true,
            onClick = {}
        )
    }
}