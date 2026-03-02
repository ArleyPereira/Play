package br.com.hellodev.design.provider.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import br.com.hellodev.core.enums.theme.ThemeType

class LightDarkModePreviewProvider : PreviewParameterProvider<ThemeType> {
    override val values: Sequence<ThemeType>
        get() = sequenceOf(ThemeType.LIGHT, ThemeType.DARK)
}