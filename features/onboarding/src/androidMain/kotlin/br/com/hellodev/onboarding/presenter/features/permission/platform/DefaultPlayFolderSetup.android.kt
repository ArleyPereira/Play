package br.com.hellodev.onboarding.presenter.features.permission.platform

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.createDirectories

private const val DEFAULT_PLAY_FOLDER_PATH = "/storage/emulated/0/Movies/play"

actual suspend fun ensureDefaultPlayFolderExists() {
    runCatching {
        PlatformFile(DEFAULT_PLAY_FOLDER_PATH).createDirectories()
    }
}
