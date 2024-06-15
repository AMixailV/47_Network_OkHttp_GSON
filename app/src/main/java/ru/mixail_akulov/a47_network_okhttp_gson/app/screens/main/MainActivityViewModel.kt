package ru.mixail_akulov.a47_network_okhttp_gson.app.screens.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mixail_akulov.a47_network_okhttp_gson.app.utils.share
import ru.mixail_akulov.a47_network_okhttp_gson.app.Singletons
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.accounts.AccountsRepository

class MainActivityViewModel(
    private val accountsRepository: AccountsRepository = Singletons.accountsRepository
) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username = _username.share()

    init {
        viewModelScope.launch {
            // listening for the current account and send the username to be displayed in the toolbar
            accountsRepository.getAccount().collect { result ->
                _username.value = result.getValueOrNull()?.username?.let { "@$it" } ?: ""
            }
        }
    }

}