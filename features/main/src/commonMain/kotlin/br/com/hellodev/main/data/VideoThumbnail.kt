package br.com.hellodev.main.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.hellodev.domain.model.video.VideoItem

@Composable
expect fun PlatformVideoThumbnail(
    video: VideoItem,
    modifier: Modifier = Modifier,
)
