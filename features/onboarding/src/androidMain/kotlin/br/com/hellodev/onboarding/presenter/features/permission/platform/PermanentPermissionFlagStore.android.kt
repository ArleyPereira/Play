package br.com.hellodev.onboarding.presenter.features.permission.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit

private const val PREFS_NAME = "play_permissions"
private const val KEY_GALLERY_PERMANENTLY_DENIED = "gallery_permanently_denied"

@Composable
actual fun rememberPermanentPermissionFlagStore(): PermanentPermissionFlagStore {
    val context = LocalContext.current.applicationContext
    val prefs = remember(context) {
        context.getSharedPreferences(PREFS_NAME, 0)
    }
    return remember(prefs) {
        object : PermanentPermissionFlagStore {
            override fun isPermanentlyDenied(): Boolean {
                return prefs.getBoolean(KEY_GALLERY_PERMANENTLY_DENIED, false)
            }

            override fun setPermanentlyDenied(value: Boolean) {
                prefs.edit { putBoolean(KEY_GALLERY_PERMANENTLY_DENIED, value) }
            }
        }
    }
}