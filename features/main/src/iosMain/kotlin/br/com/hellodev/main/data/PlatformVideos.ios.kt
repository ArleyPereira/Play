package br.com.hellodev.main.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import br.com.hellodev.domain.model.video.Video
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.AVFoundation.AVURLAsset
import platform.CoreMedia.CMTimeGetSeconds
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileSize
import platform.Foundation.NSNumber
import platform.Foundation.NSURL

@Composable
actual fun rememberVideoDataSource(): VideoDataSource = remember { IOSVideoDataSource() }

private class IOSVideoDataSource : VideoDataSource {
    override suspend fun listVideos(): List<Video> = withContext(Dispatchers.Default) {
        val fileManager = NSFileManager.defaultManager
        val privateRoot = "${FileKit.filesDir.path}/play"
        val videosDirectory = "$privateRoot/videos"
        val thumbsDirectory = "$privateRoot/thumbs"

        if (!fileManager.fileExistsAtPath(videosDirectory)) {
            fileManager.createDirectoryAtPath(videosDirectory, true, null, null)
        }

        if (!fileManager.fileExistsAtPath(thumbsDirectory)) {
            fileManager.createDirectoryAtPath(thumbsDirectory, true, null, null)
        }

        val fileNames = (fileManager.contentsOfDirectoryAtPath(videosDirectory, error = null) as? List<*>)
            ?.mapNotNull { it as? String }
            .orEmpty()

        fileNames
            .filter(::isVideoFile)
            .map { fileName ->
                val absolutePath = "$videosDirectory/$fileName"
                val attributes = fileManager.attributesOfItemAtPath(absolutePath, error = null) as? Map<*, *>
                val fileSize = (attributes?.get(NSFileSize) as? NSNumber)?.longLongValue ?: 0L
                val fileId = absolutePath.hashCode().toString()
                val thumbnailPath = findThumbnailPath(
                    thumbsDirectory = thumbsDirectory,
                    videoFileName = fileName,
                )

                Video(
                    id = fileId,
                    name = fileName,
                    path = absolutePath,
                    thumbnailPath = thumbnailPath,
                    sizeInBytes = fileSize,
                    durationMillis = extractVideoDurationMillis(absolutePath),
                )
            }
    }

    private fun isVideoFile(fileName: String): Boolean {
        val lowerCase = fileName.lowercase()
        return lowerCase.endsWith(".mp4") ||
            lowerCase.endsWith(".mkv") ||
            lowerCase.endsWith(".mov") ||
            lowerCase.endsWith(".webm") ||
            lowerCase.endsWith(".avi")
    }

    private fun extractVideoDurationMillis(path: String): Long? {
        return runCatching {
            val url = NSURL.fileURLWithPath(path)
            val asset = AVURLAsset.URLAssetWithURL(url, null)
            val totalSeconds = CMTimeGetSeconds(asset.duration)
            if (totalSeconds.isFinite() && totalSeconds > 0.0) {
                (totalSeconds * 1000.0).toLong()
            } else {
                null
            }
        }.getOrNull()
    }

    private fun findThumbnailPath(
        thumbsDirectory: String,
        videoFileName: String,
    ): String? {
        val videoBaseName = videoFileName.substringBeforeLast('.', missingDelimiterValue = videoFileName)
        if (videoBaseName.isBlank()) return null

        val thumbs = (NSFileManager.defaultManager.contentsOfDirectoryAtPath(thumbsDirectory, error = null) as? List<*>)
            ?.mapNotNull { it as? String }
            .orEmpty()

        return thumbs.firstOrNull { fileName ->
            val imageBaseName = fileName.substringBeforeLast('.', missingDelimiterValue = fileName)
            imageBaseName.equals(videoBaseName, ignoreCase = true) && isThumbnailFile(fileName)
        }?.let { "$thumbsDirectory/$it" }
    }

    private fun isThumbnailFile(fileName: String): Boolean {
        val lowerCase = fileName.lowercase()
        return lowerCase.endsWith(".jpg") ||
            lowerCase.endsWith(".jpeg") ||
            lowerCase.endsWith(".png")
    }
}
