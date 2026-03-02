package br.com.hellodev.main.presenter.features.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.hellodev.core.enums.feedback.FeedbackType
import br.com.hellodev.domain.model.feedback.Feedback
import br.com.hellodev.main.data.reindexVideosToPrivateStorage
import br.com.hellodev.main.presenter.features.settings.action.SettingsAction
import br.com.hellodev.main.presenter.features.settings.state.SettingsState
import br.com.hellodev.main.presenter.features.video_list.reindex.VideoListReindexSignal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    fun dispatchAction(action: SettingsAction) {
        when (action) {
            SettingsAction.OnChangeFolder -> {
                onChangeFolder()
            }

            is SettingsAction.OnFolderSelected -> {
                onFolderSelected(action.folderPath.orEmpty())
            }

            SettingsAction.OnFolderPickerUnsupported -> {
                onFolderPickerUnsupported()

            }

            SettingsAction.OnReindexFiles -> {
                onReindexFiles()
            }

            SettingsAction.DismissFeedback -> {
                dismissFeedback()
            }
        }
    }

    private fun onChangeFolder() {
        _state.update { it.copy(errorMessage = null) }
    }

    private fun onFolderSelected(folderPath: String) {
        _state.update {
            if (folderPath.isBlank()) {
                it.copy(errorMessage = null)
            } else {
                it.copy(
                    currentFolderPath = folderPath,
                    isAccessAllowed = true,
                    errorMessage = null,
                )
            }
        }
    }

    private fun onFolderPickerUnsupported() {
        _state.update {
            it.copy(
                errorMessage = "Seleção de pasta não suportada nesta plataforma.",
            )
        }
    }

    private fun onReindexFiles() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null, feedback = null) }

            val result = runCatching { reindexVideosToPrivateStorage() }
            result.onSuccess { reindexResult ->
                VideoListReindexSignal.requestReindex()
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        feedback = Feedback(
                            title = "Reindexado: ${reindexResult.importedCount} importados e ${reindexResult.skippedCount} ignorados",
                            type = FeedbackType.SUCCESS,
                        ),
                    )
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Falha ao reindexar arquivos",
                        feedback = Feedback(
                            title = "Falha ao reindexar arquivos",
                            type = FeedbackType.ERROR,
                        ),
                    )
                }
            }
        }
    }

    private fun dismissFeedback() {
        _state.update { it.copy(feedback = null) }
    }
}
