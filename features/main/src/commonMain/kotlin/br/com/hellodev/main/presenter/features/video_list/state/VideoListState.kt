package br.com.hellodev.main.presenter.features.video_list.state

import br.com.hellodev.domain.model.sheet.DefaultSheetModel
import br.com.hellodev.domain.model.video.Video

data class VideoListState(
    val isLoading: Boolean = false,
    val isPermissionRequired: Boolean = false,
    val videos: List<Video> = emptyList(),
    val errorMessage: String? = null,
    val videoSelected: Video? = null,
    val sheetModel: DefaultSheetModel? = null
)
