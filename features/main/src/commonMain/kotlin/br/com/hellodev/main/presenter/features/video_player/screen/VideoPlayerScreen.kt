package br.com.hellodev.main.presenter.features.video_player.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.hellodev.design.presenter.components.icon.illustration.getDrawableIllustration
import br.com.hellodev.design.presenter.theme.illustration.IllustrationType.IC_ARROW_LEFT
import br.com.hellodev.main.data.VideoItem
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.ScreenResize
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
            isFullScreen = true,
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        VideoPlayerComposable(
            modifier = Modifier.fillMaxSize(),
            playerHost = playerHost,
        )

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(12.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.55f)),
        ) {
            Icon(
                painter = getDrawableIllustration(IC_ARROW_LEFT),
                contentDescription = null,
                tint = Color.White,
            )
        }
    }
}
