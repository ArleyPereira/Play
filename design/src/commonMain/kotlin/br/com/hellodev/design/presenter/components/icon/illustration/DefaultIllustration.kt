package br.com.hellodev.design.presenter.components.icon.illustration

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import br.com.hellodev.core.enums.illustration.IllustrationType
import br.com.hellodev.core.enums.illustration.IllustrationType.IC_ARROW_LEFT
import br.com.hellodev.core.enums.illustration.IllustrationType.IC_FOLDER_FILL
import br.com.hellodev.core.enums.illustration.IllustrationType.IC_HOME_FILL
import br.com.hellodev.core.enums.illustration.IllustrationType.IC_HOME_LINE
import br.com.hellodev.core.enums.illustration.IllustrationType.IC_SETTINGS_FILL
import br.com.hellodev.core.enums.illustration.IllustrationType.IC_SETTINGS_LINE
import org.jetbrains.compose.resources.painterResource
import play.design.generated.resources.Res
import play.design.generated.resources.ic_arrow_left
import play.design.generated.resources.ic_folder_fill
import play.design.generated.resources.ic_home_fill
import play.design.generated.resources.ic_home_line
import play.design.generated.resources.ic_settings_fill
import play.design.generated.resources.ic_settings_line

@Composable
fun getDrawableIllustration(
    type: IllustrationType
): Painter {
    return painterResource(
        when (type) {
            IC_ARROW_LEFT -> Res.drawable.ic_arrow_left
            IC_HOME_LINE -> Res.drawable.ic_home_line
            IC_HOME_FILL -> Res.drawable.ic_home_fill
            IC_SETTINGS_LINE -> Res.drawable.ic_settings_line
            IC_SETTINGS_FILL -> Res.drawable.ic_settings_fill
            IC_FOLDER_FILL -> Res.drawable.ic_folder_fill
        }
    )
}
