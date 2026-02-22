package br.com.hellodev.main.data

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import br.com.hellodev.domain.model.video.VideoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable
actual fun rememberVideoDataSource(): VideoDataSource {
    val context = LocalContext.current.applicationContext
    return remember(context) { AndroidMediaStoreVideoDataSource(context) }
}

private class AndroidMediaStoreVideoDataSource(
    private val context: Context,
) : VideoDataSource {

    @Suppress("DEPRECATION")
    override suspend fun listVideos(): List<VideoItem> = withContext(Dispatchers.IO) {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.RELATIVE_PATH,
            MediaStore.Video.Media.DATA,
        )

        val videos = mutableListOf<VideoItem>()
        val sortOrder = "${MediaStore.Video.Media.DATE_MODIFIED} DESC"

        try {
            context.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder,
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                val durationColumn = cursor.getColumnIndex(MediaStore.Video.Media.DURATION)
                val relativePathColumn = cursor.getColumnIndex(MediaStore.Video.Media.RELATIVE_PATH)
                val absolutePathColumn = cursor.getColumnIndex(MediaStore.Video.Media.DATA)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn) ?: "video_$id"
                    val size = cursor.getLong(sizeColumn)
                    val duration = if (durationColumn >= 0 && !cursor.isNull(durationColumn)) {
                        cursor.getLong(durationColumn)
                    } else {
                        null
                    }
                    val relativePath = if (relativePathColumn >= 0) cursor.getString(relativePathColumn) else null
                    val absolutePath = if (absolutePathColumn >= 0) cursor.getString(absolutePathColumn) else null

                    if (!belongsToVideosFolder(relativePath = relativePath, absolutePath = absolutePath)) {
                        continue
                    }

                    val contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                    val thumbnailPath = findThumbnailPathForVideo(
                        absolutePath = absolutePath,
                        relativePath = relativePath,
                        videoName = name,
                    )
                    videos += VideoItem(
                        id = id.toString(),
                        name = name,
                        path = absolutePath ?: contentUri.toString(),
                        thumbnailPath = thumbnailPath,
                        sizeInBytes = size,
                        durationMillis = duration,
                    )
                }
            }
        } catch (_: SecurityException) {
            throw VideoPermissionException()
        }

        val knownFolderVideos = listVideosFromKnownFolders()
        val mergedVideos = videos + knownFolderVideos

        deduplicate(mergedVideos)
    }

    private fun belongsToVideosFolder(relativePath: String?, absolutePath: String?): Boolean {
        if (pathContainsVideosFolder(relativePath)) return true
        return pathContainsVideosFolder(absolutePath)
    }

    private fun pathContainsVideosFolder(path: String?): Boolean {
        val normalized = path
            .orEmpty()
            .lowercase()
            .replace('\\', '/')
            .trim('/')

        if (normalized.isEmpty()) return false
        return normalized.split('/').any { it == "play" }
    }

    private fun listVideosFromKnownFolders(): List<VideoItem> {
        val candidateDirs = listOf(
            File("/storage/emulated/0/Movies/play"),
            File("/storage/emulated/0/play"),
            File("/sdcard/Movies/play"),
            File("/sdcard/play"),
        )

        return candidateDirs
            .asSequence()
            .distinctBy { it.absolutePath }
            .filter { it.exists() && it.isDirectory }
            .flatMap { directory ->
                directory.listFiles()
                    .orEmpty()
                    .asSequence()
                    .filter { it.isFile && isVideoFile(it.name) }
            }
            .sortedByDescending { it.lastModified() }
            .map { file ->
                VideoItem(
                    id = file.absolutePath.hashCode().toString(),
                    name = file.name,
                    path = file.absolutePath,
                    thumbnailPath = findThumbnailPathForVideoPath(file.absolutePath),
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

    private fun deduplicate(items: List<VideoItem>): List<VideoItem> {
        val byPathOrMeta = items
            .groupBy { item ->
                val normalizedPath = item.path
                    .lowercase()
                    .replace('\\', '/')
                    .trim()

                when {
                    normalizedPath.startsWith("/") -> "path:$normalizedPath"
                    else -> "meta:${item.name.lowercase()}|${item.sizeInBytes}"
                }
            }
            .values
            .map { duplicates ->
                duplicates.maxByOrNull { item ->
                    // Preferir item com caminho absoluto e miniatura dedicada.
                    (if (item.path.startsWith("/")) 10 else 0) + (if (item.thumbnailPath != null) 1 else 0)
                } ?: duplicates.first()
            }

        return byPathOrMeta
            .groupBy { "${it.name.lowercase()}|${it.sizeInBytes}" }
            .values
            .map { duplicates ->
                duplicates.maxByOrNull { item ->
                    // Preferir miniatura dedicada, caminho absoluto e caminho mais longo.
                    (if (item.thumbnailPath != null) 100 else 0) +
                        (if (item.path.startsWith("/")) 10 else 0) +
                        item.path.length
                } ?: duplicates.first()
            }
            .sortedByDescending { it.sizeInBytes }
    }

    private fun findThumbnailPathForVideo(
        absolutePath: String?,
        relativePath: String?,
        videoName: String,
    ): String? {
        absolutePath
            ?.takeIf { it.startsWith("/") }
            ?.let { findThumbnailPathForVideoPath(it) }
            ?.let { return it }

        findThumbnailPathByRelativePath(
            relativePath = relativePath,
            baseName = videoName.substringBeforeLast('.', missingDelimiterValue = videoName),
        )?.let { return it }

        findThumbnailPathInMediaStore(
            relativePath = relativePath,
            baseName = videoName.substringBeforeLast('.', missingDelimiterValue = videoName),
        )?.let { return it }

        val baseName = videoName.substringBeforeLast('.', missingDelimiterValue = videoName)
        return findThumbnailPathByBaseName(baseName)
    }

    private fun findThumbnailPathForVideoPath(videoPath: String): String? {
        val videoFile = File(videoPath)
        val parent = videoFile.parentFile ?: return null
        if (!parent.exists() || !parent.isDirectory) return null

        val baseName = videoFile.name.substringBeforeLast('.', missingDelimiterValue = videoFile.name)
        return parent.listFiles()
            .orEmpty()
            .firstOrNull { candidate ->
                candidate.isFile &&
                    candidate.name.substringBeforeLast('.', missingDelimiterValue = candidate.name)
                        .equals(baseName, ignoreCase = true) &&
                    isThumbnailFile(candidate.name)
            }
            ?.absolutePath
    }

    private fun findThumbnailPathByBaseName(baseName: String): String? {
        if (baseName.isBlank()) return null

        val candidateDirs = listOf(
            File("/storage/emulated/0/Movies/play"),
            File("/storage/emulated/0/play"),
            File("/sdcard/Movies/play"),
            File("/sdcard/play"),
        )

        return candidateDirs
            .asSequence()
            .distinctBy { it.absolutePath }
            .filter { it.exists() && it.isDirectory }
            .flatMap { it.listFiles().orEmpty().asSequence() }
            .firstOrNull { candidate ->
                candidate.isFile &&
                    candidate.name.substringBeforeLast('.', missingDelimiterValue = candidate.name)
                        .equals(baseName, ignoreCase = true) &&
                    isThumbnailFile(candidate.name)
            }
            ?.absolutePath
    }

    private fun findThumbnailPathByRelativePath(relativePath: String?, baseName: String): String? {
        if (relativePath.isNullOrBlank() || baseName.isBlank()) return null

        val normalizedRelativePath = relativePath
            .replace('\\', '/')
            .trim('/')

        if (normalizedRelativePath.isBlank()) return null

        val candidateDirs = listOf(
            File("/storage/emulated/0/$normalizedRelativePath"),
            File("/sdcard/$normalizedRelativePath"),
        )

        return candidateDirs
            .asSequence()
            .distinctBy { it.absolutePath }
            .filter { it.exists() && it.isDirectory }
            .flatMap { it.listFiles().orEmpty().asSequence() }
            .firstOrNull { candidate ->
                candidate.isFile &&
                    candidate.name.substringBeforeLast('.', missingDelimiterValue = candidate.name)
                        .equals(baseName, ignoreCase = true) &&
                    isThumbnailFile(candidate.name)
            }
            ?.absolutePath
    }

    private fun isThumbnailFile(fileName: String): Boolean {
        val lowerCase = fileName.lowercase()
        return lowerCase.endsWith(".jpg") ||
            lowerCase.endsWith(".jpeg") ||
            lowerCase.endsWith(".png")
    }

    private fun findThumbnailPathInMediaStore(relativePath: String?, baseName: String): String? {
        if (baseName.isBlank() || relativePath.isNullOrBlank()) return null

        val normalizedRelativePath = normalizeRelativePath(relativePath)
        if (normalizedRelativePath.isEmpty()) return null

        val acceptedNames = setOf(
            "$baseName.jpg",
            "$baseName.jpeg",
            "$baseName.png",
        ).map { it.lowercase() }.toSet()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.RELATIVE_PATH,
        )

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            "${MediaStore.Images.Media.RELATIVE_PATH} = ?",
            arrayOf(normalizedRelativePath),
            "${MediaStore.Images.Media.DATE_MODIFIED} DESC",
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val relativePathColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)

            while (cursor.moveToNext()) {
                val displayName = cursor.getString(nameColumn) ?: continue
                if (displayName.lowercase() !in acceptedNames) continue
                val imageRelativePath = normalizeRelativePath(cursor.getString(relativePathColumn))
                if (imageRelativePath != normalizedRelativePath) continue

                val imageId = cursor.getLong(idColumn)
                return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId).toString()
            }
        }
        return null
    }

    private fun normalizeRelativePath(path: String?): String {
        val normalized = path
            .orEmpty()
            .replace('\\', '/')
            .trim('/')
        return if (normalized.isEmpty()) "" else "$normalized/"
    }
}
