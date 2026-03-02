package br.com.hellodev.main.presenter.features.video_list.action

import br.com.hellodev.core.enums.sheet.BottomSheetType
import br.com.hellodev.domain.model.video.Video
import br.com.hellodev.main.data.VideoDataSource

sealed class VideoListAction {

    object ClearBottomSheet : VideoListAction()

    data class Refresh(
        val dataSource: VideoDataSource,
    ) : VideoListAction()

    data class OnVideoSelected(val video: Video) : VideoListAction()

    data class SetCurrentBottomSheet(val type: BottomSheetType) : VideoListAction()

}
