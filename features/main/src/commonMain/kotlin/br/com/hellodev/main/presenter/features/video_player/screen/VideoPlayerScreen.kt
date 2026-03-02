package br.com.hellodev.main.presenter.features.video_player.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import br.com.hellodev.design.presenter.theme.DefaultColor
import br.com.hellodev.domain.model.video.Video
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.ScreenResize
import chaintech.videoplayer.model.VideoPlayerConfig
import chaintech.videoplayer.ui.video.VideoPlayerComposable

@Composable
fun VideoPlayerScreen(
    video: Video,
    onBack: () -> Unit,
) {
    val playerHost = remember(video.path) {
        MediaPlayerHost(
            mediaUrl = video.path.orEmpty(),
            isLooping = false,
            autoPlay = true,
            initialVideoFitMode = ScreenResize.FILL,
            isFullScreen = true
        )
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_STOP) {
        playerHost.pause()
    }

    val playerConfig = remember(onBack) {
        VideoPlayerConfig(
            isZoomEnabled = false,
            seekBarThumbRadius = 12.dp,
            seekBarActiveTrackColor = DefaultColor,
            seekBarThumbColor = DefaultColor,
            seekBarTrackHeight = 6.dp,
            enableBackButton = true,
            backActionCallback = onBack,
            isFullScreenEnabled = false,
        )
    }

    VideoPlayerComposable(
        modifier = Modifier.fillMaxSize(),
        playerHost = playerHost,
        playerConfig = playerConfig,
    )
}
