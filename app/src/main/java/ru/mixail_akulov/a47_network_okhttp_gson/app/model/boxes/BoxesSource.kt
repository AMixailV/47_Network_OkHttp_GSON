package ru.mixail_akulov.a47_network_okhttp_gson.app.model.boxes

import ru.mixail_akulov.a47_network_okhttp_gson.app.model.BackendException
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.ConnectionException
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.ParseBackendResponseException
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.boxes.entities.BoxAndSettings
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.boxes.entities.BoxesFilter

interface BoxesSource {

    /**
     * Get the list of all boxes for the current logged-in user.
     * @throws BackendException
     * @throws ConnectionException
     * @throws ParseBackendResponseException
     */
    suspend fun getBoxes(boxesFilter: BoxesFilter): List<BoxAndSettings>

    /**
     * Set isActive flag for the specified box.
     * @throws BackendException
     * @throws ConnectionException
     * @throws ParseBackendResponseException
     */
    suspend fun setIsActive(boxId: Long, isActive: Boolean)

}
