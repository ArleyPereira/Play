package br.com.hellodev.domain.model.video

data class VideoItem(
    val id: String,
    val name: String,
    val path: String,
    val thumbnailPath: String?,
    val sizeInBytes: Long,
    val durationMillis: Long?
)