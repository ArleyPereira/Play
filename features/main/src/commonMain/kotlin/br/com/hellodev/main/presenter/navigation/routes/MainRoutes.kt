package br.com.hellodev.main.presenter.navigation.routes

import kotlinx.serialization.Serializable

sealed class MainRoutes {

    @Serializable
    object Graph : MainRoutes()

    @Serializable
    object VideoList : MainRoutes()

    @Serializable
    data class VideoPlayer(
        val id: String,
        val name: String,
        val path: String,
        val thumbnailPath: String?,
        val sizeInBytes: Long,
        val durationMillis: Long?,
    ) : MainRoutes()

}
