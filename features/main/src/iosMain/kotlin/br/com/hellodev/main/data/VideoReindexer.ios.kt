package br.com.hellodev.main.data

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSFileManager

actual suspend fun reindexVideosToPrivateStorage(): VideoReindexResult = withContext(Dispatchers.Default) {
    val root = "${FileKit.filesDir.path}/play"
    val videos = "$root/videos"
    val thumbs = "$root/thumbs"
    val fileManager = NSFileManager.defaultManager

    if (!fileManager.fileExistsAtPath(root)) fileManager.createDirectoryAtPath(root, true, null, null)
    if (!fileManager.fileExistsAtPath(videos)) fileManager.createDirectoryAtPath(videos, true, null, null)
    if (!fileManager.fileExistsAtPath(thumbs)) fileManager.createDirectoryAtPath(thumbs, true, null, null)

    VideoReindexResult(importedCount = 0, skippedCount = 0)
}
