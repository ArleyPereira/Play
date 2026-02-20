package br.com.hellodev.main.presenter.features.video_player.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.hellodev.main.data.LockVideoPlayerLandscapeEffect
import br.com.hellodev.main.data.PlatformVideoPlayer
import br.com.hellodev.main.data.VideoItem

@Composable
fun VideoPlayerScreen(
    video: VideoItem,
    onBack: () -> Unit,
) {
    LockVideoPlayerLandscapeEffect(enabled = true)
    PlatformVideoPlayer(
        video = video,
        onBack = onBack,
        modifier = Modifier.fillMaxSize(),
    )
}
