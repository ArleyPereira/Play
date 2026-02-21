package br.com.hellodev.main.presenter.navigation.routes

import kotlinx.serialization.Serializable

sealed class MainRoutes {

    @Serializable
    object Graph : MainRoutes()

    @Serializable
    object Main : MainRoutes()

}
