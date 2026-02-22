package br.com.hellodev.main.di

import br.com.hellodev.main.presenter.features.settings.viewmodel.SettingsViewModel
import br.com.hellodev.main.presenter.features.video_list.viewmodel.VideoListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainModule = module {

    viewModelOf(::VideoListViewModel)

    viewModelOf(::SettingsViewModel)

}
