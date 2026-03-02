package br.com.hellodev.design.presenter.components.card.video

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.hellodev.design.presenter.components.card.default.DefaultCardUI
import br.com.hellodev.design.presenter.components.item.video.VideoItem
import br.com.hellodev.design.presenter.theme.ColorScheme
import br.com.hellodev.domain.model.video.Video
import com.stevdza_san.swipeable.Swipeable
import com.stevdza_san.swipeable.domain.ActionCustomization
import com.stevdza_san.swipeable.domain.HapticFeedbackConfig
import com.stevdza_san.swipeable.domain.SwipeAction
import com.stevdza_san.swipeable.domain.SwipeBackground
import com.stevdza_san.swipeable.domain.SwipeBehavior
import com.stevdza_san.swipeable.domain.SwipeDirection
import play.design.generated.resources.Res
import play.design.generated.resources.ic_delete_line

@Composable
fun VideoCard(
    modifier: Modifier = Modifier,
    video: Video,
    onClick: () -> Unit,
    onDeleteSwipe: () -> Unit
) {
    Swipeable(
        behavior = SwipeBehavior.REVEAL,
        direction = SwipeDirection.RIGHT,
        rightRevealActions = listOf(
            SwipeAction(
                customization = ActionCustomization(
                    icon = Res.drawable.ic_delete_line,
                    iconColor = Color.White,
                    containerColor = Color.Black.copy(alpha = 0.1f)
                ),
                onAction = onDeleteSwipe
            )
        ),
        shape = RoundedCornerShape(8.dp),
        rightBackground = SwipeBackground.solid(ColorScheme.colorScheme.alertColor),
        hapticFeedbackConfig = HapticFeedbackConfig.Default
    ) {
        DefaultCardUI(
            shape = RoundedCornerShape(8.dp),
            onClick = onClick,
            content = {
                VideoItem(
                    modifier = modifier,
                    video = video,
                    isBordered = false
                )
            }
        )
    }
}
