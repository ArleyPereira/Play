package br.com.hellodev.main.data

data class VideoItem(
    val id: String,
    val name: String,
    val path: String,
    val thumbnailPath: String?,
    val sizeInBytes: Long,
    val durationMillis: Long?,
)
