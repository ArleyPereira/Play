package br.com.hellodev.design.presenter.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Border Stroke None
val BorderStrokeNone = BorderStroke(
    width = 0.dp,
    color = Color.Transparent
)

val ShapeBottomSheet = RoundedCornerShape(
    topStart = 32.dp,
    topEnd = 32.dp
)

@Composable
fun Modifier.borderDefault(
    width: Dp = 2.dp,
    color: Color = ColorScheme.colorScheme.border.unselected,
    shape: Shape = CircleShape
): Modifier {
    return this
        .clip(shape)
        .drawBehind {
            drawOutline(
                outline = shape.createOutline(size, layoutDirection, this),
                color = color,
                style = Stroke(width.toPx())
            )
        }
}

@Composable
fun borderStrokeDefault(
    isSelect: Boolean = false,
    width: Dp = 2.dp,
    selectedColor: Color = ColorScheme.colorScheme.border.selected,
    unselectedColor: Color = ColorScheme.colorScheme.border.unselected
): BorderStroke {
    return if (isSelect) {
        BorderStroke(
            width = width,
            color = selectedColor
        )
    } else {
        BorderStroke(
            width = width,
            color = unselectedColor
        )
    }
}