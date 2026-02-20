package br.com.hellodev.main.presenter.features.video_player.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.hellodev.design.presenter.theme.DefaultColor
import br.com.hellodev.main.data.VideoItem
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.ScreenResize
import chaintech.videoplayer.model.VideoPlayerConfig
import chaintech.videoplayer.ui.video.VideoPlayerComposable

@Composable
fun VideoPlayerScreen(
    video: VideoItem,
    onBack: () -> Unit,
) {
    val playerHost = remember(video.path) {
        MediaPlayerHost(
            mediaUrl = video.path,
            isLooping = false,
            autoPlay = true,
            initialVideoFitMode = ScreenResize.FILL,
            isFullScreen = true
        )
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
        )
    }

    VideoPlayerComposable(
        modifier = Modifier.fillMaxSize(),
        playerHost = playerHost,
        playerConfig = playerConfig,
    )
}
