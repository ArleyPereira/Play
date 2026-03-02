package br.com.hellodev.design.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.hellodev.domain.model.video.Video

@Composable
expect fun PlatformVideoThumbnail(
    video: Video,
    modifier: Modifier = Modifier,
)