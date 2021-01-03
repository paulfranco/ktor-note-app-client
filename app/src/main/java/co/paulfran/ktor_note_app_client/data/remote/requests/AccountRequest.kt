package co.paulfran.ktor_note_app_client.data.remote.requests

data class AccountRequest(
    val email: String,
    val password: String
)