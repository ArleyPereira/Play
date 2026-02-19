package br.com.hellodev.play.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.hellodev.play.VideoDataSource
import br.com.hellodev.play.VideoPermissionException
import br.com.hellodev.play.list.action.VideoListAction
import br.com.hellodev.play.list.state.VideoListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VideoListViewModel : ViewModel() {

    private val _state = MutableStateFlow(VideoListState())
    val state = _state.asStateFlow()

    fun dispatchAction(action: VideoListAction) {
        when (action) {
            is VideoListAction.Refresh -> refresh(
                hasAccess = action.hasAccess,
                dataSource = action.dataSource,
            )
            is VideoListAction.OnVideoClick -> {
                _state.update { current ->
                    current.copy(selectedVideo = action.video)
                }
            }
            VideoListAction.OnPlayerBack -> {
                _state.update { current ->
                    current.copy(selectedVideo = null)
                }
            }
        }
    }

    private fun refresh(
        hasAccess: Boolean,
        dataSource: VideoDataSource,
    ) {
        if (!hasAccess) {
            _state.update { current ->
                current.copy(
                    isLoading = false,
                    isPermissionRequired = true,
                    videos = emptyList(),
                    errorMessage = null,
                )
            }
            return
        }

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
                    selectedVideo = _state.value.selectedVideo,
                )
            } catch (error: Throwable) {
                if (error is VideoPermissionException) {
                    VideoListState(
                        isLoading = false,
                        isPermissionRequired = true,
                        videos = emptyList(),
                        errorMessage = null,
                        selectedVideo = _state.value.selectedVideo,
                    )
                } else {
                    VideoListState(
                        isLoading = false,
                        isPermissionRequired = false,
                        videos = emptyList(),
                        errorMessage = error.message ?: "Falha ao listar videos",
                        selectedVideo = _state.value.selectedVideo,
                    )
                }
            }

            _state.value = nextState
        }
    }
}
