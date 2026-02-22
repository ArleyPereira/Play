package br.com.hellodev.onboarding.presenter.features.permission.platform

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.createDirectories

actual suspend fun ensureDefaultPlayFolderExists() {
    runCatching {
        val folderPath = "${FileKit.filesDir.path}/play"
        PlatformFile(folderPath).createDirectories()
    }
}
