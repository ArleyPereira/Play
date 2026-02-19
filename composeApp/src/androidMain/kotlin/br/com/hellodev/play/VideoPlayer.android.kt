package br.com.hellodev.play

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.os.Build
import android.net.Uri
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import java.io.File
import kotlin.math.roundToInt

@Composable
actual fun PlatformVideoPlayer(
    video: VideoItem,
    onBack: () -> Unit,
    modifier: Modifier,
) {
    SystemBarsVisibilityEffect(hide = true)

    val context = LocalContext.current
    var videoViewRef by remember { mutableStateOf<VideoView?>(null) }
    var isPrepared by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var positionMs by remember { mutableStateOf(0L) }
    var durationMs by remember { mutableStateOf(0L) }
    var controlsVisible by remember { mutableStateOf(false) }

    val audioManager = remember(context) {
        context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    }
    val maxVolume = remember(audioManager) {
        (audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 1).coerceAtLeast(1)
    }
    var volume by remember(audioManager) {
        mutableStateOf((audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: maxVolume).toFloat())
    }

    LaunchedEffect(videoViewRef, isPrepared) {
        while (true) {
            val view = videoViewRef
            if (view != null && isPrepared) {
                positionMs = view.currentPosition.toLong()
                durationMs = view.duration.toLong().coerceAtLeast(0L)
                isPlaying = view.isPlaying
            }
            delay(250)
        }
    }

    LaunchedEffect(controlsVisible, isPlaying) {
        if (controlsVisible && isPlaying) {
            delay(3_000)
            controlsVisible = false
        }
    }

    Box(
        modifier = modifier
            .background(Color.Black)
            .clickable { controlsVisible = !controlsVisible },
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { viewContext ->
                VideoView(viewContext).apply {
                    keepScreenOn = true

                    setOnPreparedListener { mediaPlayer ->
                        mediaPlayer.isLooping = false
                        isPrepared = true
                        durationMs = duration.toLong().coerceAtLeast(0L)
                        start()
                        isPlaying = true
                    }

                    setOnCompletionListener {
                        isPlaying = false
                        positionMs = durationMs
                        controlsVisible = true
                    }

                    val uri = buildVideoUri(video.path)
                    tag = uri.toString()
                    setVideoURI(uri)
                    videoViewRef = this
                }
            },
            update = { videoView ->
                val uri = buildVideoUri(video.path)
                val uriKey = uri.toString()
                if (videoView.tag != uriKey) {
                    isPrepared = false
                    positionMs = 0L
                    durationMs = 0L
                    videoView.tag = uriKey
                    videoView.setVideoURI(uri)
                }
                videoViewRef = videoView
            },
        )

        AnimatedVisibility(
            visible = controlsVisible,
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.28f)),
            ) {
                PlayerTopBar(
                    title = video.name,
                    onBack = onBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .fillMaxWidth(),
                )

                PlayerCenterControls(
                    isPlaying = isPlaying,
                    onRewind = {
                        val view = videoViewRef ?: return@PlayerCenterControls
                        val target = (view.currentPosition - 10_000).coerceAtLeast(0)
                        view.seekTo(target)
                        positionMs = target.toLong()
                    },
                    onPlayPause = {
                        val view = videoViewRef ?: return@PlayerCenterControls
                        if (view.isPlaying) {
                            view.pause()
                            isPlaying = false
                        } else {
                            view.start()
                            isPlaying = true
                        }
                    },
                    onForward = {
                        val view = videoViewRef ?: return@PlayerCenterControls
                        val end = view.duration.takeIf { it > 0 } ?: Int.MAX_VALUE
                        val target = (view.currentPosition + 10_000).coerceAtMost(end)
                        view.seekTo(target)
                        positionMs = target.toLong()
                    },
                    modifier = Modifier.align(Alignment.Center),
                )

                PlayerBottomBar(
                    currentMs = positionMs,
                    durationMs = durationMs,
                    volume = volume,
                    maxVolume = maxVolume,
                    onSeek = { target ->
                        val targetMs = target.roundToInt().coerceAtLeast(0).toLong()
                        positionMs = targetMs
                        videoViewRef?.seekTo(targetMs.toInt())
                    },
                    onVolumeChange = { value ->
                        volume = value
                        audioManager?.setStreamVolume(
                            AudioManager.STREAM_MUSIC,
                            value.roundToInt().coerceIn(0, maxVolume),
                            0,
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun SystemBarsVisibilityEffect(hide: Boolean) {
    val activity = LocalContext.current.findActivity()

    DisposableEffect(activity, hide) {
        val window = activity?.window
        val decorView = window?.decorView

        if (!hide || window == null || decorView == null) {
            onDispose {}
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val insetsController = window.insetsController
                insetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                insetsController?.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                @Suppress("DEPRECATION")
                run {
                    decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                        )
                }
            }

            onDispose {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.insetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                } else {
                    @Suppress("DEPRECATION")
                    run { decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE }
                }
            }
        }
    }
}

@Composable
private fun PlayerTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleIconButton(icon = "←", contentDescription = "Voltar", onClick = onBack)
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}

@Composable
private fun PlayerCenterControls(
    isPlaying: Boolean,
    onRewind: () -> Unit,
    onPlayPause: () -> Unit,
    onForward: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleIconButton(icon = "↺10", contentDescription = "Voltar 10 segundos", onClick = onRewind)
        CircleIconButton(
            icon = if (isPlaying) "❚❚" else "▶",
            contentDescription = if (isPlaying) "Pausar" else "Play",
            onClick = onPlayPause,
            isPrimary = true,
        )
        CircleIconButton(icon = "10↻", contentDescription = "Avancar 10 segundos", onClick = onForward)
    }
}

@Composable
private fun PlayerBottomBar(
    currentMs: Long,
    durationMs: Long,
    volume: Float,
    maxVolume: Int,
    onSeek: (Float) -> Unit,
    onVolumeChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.45f))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Slider(
            value = currentMs.toFloat(),
            onValueChange = onSeek,
            valueRange = 0f..durationMs.coerceAtLeast(1L).toFloat(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${formatClock(currentMs)} / ${formatClock(durationMs)}",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(text = "🔊", color = Color.White)
            Slider(
                value = volume,
                onValueChange = onVolumeChange,
                valueRange = 0f..maxVolume.toFloat(),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
private fun CircleIconButton(
    icon: String,
    contentDescription: String,
    onClick: () -> Unit,
    isPrimary: Boolean = false,
) {
    val size = if (isPrimary) 72.dp else 58.dp
    val textStyle = if (isPrimary) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleMedium

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = if (isPrimary) 0.70f else 0.55f)),
    ) {
        Text(text = icon, style = textStyle, color = Color.White)
    }
}

@Composable
actual fun LockVideoPlayerLandscapeEffect(enabled: Boolean) {
    val activity = LocalContext.current.findActivity()

    DisposableEffect(activity, enabled) {
        if (activity == null || !enabled) {
            onDispose {}
        } else {
            val previousOrientation = activity.requestedOrientation
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

            onDispose {
                activity.requestedOrientation = previousOrientation
            }
        }
    }
}

private fun formatClock(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        "${hours.twoDigits()}:${minutes.twoDigits()}:${seconds.twoDigits()}"
    } else {
        "${minutes.twoDigits()}:${seconds.twoDigits()}"
    }
}

private fun Long.twoDigits(): String = if (this < 10) "0$this" else toString()

private fun buildVideoUri(path: String): Uri {
    return if (path.startsWith("content://")) {
        Uri.parse(path)
    } else {
        Uri.fromFile(File(path))
    }
}

private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}
