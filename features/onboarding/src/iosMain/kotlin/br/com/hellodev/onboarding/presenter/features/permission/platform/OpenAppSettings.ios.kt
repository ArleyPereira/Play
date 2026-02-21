package br.com.hellodev.onboarding.presenter.features.permission.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

@Composable
actual fun rememberOpenAppSettingsAction(): () -> Unit {
    return remember {
        {
            val settingsUrl = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
            if (settingsUrl != null) {
                UIApplication.sharedApplication.openURL(settingsUrl)
            }
        }
    }
}
