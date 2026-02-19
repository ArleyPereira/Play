package br.com.hellodev.play.list.action

import br.com.hellodev.play.VideoDataSource
import br.com.hellodev.play.VideoItem

sealed interface VideoListAction {
    data class Refresh(
        val hasAccess: Boolean,
        val dataSource: VideoDataSource,
    ) : VideoListAction

    data class OnVideoClick(val video: VideoItem) : VideoListAction
    data object OnPlayerBack : VideoListAction
}
