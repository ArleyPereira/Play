package br.com.hellodev.main.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.com.hellodev.domain.model.video.VideoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable
actual fun PlatformVideoThumbnail(
    video: VideoItem,
    modifier: Modifier,
) {
    val context = LocalContext.current
    val bitmapState = produceState<Bitmap?>(
        initialValue = null,
        key1 = video.path,
        key2 = video.thumbnailPath,
    ) {
        value = withContext(Dispatchers.IO) {
            loadThumbnail(
                context = context,
                videoName = video.name,
                videoPath = video.path,
                imagePath = video.thumbnailPath,
            )
        }
    }

    if (bitmapState.value != null) {
        Image(
            bitmap = bitmapState.value!!.asImageBitmap(),
            contentDescription = null,
            modifier = modifier.clip(MaterialTheme.shapes.small),
            contentScale = ContentScale.Crop,
        )
    } else {
        ThumbnailPlaceholder(modifier)
    }
}

@Composable
private fun ThumbnailPlaceholder(modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "VIDEO",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(6.dp),
        )
    }
}

private fun loadThumbnail(
    context: android.content.Context,
    videoName: String,
    videoPath: String,
    imagePath: String?,
): Bitmap? {
    val videoFile = File(videoPath)
    val isContentUri = runCatching {
        val uri = Uri.parse(videoPath)
        uri.scheme == "content"
    }.getOrDefault(false)

    if (!isContentUri && (!videoFile.exists() || !videoFile.isFile)) {
        return null
    }

    val resolvedImagePath = imagePath ?: findCompanionThumbnailPath(
        videoName = videoName,
        videoPath = videoPath,
    )

    resolvedImagePath?.let { customImagePath ->
        loadImageThumbnail(context, customImagePath)?.let { return it }
    }

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD_MR1) {
        return null
    }

    val uri = runCatching { Uri.parse(videoPath) }.getOrNull()
    if (uri != null && uri.scheme == "content") {
        return runCatching {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(context, uri)
                retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            } finally {
                retriever.release()
            }
        }.getOrNull()
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        return runCatching {
            ThumbnailUtils.createVideoThumbnail(videoFile, Size(320, 180), null)
        }.getOrNull()
    }

    @Suppress("DEPRECATION")
    return runCatching {
        ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND)
    }.getOrNull()
}

private fun findCompanionThumbnailPath(
    videoName: String,
    videoPath: String,
): String? {
    val baseName = videoName.substringBeforeLast('.', missingDelimiterValue = videoName).trim()
    if (baseName.isBlank()) return null

    val videoFile = File(videoPath)
    if (videoFile.exists() && videoFile.isFile) {
        val parent = videoFile.parentFile
        if (parent != null && parent.exists() && parent.isDirectory) {
            parent.listFiles()
                .orEmpty()
                .firstOrNull { candidate ->
                    candidate.isFile &&
                        candidate.name.substringBeforeLast('.', missingDelimiterValue = candidate.name)
                            .equals(baseName, ignoreCase = true) &&
                        isThumbnailFile(candidate.name)
                }?.let { return it.absolutePath }
        }
    }
    return null
}

private fun isThumbnailFile(fileName: String): Boolean {
    val lowerCase = fileName.lowercase()
    return lowerCase.endsWith(".jpg") ||
        lowerCase.endsWith(".jpeg") ||
        lowerCase.endsWith(".png")
}

private fun loadImageThumbnail(context: android.content.Context, path: String): Bitmap? {
    val uri = runCatching { Uri.parse(path) }.getOrNull()
    if (uri != null && uri.scheme == "content") {
        return runCatching {
            context.contentResolver.openInputStream(uri)?.use(BitmapFactory::decodeStream)
        }.getOrNull()
    }

    val file = File(path)
    if (!file.exists() || !file.isFile) return null
    return BitmapFactory.decodeFile(file.absolutePath)
}

fun extractVideoDurationMillis(path: String): Long? {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(path)
        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
    } catch (_: Throwable) {
        null
    } finally {
        runCatching { retriever.release() }
    }
}
