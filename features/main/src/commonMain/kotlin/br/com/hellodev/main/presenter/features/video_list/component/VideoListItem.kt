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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.hellodev.design.presenter.components.card.default.DefaultCardUI
import br.com.hellodev.design.presenter.theme.ColorScheme
import br.com.hellodev.main.data.PlatformVideoThumbnail
import br.com.hellodev.main.data.VideoItem
import kotlin.math.pow
import kotlin.math.round

@Composable
fun VideoListItem(
    modifier: Modifier = Modifier,
    video: VideoItem,
    onClick: () -> Unit,
) {
    DefaultCardUI(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        content = {
            Row(
                modifier = Modifier
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
                        text = video.fileNameWithoutExtension(),
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
        },
    )
}

private fun VideoItem.fileNameWithoutExtension(): String {
    val trimmed = name.trim()
    if (trimmed.isEmpty()) return name
    return trimmed.substringBeforeLast('.', missingDelimiterValue = trimmed)
}

private fun formatFileSize(sizeInBytes: Long): String {
    val mb = sizeInBytes / (1024.0 * 1024.0)
    val gb = mb / 1024.0

    return if (gb >= 1.0) {
        "${gb.fixed(2)} GB"
    } else {
        "${mb.fixed(1)} MB"
    }
}

private fun formatVideoDuration(durationMillis: Long?): String {
    if (durationMillis == null || durationMillis <= 0L) {
        return "00:00 Minutos"
    }

    val totalSeconds = durationMillis / 1000
    return if (totalSeconds >= 3600) {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        "${hours.twoDigits()}:${minutes.twoDigits()} Horas"
    } else {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        "${minutes.twoDigits()}:${seconds.twoDigits()} Minutos"
    }
}

private fun Double.fixed(decimals: Int): String {
    val factor = 10.0.pow(decimals)
    val rounded = round(this * factor) / factor
    val raw = rounded.toString()

    val dotIndex = raw.indexOf('.')
    if (decimals == 0) return raw.substringBefore('.')
    if (dotIndex == -1) return "$raw.${"0".repeat(decimals)}"

    val currentDecimals = raw.length - dotIndex - 1
    if (currentDecimals >= decimals) return raw
    return raw + "0".repeat(decimals - currentDecimals)
}

private fun Long.twoDigits(): String = if (this < 10) "0$this" else this.toString()
