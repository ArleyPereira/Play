package br.com.hellodev.play

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PlatformVideoPlayer(
    video: VideoItem,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
)

@Composable
expect fun LockVideoPlayerLandscapeEffect(enabled: Boolean)
