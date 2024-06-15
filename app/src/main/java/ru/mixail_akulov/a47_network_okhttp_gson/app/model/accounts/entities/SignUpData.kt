package ru.mixail_akulov.a47_network_okhttp_gson.app.model.accounts.entities

import ru.mixail_akulov.a47_network_okhttp_gson.app.model.EmptyFieldException
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.Field
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.PasswordMismatchException

/**
 * Fields that should be provided during creating a new account.
 */
data class SignUpData(
    val username: String,
    val email: String,
    val password: String,
    val repeatPassword: String
) {

    /**
     * @throws EmptyFieldException
     * @throws PasswordMismatchException
     */
    fun validate() {
        if (email.isBlank()) throw EmptyFieldException(Field.Email)
        if (username.isBlank()) throw EmptyFieldException(Field.Username)
        if (password.isBlank()) throw EmptyFieldException(Field.Password)
        if (password != repeatPassword) throw PasswordMismatchException()
    }
}
