package br.com.hellodev.play

data class VideoAccessState(
    val hasAccess: Boolean,
    val requestAccess: () -> Unit,
)
