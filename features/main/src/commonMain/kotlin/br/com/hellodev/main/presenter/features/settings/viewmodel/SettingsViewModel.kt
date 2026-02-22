package br.com.hellodev.main.presenter.features.settings.viewmodel

import androidx.lifecycle.ViewModel
import br.com.hellodev.main.presenter.features.settings.state.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(

) : ViewModel() {

    private var _state = MutableStateFlow(SettingsState())
    var state: StateFlow<SettingsState> = _state

    //private var _event: Channel<SettingsEvent> = Channel()
    //var event = _event.receiveAsFlow()

    init {

    }

//    fun dispatchAction(action: SettingsAction) {
//        when (action) {
//            else -> {}
//        }
//    }

}