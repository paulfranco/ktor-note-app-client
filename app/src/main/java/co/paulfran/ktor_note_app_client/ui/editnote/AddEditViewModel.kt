package co.paulfran.ktor_note_app_client.ui.editnote

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.paulfran.ktor_note_app_client.data.local.entities.Note
import co.paulfran.ktor_note_app_client.other.Event
import co.paulfran.ktor_note_app_client.other.Resource
import co.paulfran.ktor_note_app_client.repositories.NoteRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddEditViewModel  @ViewModelInject constructor(
        private val repository: NoteRepository
) : ViewModel() {

    private val _note = MutableLiveData<Event<Resource<Note>>>()
    val note: LiveData<Event<Resource<Note>>> = _note

    fun insertNote(note: Note) = GlobalScope.launch {
        repository.insertNote(note)
    }

    fun getNoteById(noteID: String) = viewModelScope.launch {
        _note.postValue(Event(Resource.loading(null)))
        val note = repository.getNoteById(noteID)
        note?.let {
            _note.postValue(Event(Resource.success(it)))
        } ?: _note.postValue(Event(Resource.error("Note not found", null)))
    }
}