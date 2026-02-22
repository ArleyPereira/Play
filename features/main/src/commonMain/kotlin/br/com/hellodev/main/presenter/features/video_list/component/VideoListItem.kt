package br.com.hellodev.main.presenter.features.video_list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.hellodev.core.enums.illustration.IllustrationType
import br.com.hellodev.core.extensions.fileNameWithoutExtension
import br.com.hellodev.core.functions.formatFileSize
import br.com.hellodev.core.functions.formatVideoDuration
import br.com.hellodev.design.presenter.components.card.default.DefaultCardUI
import br.com.hellodev.design.presenter.components.icon.illustration.getDrawableIllustrationResourceByType
import br.com.hellodev.design.presenter.theme.ColorScheme
import br.com.hellodev.domain.model.video.VideoItem
import br.com.hellodev.main.data.PlatformVideoThumbnail
import com.stevdza_san.swipeable.Swipeable
import com.stevdza_san.swipeable.domain.ActionCustomization
import com.stevdza_san.swipeable.domain.HapticFeedbackConfig
import com.stevdza_san.swipeable.domain.SwipeAction
import com.stevdza_san.swipeable.domain.SwipeBackground
import com.stevdza_san.swipeable.domain.SwipeBehavior
import com.stevdza_san.swipeable.domain.SwipeDirection

@Composable
fun VideoListItem(
    modifier: Modifier = Modifier,
    video: VideoItem,
    onClick: () -> Unit,
    onDeleteSwipe: () -> Unit
) {
    Swipeable(
        behavior = SwipeBehavior.REVEAL,
        direction = SwipeDirection.RIGHT,
        rightRevealActions = listOf(
            SwipeAction(
                customization = ActionCustomization(
                    icon = getDrawableIllustrationResourceByType(IllustrationType.IC_DELETE_LINE),
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
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    PlatformVideoThumbnail(
                        video = video,
                        modifier = Modifier
                            .width(120.dp)
                            .height(68.dp)
                            .clip(RoundedCornerShape(8.dp)),
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp)
                            .padding(end = 16.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = video.name.fileNameWithoutExtension(),
                            style = TextStyle(
                                color = ColorScheme.colorScheme.text.primaryColor,
                                fontWeight = FontWeight(500),
                            ),
                            maxLines = 2,
                            minLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = formatFileSize(video.sizeInBytes),
                                style = TextStyle(
                                    color = ColorScheme.colorScheme.text.secondaryColor,
                                ),
                            )

                            Text(
                                text = formatVideoDuration(video.durationMillis),
                                style = TextStyle(
                                    color = ColorScheme.colorScheme.text.secondaryColor,
                                ),
                            )
                        }
                    }
                }
            }
        )
    }
}
