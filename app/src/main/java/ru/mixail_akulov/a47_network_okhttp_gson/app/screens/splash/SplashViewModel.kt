package ru.mixail_akulov.a47_network_okhttp_gson.app.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.accounts.AccountsRepository
import ru.mixail_akulov.a47_network_okhttp_gson.app.utils.MutableLiveEvent
import ru.mixail_akulov.a47_network_okhttp_gson.app.utils.publishEvent
import ru.mixail_akulov.a47_network_okhttp_gson.app.utils.share
import ru.mixail_akulov.a47_network_okhttp_gson.app.Singletons

/**
 * SplashViewModel checks whether user is signed-in or not.
 */
class SplashViewModel(
    private val accountsRepository: AccountsRepository = Singletons.accountsRepository
) : ViewModel() {

    private val _launchMainScreenEvent = MutableLiveEvent<Boolean>()
    val launchMainScreenEvent = _launchMainScreenEvent.share()

    init {
        viewModelScope.launch {
            _launchMainScreenEvent.publishEvent(accountsRepository.isSignedIn())
        }
    }
}