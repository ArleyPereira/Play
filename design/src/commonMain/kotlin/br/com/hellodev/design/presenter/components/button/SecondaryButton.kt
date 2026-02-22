package br.com.hellodev.design.presenter.components.button

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.hellodev.design.presenter.components.loading.CircularProgressLoading
import br.com.hellodev.design.presenter.theme.ColorScheme
import br.com.hellodev.design.presenter.theme.HelloTheme
import br.com.hellodev.design.presenter.theme.ThemeType
import br.com.hellodev.design.presenter.theme.borderStrokeDefault
import br.com.hellodev.design.presenter.theme.helloFontFamily
import br.com.hellodev.design.provider.preview.LightDarkModePreviewProvider

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp),
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = ColorScheme.colorScheme.disabledDefaultColor
        ),
        border = borderStrokeDefault(isSelect = true),
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