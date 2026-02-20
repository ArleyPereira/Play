package br.com.hellodev.main.data

data class VideoAccessState(
    val hasAccess: Boolean,
    val requestAccess: () -> Unit,
)
