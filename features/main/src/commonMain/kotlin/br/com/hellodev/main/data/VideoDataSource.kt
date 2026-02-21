package br.com.hellodev.main.data

import br.com.hellodev.domain.model.video.VideoItem

interface VideoDataSource {
    suspend fun listVideos(): List<VideoItem>
}

class VideoPermissionException : IllegalStateException("Permission denied to read videos")
