package ru.mixail_akulov.a47_network_okhttp_gson.sources

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.mixail_akulov.a47_network_okhttp_gson.app.Const
import ru.mixail_akulov.a47_network_okhttp_gson.app.Singletons
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.SourcesProvider
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.settings.AppSettings
import ru.mixail_akulov.a47_network_okhttp_gson.sources.base.OkHttpConfig
import ru.mixail_akulov.a47_network_okhttp_gson.sources.base.OkHttpSourcesProvider

object SourceProviderHolder {

    val sourcesProvider: SourcesProvider by lazy {
        val config = OkHttpConfig(
            baseUrl = Const.BASE_URL,
            client = createOkHttpClient(),
            gson = Gson()
        )
        OkHttpSourcesProvider(config)
    }

    /**
     * Cоздайте экземпляр OkHttpClient с перехватчиками для авторизации и ведения журнала
     * (см. [createAuthorizationInterceptor] и [createLoggingInterceptor]).
     */
    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(createAuthorizationInterceptor(Singletons.appSettings))
            .addInterceptor(createLoggingInterceptor())
            .build()
    }

    /**
     * Добавьте заголовок авторизации в каждый запрос, если существует JWT-токен.
     */
    private fun createAuthorizationInterceptor(settings: AppSettings): Interceptor {
        return Interceptor { chain ->
            val newBuilder = chain.request().newBuilder()
            val token = settings.getCurrentToken()
            if (token != null) {
                newBuilder.addHeader("Authorization", token)
            }
            return@Interceptor chain.proceed(newBuilder.build())
        }
    }

    /**
     * Записывайте запросы и ответы в LogCat.
     */
    private fun createLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

}
