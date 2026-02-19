package br.com.hellodev.play

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VideoPlayerScreen(
    video: VideoItem,
    onBack: () -> Unit,
) {
    PlatformBackHandler(onBack = onBack)
    LockVideoPlayerLandscapeEffect(enabled = true)
    PlatformVideoPlayer(
        video = video,
        onBack = onBack,
        modifier = Modifier.fillMaxSize(),
    )
}
