package ru.mixail_akulov.a47_network_okhttp_gson.sources.base

import ru.mixail_akulov.a47_network_okhttp_gson.app.model.SourcesProvider
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.accounts.AccountsSource
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.boxes.BoxesSource
import ru.mixail_akulov.a47_network_okhttp_gson.sources.accounts.OkHttpAccountsSource
import ru.mixail_akulov.a47_network_okhttp_gson.sources.boxes.OkHttpBoxesSource

/**
 * Создание исходников на основе OkHttp+GSON.
 */
class OkHttpSourcesProvider(
    private val config: OkHttpConfig
) : SourcesProvider {

    override fun getAccountsSource(): AccountsSource {
        return OkHttpAccountsSource(config)
    }

    override fun getBoxesSource(): BoxesSource {
        return OkHttpBoxesSource(config)
    }

}