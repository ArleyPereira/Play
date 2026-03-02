package br.com.hellodev.main.data

data class VideoReindexResult(
    val importedCount: Int,
    val skippedCount: Int,
)

expect suspend fun reindexVideosToPrivateStorage(): VideoReindexResult
