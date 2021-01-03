package co.paulfran.ktor_note_app_client.other

object Constants {

    val IGNORE_AUTH_URLS = listOf("/login", "/register")

    const val DATABASE_NAME = "notes_db"
    const val BASE_URL = "http://10.0.2.2:8080"
    const val ENCRYPTED_SHARED_PREF_NAME = "enc_shared_pref"
}