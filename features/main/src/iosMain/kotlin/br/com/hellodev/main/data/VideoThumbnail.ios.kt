package br.com.hellodev.main.data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import br.com.hellodev.domain.model.video.VideoItem

@Composable
actual fun PlatformVideoThumbnail(
    video: VideoItem,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "VIDEO",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(6.dp),
        )
    }
}
