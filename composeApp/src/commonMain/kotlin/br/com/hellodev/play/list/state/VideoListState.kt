package br.com.hellodev.play.list.state

import br.com.hellodev.play.VideoItem

data class VideoListState(
    val isLoading: Boolean = false,
    val isPermissionRequired: Boolean = true,
    val videos: List<VideoItem> = emptyList(),
    val errorMessage: String? = null,
    val selectedVideo: VideoItem? = null,
)
