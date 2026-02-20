package br.com.hellodev.main.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PlatformVideoThumbnail(
    video: VideoItem,
    modifier: Modifier = Modifier,
)
