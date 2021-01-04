package co.paulfran.ktor_note_app_client.other

open class Even<out T>(private val content: T) {

    var hasBeenHandled = false
    private set

    fun getContentIfNotHandled() = if (hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }

    fun peekContent() = content
}