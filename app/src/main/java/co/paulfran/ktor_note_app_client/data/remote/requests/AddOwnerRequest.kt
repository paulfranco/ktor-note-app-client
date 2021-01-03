package co.paulfran.ktor_note_app_client.data.remote.requests

data class AddOwnerRequest(
    val owner: String,
    val noteId: String
)