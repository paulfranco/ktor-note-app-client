package co.paulfran.ktor_note_app_client.data.remote.responses

data class SimpleResponse(
    val successful: Boolean,
    val message: String
)