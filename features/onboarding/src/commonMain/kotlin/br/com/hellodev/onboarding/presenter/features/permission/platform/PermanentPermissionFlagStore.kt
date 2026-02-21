package br.com.hellodev.onboarding.presenter.features.permission.platform

import androidx.compose.runtime.Composable

interface PermanentPermissionFlagStore {
    fun isPermanentlyDenied(): Boolean
    fun setPermanentlyDenied(value: Boolean)
}

@Composable
expect fun rememberPermanentPermissionFlagStore(): PermanentPermissionFlagStore
