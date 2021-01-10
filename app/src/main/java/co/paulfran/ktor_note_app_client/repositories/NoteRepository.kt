package co.paulfran.ktor_note_app_client.repositories

import android.app.Application
import co.paulfran.ktor_note_app_client.data.local.NoteDao
import co.paulfran.ktor_note_app_client.data.local.entities.LocallyDeletedNoteId
import co.paulfran.ktor_note_app_client.data.local.entities.Note
import co.paulfran.ktor_note_app_client.data.remote.NoteApi
import co.paulfran.ktor_note_app_client.data.remote.requests.AccountRequest
import co.paulfran.ktor_note_app_client.data.remote.requests.AddOwnerRequest
import co.paulfran.ktor_note_app_client.data.remote.requests.DeleteNoteRequest
import co.paulfran.ktor_note_app_client.other.Resource
import co.paulfran.ktor_note_app_client.other.checkForInternetConnection
import co.paulfran.ktor_note_app_client.other.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
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
                    syncNotes()
                    currentNotesResponse
                },
                saveFetchResult = { response ->
                    response?.body()?.let {
                        insertNotes(it.onEach { note -> note.isSynced = true })
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

    suspend fun insertNotes(notes: List<Note>) {
        notes.forEach {
            insertNote(it)
        }
    }

    suspend fun getNoteById(noteId: String) = noteDao.getNoteById(noteId)

    suspend fun deleteLocallyDeletedNoteId(deletedNoteId: String) {
        noteDao.deleteAllLocallyDeletedNoteId(deletedNoteId)
    }

    suspend fun deleteNote(noteId: String) {
        val response = try {
            noteApi.deleteNote(DeleteNoteRequest(noteId))
        } catch (e: Exception) {
            null
        }
        noteDao.deleteNoteById(noteId)
        if (response == null || !response.isSuccessful) {
            noteDao.insertLocallyDeletedNoteId(LocallyDeletedNoteId(noteId))
        } else {
            deleteLocallyDeletedNoteId(noteId)
        }
    }

    private var currentNotesResponse: Response<List<Note>>? = null

    suspend fun syncNotes() {
        val locallyDeletedNoteIds = noteDao.getAllLocallyDeletedNoteIds()
        locallyDeletedNoteIds.forEach { id -> deleteNote(id.deletedNoteId) }

        val unsyncedNotes = noteDao.getAllUnSyncedNotes()
        unsyncedNotes.forEach { note -> insertNote(note) }

        currentNotesResponse = noteApi.getNotes()
        currentNotesResponse?.body()?.let { notes ->
            noteDao.deleteAllNotes()
            insertNotes(notes.onEach { note -> note.isSynced = true })
        }
    }

    fun observeNoteById(noteId: String) = noteDao.observeNoteById(noteId)

    suspend fun addOwnerToNote(owner: String, noteID: String) = withContext(Dispatchers.IO) {
        try {
            val response = noteApi.addOwnerToNote(AddOwnerRequest(owner, noteID))
            if(response.isSuccessful && response.body()!!.successful) {
                Resource.success(response.body()?.message)
            } else {
                Resource.error(response.body()?.message ?: response.message(), null)
            }
        } catch(e: Exception) {
            Resource.error("Couldn't connect to the servers. Check your internet connection", null)
        }
    }
}