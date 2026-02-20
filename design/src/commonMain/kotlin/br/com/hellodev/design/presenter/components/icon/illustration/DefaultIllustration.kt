package br.com.hellodev.design.presenter.components.icon.illustration

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import br.com.hellodev.design.presenter.theme.illustration.IllustrationType
import br.com.hellodev.design.presenter.theme.illustration.IllustrationType.IC_ARROW_LEFT
import org.jetbrains.compose.resources.painterResource
import play.design.generated.resources.Res
import play.design.generated.resources.ic_arrow_left

@Composable
fun getDrawableIllustration(
    type: IllustrationType
): Painter {
    return painterResource(
        when (type) {
            IC_ARROW_LEFT -> Res.drawable.ic_arrow_left
        }
    )
}
