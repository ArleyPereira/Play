package br.com.hellodev.domain.model.video

data class Video(
    val id: String? = null,
    val name: String? = null,
    val path: String? = null,
    val thumbnailPath: String? = null,
    val sizeInBytes: Long? = null,
    val durationMillis: Long? = null
)