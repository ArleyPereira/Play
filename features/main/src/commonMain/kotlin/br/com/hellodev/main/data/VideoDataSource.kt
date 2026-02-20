package br.com.hellodev.main.data

interface VideoDataSource {
    suspend fun listVideos(): List<VideoItem>
}

class VideoPermissionException : IllegalStateException("Permission denied to read videos")
