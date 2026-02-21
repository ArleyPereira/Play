package br.com.hellodev.main.presenter.features.video_list.state

import br.com.hellodev.domain.model.video.VideoItem

data class VideoListState(
    val isLoading: Boolean = false,
    val isPermissionRequired: Boolean = false,
    val videos: List<VideoItem> = emptyList(),
    val errorMessage: String? = null,
)
