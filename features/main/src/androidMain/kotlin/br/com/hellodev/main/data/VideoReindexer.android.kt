package br.com.hellodev.main.data

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

private val VIDEO_EXTENSIONS = setOf("mp4", "mkv", "mov", "webm", "avi")
private val THUMB_EXTENSIONS = setOf("jpg", "jpeg", "png")

actual suspend fun reindexVideosToPrivateStorage(): VideoReindexResult = withContext(Dispatchers.IO) {
    val sourceDirs = listOf(
        File("/storage/emulated/0/Movies/play"),
        File("/storage/emulated/0/play"),
        File("/sdcard/Movies/play"),
        File("/sdcard/play"),
    )

    val privateRoot = File("${FileKit.filesDir.path}/play")
    val privateVideosDir = File(privateRoot, "videos")
    val privateThumbsDir = File(privateRoot, "thumbs")

    privateVideosDir.mkdirs()
    privateThumbsDir.mkdirs()

    var importedCount = 0
    var skippedCount = 0
    var hadReadableSource = false
    val processedFiles = mutableSetOf<String>()

    val uniqueSourceDirs = sourceDirs
        .asSequence()
        .map { dir -> runCatching { dir.canonicalFile }.getOrElse { dir.absoluteFile } }
        .distinctBy { it.absolutePath }
        .filter { it.exists() && it.isDirectory }
        .toList()

    uniqueSourceDirs
        .asSequence()
        .forEach { dir ->
            val sourceFiles = dir.listFiles()
            if (sourceFiles == null) return@forEach
            hadReadableSource = true

            sourceFiles
                .asSequence()
                .filter { it.isFile }
                .forEach sourceFileLoop@{ sourceFile ->
                    val sourceCanonicalPath = runCatching { sourceFile.canonicalPath }.getOrElse { sourceFile.absolutePath }
                    if (!processedFiles.add(sourceCanonicalPath)) {
                        return@sourceFileLoop
                    }

                    val extension = sourceFile.extension.lowercase()
                    val destinationDirectory = when {
                        extension in VIDEO_EXTENSIONS -> privateVideosDir
                        extension in THUMB_EXTENSIONS -> privateThumbsDir
                        else -> null
                    }

                    if (destinationDirectory == null) {
                        skippedCount += 1
                        return@sourceFileLoop
                    }

                    val destinationFile = File(destinationDirectory, sourceFile.name)

                    if (
                        destinationFile.exists() &&
                        destinationFile.length() == sourceFile.length() &&
                        destinationFile.lastModified() >= sourceFile.lastModified()
                    ) {
                        skippedCount += 1
                        return@sourceFileLoop
                    }

                    runCatching {
                        sourceFile.inputStream().use { input ->
                            destinationFile.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                        destinationFile.setLastModified(sourceFile.lastModified())
                    }.onSuccess {
                        importedCount += 1
                    }.onFailure {
                        skippedCount += 1
                    }
                }
        }

    if (!hadReadableSource) {
        throw VideoPermissionException()
    }

    VideoReindexResult(
        importedCount = importedCount,
        skippedCount = skippedCount,
    )
}
