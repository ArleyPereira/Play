package br.com.hellodev.onboarding.presenter.features.permission.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSUserDefaults

private const val KEY_GALLERY_PERMANENTLY_DENIED = "gallery_permanently_denied"

@Composable
actual fun rememberPermanentPermissionFlagStore(): PermanentPermissionFlagStore {
    val defaults = NSUserDefaults.standardUserDefaults
    return remember(defaults) {
        object : PermanentPermissionFlagStore {
            override fun isPermanentlyDenied(): Boolean {
                return defaults.boolForKey(KEY_GALLERY_PERMANENTLY_DENIED)
            }

            override fun setPermanentlyDenied(value: Boolean) {
                defaults.setBool(value, KEY_GALLERY_PERMANENTLY_DENIED)
            }
        }
    }
}