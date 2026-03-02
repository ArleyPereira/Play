package br.com.hellodev.main.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import br.com.hellodev.design.platform.extractVideoDurationMillis
import br.com.hellodev.domain.model.video.Video
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable
actual fun rememberVideoDataSource(): VideoDataSource {
    val context = LocalContext.current.applicationContext
    return remember(context) { AndroidPrivateVideoDataSource() }
}

private class AndroidPrivateVideoDataSource : VideoDataSource {

    override suspend fun listVideos(): List<Video> = withContext(Dispatchers.IO) {
        val privateRoot = File("${FileKit.filesDir.path}/play")
        val videosDir = File(privateRoot, "videos")
        val thumbsDir = File(privateRoot, "thumbs")

        if (!privateRoot.exists()) privateRoot.mkdirs()
        if (!videosDir.exists()) videosDir.mkdirs()
        if (!thumbsDir.exists()) thumbsDir.mkdirs()

        videosDir
            .listFiles()
            .orEmpty()
            .asSequence()
            .filter { it.isFile && isVideoFile(it.name) }
            .sortedByDescending { it.lastModified() }
            .map { file ->
                Video(
                    id = file.absolutePath.hashCode().toString(),
                    name = file.name,
                    path = file.absolutePath,
                    thumbnailPath = findThumbnailPath(file = file, thumbsDir = thumbsDir),
                    sizeInBytes = file.length(),
                    durationMillis = extractVideoDurationMillis(file.absolutePath),
                )
            }
            .toList()
    }

    private fun isVideoFile(fileName: String): Boolean {
        val lowerCase = fileName.lowercase()
        return lowerCase.endsWith(".mp4") ||
            lowerCase.endsWith(".mkv") ||
            lowerCase.endsWith(".mov") ||
            lowerCase.endsWith(".webm") ||
            lowerCase.endsWith(".avi")
    }

    private fun findThumbnailPath(file: File, thumbsDir: File): String? {
        if (!thumbsDir.exists() || !thumbsDir.isDirectory) return null

        val videoBaseName = file.name.substringBeforeLast('.', missingDelimiterValue = file.name)
        if (videoBaseName.isBlank()) return null

        return thumbsDir
            .listFiles()
            .orEmpty()
            .firstOrNull { thumb ->
                thumb.isFile &&
                    isThumbnailFile(thumb.name) &&
                    thumb.name.substringBeforeLast('.', missingDelimiterValue = thumb.name)
                        .equals(videoBaseName, ignoreCase = true)
            }
            ?.absolutePath
    }

    private fun isThumbnailFile(fileName: String): Boolean {
        val lowerCase = fileName.lowercase()
        return lowerCase.endsWith(".jpg") ||
            lowerCase.endsWith(".jpeg") ||
            lowerCase.endsWith(".png")
    }
}
