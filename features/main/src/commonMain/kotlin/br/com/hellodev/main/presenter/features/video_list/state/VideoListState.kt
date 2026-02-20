package br.com.hellodev.main.presenter.features.video_list.state

import br.com.hellodev.main.data.VideoItem

data class VideoListState(
    val isLoading: Boolean = false,
    val isPermissionRequired: Boolean = true,
    val videos: List<VideoItem> = emptyList(),
    val errorMessage: String? = null,
)
