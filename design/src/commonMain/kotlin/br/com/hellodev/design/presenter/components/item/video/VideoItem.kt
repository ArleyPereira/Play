package br.com.hellodev.design.presenter.components.item.video

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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.hellodev.core.extensions.fileNameWithoutExtension
import br.com.hellodev.core.extensions.long.orZero
import br.com.hellodev.core.functions.formatFileSize
import br.com.hellodev.core.functions.formatVideoDuration
import br.com.hellodev.design.platform.PlatformVideoThumbnail
import br.com.hellodev.design.presenter.theme.ColorScheme
import br.com.hellodev.design.presenter.theme.borderDefault
import br.com.hellodev.domain.model.video.Video

@Composable
fun VideoItem(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    isBordered: Boolean = true,
    video: Video,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .borderDefault(
                shape = shape,
                width = if (isBordered) 2.dp else 0.dp
            )
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
                text = video.name?.fileNameWithoutExtension().orEmpty(),
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
                    text = formatFileSize(video.sizeInBytes.orZero()),
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