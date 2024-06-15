package ru.mixail_akulov.a47_network_okhttp_gson.app.utils.logger

interface Logger {

    fun log(tag: String, message: String)

    fun error(tag: String, e: Throwable)

}