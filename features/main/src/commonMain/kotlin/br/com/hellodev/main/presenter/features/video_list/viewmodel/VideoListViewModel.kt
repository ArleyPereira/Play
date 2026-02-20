package br.com.hellodev.main.presenter.features.video_list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.hellodev.main.data.VideoDataSource
import br.com.hellodev.main.data.VideoPermissionException
import br.com.hellodev.main.presenter.features.video_list.action.VideoListAction
import br.com.hellodev.main.presenter.features.video_list.state.VideoListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VideoListViewModel : ViewModel() {

    private val _state = MutableStateFlow(VideoListState())
    val state = _state.asStateFlow()

    fun dispatchAction(action: VideoListAction) {
        when (action) {
            is VideoListAction.Refresh -> refresh(dataSource = action.dataSource)
            is VideoListAction.OnVideoClick -> Unit
        }
    }

    private fun refresh(
        dataSource: VideoDataSource,
    ) {
        viewModelScope.launch {
            _state.update { current ->
                current.copy(
                    isLoading = true,
                    isPermissionRequired = false,
                    errorMessage = null,
                    videos = emptyList(),
                )
            }

            val nextState = try {
                val videos = dataSource.listVideos()
                VideoListState(
                    isLoading = false,
                    isPermissionRequired = false,
                    videos = videos,
                    errorMessage = null,
                )
            } catch (error: Throwable) {
                if (error is VideoPermissionException) {
                    VideoListState(
                        isLoading = false,
                        isPermissionRequired = true,
                        videos = emptyList(),
                        errorMessage = null,
                    )
                } else {
                    VideoListState(
                        isLoading = false,
                        isPermissionRequired = false,
                        videos = emptyList(),
                        errorMessage = error.message ?: "Falha ao listar videos",
                    )
                }
            }

            _state.value = nextState
        }
    }
}
