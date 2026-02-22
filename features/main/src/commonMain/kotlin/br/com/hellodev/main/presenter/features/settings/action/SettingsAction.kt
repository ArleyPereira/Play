package br.com.hellodev.main.presenter.features.settings.action

sealed interface SettingsAction {
    object OnChangeFolder : SettingsAction
    object OnFolderPickerUnsupported : SettingsAction
    object OnReindexFiles : SettingsAction
    object DismissFeedback : SettingsAction

    data class OnFolderSelected(val folderPath: String?) : SettingsAction
}
