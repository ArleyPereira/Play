package br.com.hellodev.main.presenter.features.settings.state

data class SettingsState(
    val isLoading: Boolean = false,
    val isAccessAllowed: Boolean = true,
    val currentFolderPath: String = "/storage/emulated/0/Movies/videos",
)
