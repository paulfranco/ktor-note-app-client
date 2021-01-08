package co.paulfran.ktor_note_app_client.repositories

import android.app.Application
import co.paulfran.ktor_note_app_client.data.local.NoteDao
import co.paulfran.ktor_note_app_client.data.local.entities.Note
import co.paulfran.ktor_note_app_client.data.remote.NoteApi
import co.paulfran.ktor_note_app_client.data.remote.requests.AccountRequest
import co.paulfran.ktor_note_app_client.other.Resource
import co.paulfran.ktor_note_app_client.other.checkForInternetConnection
import co.paulfran.ktor_note_app_client.other.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val noteApi: NoteApi,
    private val context: Application
) {
    fun getAllNotes(): Flow<Resource<List<Note>>> {
        return networkBoundResource(
                query = {
                    noteDao.getAllNotes()
                },
                fetch = {
                    noteApi.getNotes()
                },
                saveFetchResult = { response ->
                    response.body()?.let {
                        // TODO: insert notes in database
                    }
                },
                shouldFetch = {
                    checkForInternetConnection(context)
                }
        )
    }
    suspend fun register(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            val response = noteApi.register(AccountRequest(email, password))
            if (response.isSuccessful && response.body()!!.successful) {
                Resource.success(response.body()?.message)
            } else {
                Resource.error(response.body()?.message ?: response.message(), null)
            }
        } catch (e: Exception) {
            Resource.error("Could'nt connect to the servers. Check your internet connection", null)
        }
    }

    suspend fun login(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            val response = noteApi.login(AccountRequest(email, password))
            if (response.isSuccessful && response.body()!!.successful) {
                Resource.success(response.body()?.message)
            } else {
                Resource.error(response.body()?.message ?: response.message(), null)
            }
        } catch (e: Exception) {
            Resource.error("Could'nt connect to the servers. Check your internet connection", null)
        }
    }
    suspend fun insertNote(note:Note) {
        val response = try {
            noteApi.addNote(note)
        } catch (e: Exception) {
            null
        }
        if (response != null && response.isSuccessful) {
            noteDao.insertNote(note.apply { isSynced = true })
        } else {
            noteDao.insertNote(note)
        }
    }
}