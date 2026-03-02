package br.com.hellodev.main.data

import br.com.hellodev.domain.model.video.Video

interface VideoDataSource {
    suspend fun listVideos(): List<Video>
}

class VideoPermissionException : IllegalStateException("Permission denied to read videos")
