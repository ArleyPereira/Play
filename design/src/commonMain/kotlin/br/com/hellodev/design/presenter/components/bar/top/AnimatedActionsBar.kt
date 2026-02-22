package br.com.hellodev.design.presenter.components.bar.top

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import br.com.hellodev.core.enums.illustration.IllustrationType
import br.com.hellodev.design.presenter.components.icon.illustration.getDrawableIllustration
import br.com.hellodev.design.presenter.theme.ColorScheme
import br.com.hellodev.design.presenter.theme.HelloTheme
import br.com.hellodev.design.presenter.theme.ThemeType
import br.com.hellodev.design.presenter.theme.helloFontFamily
import br.com.hellodev.design.provider.preview.LightDarkModePreviewProvider

@Composable
fun AnimatedActionsBar(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = !isVisible,
        enter = slideInHorizontally(
            initialOffsetX = { it },
        ) + fadeIn(),
        exit = slideOutHorizontally(
            targetOffsetX = { it },
        ) + fadeOut()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AnimatedActionsBarPreview(
    @PreviewParameter(LightDarkModePreviewProvider::class) type: ThemeType
) {
    HelloTheme(themeType = type) {
        TopAppBarUI(
            title = "Voltar",
            actions = {
                AnimatedActionsBar(
                    isVisible = false,
                    content = {
                        Text(
                            text = "editar",
                            style = TextStyle(
                                fontFamily = helloFontFamily(),
                                color = ColorScheme.colorScheme.text.primaryColor,
                                letterSpacing = 0.2.sp
                            )
                        )

                        IconButton(
                            onClick = {},
                            content = {
                                Icon(
                                    painter = getDrawableIllustration(
                                        type = IllustrationType.IC_SETTINGS_LINE
                                    ),
                                    contentDescription = null,
                                    tint = ColorScheme.colorScheme.icon.color
                                )
                            }
                        )
                    }
                )
            }
        )
    }
}