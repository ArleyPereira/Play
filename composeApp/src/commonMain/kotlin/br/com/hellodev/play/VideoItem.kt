package br.com.hellodev.play

data class VideoItem(
    val id: String,
    val name: String,
    val path: String,
    val thumbnailPath: String?,
    val sizeInBytes: Long,
    val durationMillis: Long?,
)
