package br.com.hellodev.play

interface VideoDataSource {
    suspend fun listVideos(): List<VideoItem>
}

class VideoPermissionException : IllegalStateException("Permission denied to read videos")
