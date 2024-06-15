package ru.mixail_akulov.a47_network_okhttp_gson.app.model

import ru.mixail_akulov.a47_network_okhttp_gson.app.model.accounts.AccountsSource
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.boxes.BoxesSource

/**
 * Factory class for all network sources.
 */
interface SourcesProvider {

    /**
     * Create [AccountsSource] which is responsible for reading/writing
     * user accounts data.
     */
    fun getAccountsSource(): AccountsSource

    /**
     * Create [BoxesSource] which is responsible for reading/updating
     * boxes data.
     */
    fun getBoxesSource(): BoxesSource

}