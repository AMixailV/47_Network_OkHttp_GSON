package ru.mixail_akulov.a47_network_okhttp_gson.sources.accounts.entities

/**
 * Response body for `POST /sign-in` HTTP-request. Contains JWT-token
 * which should be placed to other requests as a value
 * of 'Authorization' header.
 *
 * JSON example:
 * ```
 * {
 *   "token": "jwt-token",
 * }
 * ```
 */
data class SignInResponseEntity(
    val token: String
)