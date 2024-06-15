package ru.mixail_akulov.a47_network_okhttp_gson.sources.base

import com.google.gson.Gson
import okhttp3.OkHttpClient

/**
 * Все необходимое для выполнения HTTP-запросов с помощью
 * клиента OkHttp и анализа JSON-сообщений.
 */
class OkHttpConfig(
    val baseUrl: String,        // префикс для всех конечных точек
    val client: OkHttpClient,   // для выполнения HTTP-запросов
    val gson: Gson              // для парсинга JSON-сообщений
)