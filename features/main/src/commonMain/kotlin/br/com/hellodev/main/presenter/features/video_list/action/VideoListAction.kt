package br.com.hellodev.main.presenter.features.video_list.action

import br.com.hellodev.main.data.VideoDataSource
import br.com.hellodev.main.data.VideoItem

sealed interface VideoListAction {
    data class Refresh(
        val hasAccess: Boolean,
        val dataSource: VideoDataSource,
    ) : VideoListAction

    data class OnVideoClick(val video: VideoItem) : VideoListAction
}
