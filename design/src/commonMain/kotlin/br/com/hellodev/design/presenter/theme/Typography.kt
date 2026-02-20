package br.com.hellodev.design.presenter.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import play.design.generated.resources.Res
import play.design.generated.resources.urbanist_300
import play.design.generated.resources.urbanist_400
import play.design.generated.resources.urbanist_500
import play.design.generated.resources.urbanist_600
import play.design.generated.resources.urbanist_700

@Composable
fun helloFontFamily(): FontFamily {
    return FontFamily(
        Font(
            resource = Res.font.urbanist_300,
            weight = FontWeight.Light,
            style = FontStyle.Normal
        ),
        Font(
            resource = Res.font.urbanist_400,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(
            resource = Res.font.urbanist_500,
            weight = FontWeight.Medium,
            style = FontStyle.Normal
        ),
        Font(
            resource = Res.font.urbanist_600,
            weight = FontWeight.SemiBold,
            style = FontStyle.Normal
        ),
        Font(
            resource = Res.font.urbanist_700,
            weight = FontWeight.Bold,
            style = FontStyle.Normal
        )
    )
}