package br.com.hellodev.main.presenter.features.settings.state

import br.com.hellodev.domain.model.feedback.Feedback

data class SettingsState(
    val isLoading: Boolean = false,
    val isAccessAllowed: Boolean = true,
    val currentFolderPath: String = "/storage/emulated/0/Movies/play",
    val errorMessage: String? = null,
    val feedback: Feedback? = null,
)
