package br.com.hellodev.onboarding.presenter.navigation.routes

import kotlinx.serialization.Serializable

sealed class OnboardingRoutes {

    @Serializable
    object Graph: OnboardingRoutes()

    @Serializable
    object Splash: OnboardingRoutes()

    @Serializable
    object Permissions: OnboardingRoutes()

}