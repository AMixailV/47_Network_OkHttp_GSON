package ru.mixail_akulov.a47_network_okhttp_gson.sources.boxes

import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import okhttp3.Request
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.boxes.BoxesSource
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.boxes.entities.BoxAndSettings
import ru.mixail_akulov.a47_network_okhttp_gson.app.model.boxes.entities.BoxesFilter
import ru.mixail_akulov.a47_network_okhttp_gson.sources.base.BaseOkHttpSource
import ru.mixail_akulov.a47_network_okhttp_gson.sources.base.OkHttpConfig
import ru.mixail_akulov.a47_network_okhttp_gson.sources.boxes.entities.GetBoxResponseEntity
import ru.mixail_akulov.a47_network_okhttp_gson.sources.boxes.entities.UpdateBoxRequestEntity

class OkHttpBoxesSource(
    config: OkHttpConfig
) : BaseOkHttpSource(config), BoxesSource {

    override suspend fun getBoxes(boxesFilter: BoxesFilter): List<BoxAndSettings> {
        delay(500)
        val args = if (boxesFilter == BoxesFilter.ONLY_ACTIVE) "?active=true" else ""
        val request = Request.Builder()
            .get()
            .endpoint("/boxes$args")
            .build()
        val call = client.newCall(request)
        val typeToken = object : TypeToken<List<GetBoxResponseEntity>>() {}
        val response = call.suspendEnqueue().parseJsonResponse(typeToken)
        return response.map { it.toBoxAndSettings() }
    }

    override suspend fun setIsActive(boxId: Long, isActive: Boolean) {
        val updateBoxRequestEntity = UpdateBoxRequestEntity(isActive)
        val request = Request.Builder()
            .put(updateBoxRequestEntity.toJsonRequestBody())
            .endpoint("/boxes/${boxId}")
            .build()
        val call = client.newCall(request)
        call.suspendEnqueue()
    }

}