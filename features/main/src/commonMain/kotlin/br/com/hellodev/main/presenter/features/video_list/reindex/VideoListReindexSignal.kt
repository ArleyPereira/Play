package br.com.hellodev.main.presenter.features.video_list.reindex

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object VideoListReindexSignal {
    private val _signal = MutableStateFlow(0L)
    val signal: StateFlow<Long> = _signal.asStateFlow()

    fun requestReindex() {
        _signal.value += 1L
    }
}
