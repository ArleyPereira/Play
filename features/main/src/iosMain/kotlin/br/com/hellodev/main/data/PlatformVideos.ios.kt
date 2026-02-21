package br.com.hellodev.main.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import br.com.hellodev.domain.model.video.VideoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.AVFoundation.AVURLAsset
import platform.CoreMedia.CMTimeGetSeconds
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileSize
import platform.Foundation.NSNumber
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@Composable
actual fun rememberVideoDataSource(): VideoDataSource = remember { IOSVideoDataSource() }

private class IOSVideoDataSource : VideoDataSource {
    override suspend fun listVideos(): List<VideoItem> = withContext(Dispatchers.Default) {
        val fileManager = NSFileManager.defaultManager
        val documentsDir = (NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true)
            .firstOrNull() as? String) ?: return@withContext emptyList()

        val videosDirectory = "$documentsDir/videos"

        if (!fileManager.fileExistsAtPath(videosDirectory)) {
            fileManager.createDirectoryAtPath(videosDirectory, true, null, null)
            return@withContext emptyList()
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
                    videosDirectory = videosDirectory,
                    videoFileName = fileName,
                    fileNames = fileNames,
                )

                VideoItem(
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
        videosDirectory: String,
        videoFileName: String,
        fileNames: List<String>,
    ): String? {
        val videoBaseName = videoFileName.substringBeforeLast('.', missingDelimiterValue = videoFileName)
        if (videoBaseName.isBlank()) return null

        return fileNames.firstOrNull { fileName ->
            val imageBaseName = fileName.substringBeforeLast('.', missingDelimiterValue = fileName)
            imageBaseName.equals(videoBaseName, ignoreCase = true) && isThumbnailFile(fileName)
        }?.let { "$videosDirectory/$it" }
    }

    private fun isThumbnailFile(fileName: String): Boolean {
        val lowerCase = fileName.lowercase()
        return lowerCase.endsWith(".jpg") ||
            lowerCase.endsWith(".jpeg") ||
            lowerCase.endsWith(".png")
    }
}
