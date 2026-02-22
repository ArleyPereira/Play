package br.com.hellodev.main.presenter.features.settings.viewmodel

import androidx.lifecycle.ViewModel
import br.com.hellodev.core.enums.feedback.FeedbackType
import br.com.hellodev.domain.model.feedback.Feedback
import br.com.hellodev.main.presenter.features.settings.action.SettingsAction
import br.com.hellodev.main.presenter.features.settings.state.SettingsState
import br.com.hellodev.main.presenter.features.video_list.reindex.VideoListReindexSignal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
        VideoListReindexSignal.requestReindex()
        _state.update {
            it.copy(
                isLoading = false,
                errorMessage = null,
                feedback = Feedback(
                    title = "Arquivos reindexados com sucesso",
                    type = FeedbackType.SUCCESS,
                ),
            )
        }
    }

    private fun dismissFeedback() {
        _state.update { it.copy(feedback = null) }
    }
}
