package ru.mixail_akulov.a47_network_okhttp_gson.sources.base

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.AppException
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.BackendException
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.ConnectionException
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.ParseBackendResponseException
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Базовый класс для всех источников OkHttp.
 */
open class BaseOkHttpSource(
    private val config: OkHttpConfig
) {
    val gson: Gson = config.gson
    val client: OkHttpClient = config.client

    private val contentType = "application/json; charset=utf-8".toMediaType()

    /**
     * Функция приостановки, которая оборачивает метод OkHttp [Call.enqueue] для выполнения HTTP-запросов
     * и переносит внешние исключения в подклассы [AppException].
     *
     * @throws ConnectionException
     * @throws BackendException
     * @throws ParseBackendResponseException
     */
    suspend fun Call.suspendEnqueue(): Response {
        return suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                cancel()
            }
            enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    val appException = ConnectionException(e)
                    continuation.resumeWithException(appException)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        // well done
                        continuation.resume(response)
                    } else {
                        handleErrorResponse(response, continuation)
                    }
                }
            })
        }
    }

    /**
     * Объедините базовый URL-адрес с путем и аргументами запроса.
     */
    fun Request.Builder.endpoint(endpoint: String): Request.Builder {
        url("${config.baseUrl}$endpoint")
        return this
    }

    /**
     * Преобразовать класс данных в [RequestBody] в формате JSON.
     */
    fun <T> T.toJsonRequestBody(): RequestBody {
        val json = gson.toJson(this)
        return json.toRequestBody(contentType)
    }

    /**
     * Разберите экземпляр OkHttp [Response] в объект данных.
     * Тип является производным от TypeToken, переданного этой функции в качестве второго аргумента.
     * Обычно этот метод используется для анализа массивов JSON.
     *
     * @throws ParseBackendResponseException
     */
    fun <T> Response.parseJsonResponse(typeToken: TypeToken<T>): T {
        try {
            return gson.fromJson(this.body!!.string(), typeToken.type)
        } catch (e: Exception) {
            throw ParseBackendResponseException(e)
        }
    }

    /**
     * Разберите экземпляр OkHttp [Response] в объект данных.
     * Тип является производным от универсального типа [T].
     * Обычно этот метод используется для анализа объектов JSON.
     *
     * @throws ParseBackendResponseException
     */
    inline fun <reified T> Response.parseJsonResponse(): T {
        try {
            return gson.fromJson(this.body!!.string(), T::class.java)
        } catch (e: Exception) {
            throw ParseBackendResponseException(e)
        }
    }

    /**
     * 1. Преобразуйте ответ об ошибке от сервера в [BackendException] и выдайте последнее.
     * 2. Выдайте [ParseBackendResponseException], если процесс анализа ответа об ошибке не удался.
     */
    private fun handleErrorResponse(response: Response,
                                    continuation: CancellableContinuation<Response>) {
        val httpCode = response.code
        try {
            // parse error body:
            // {
            //   "error": "..."
            // }
            val map = gson.fromJson(response.body!!.string(), Map::class.java)
            val message = map["error"].toString()
            continuation.resumeWithException(BackendException(httpCode, message))
        } catch (e: Exception) {
            // не удалось проанализировать тело ошибки → выдать исключение синтаксического анализа
            val appException = ParseBackendResponseException(e)
            continuation.resumeWithException(appException)
        }
    }

}