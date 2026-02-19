package br.com.hellodev.play.list.di

import br.com.hellodev.play.list.viewmodel.VideoListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val videoListModule = module {
    viewModelOf(::VideoListViewModel)
}
