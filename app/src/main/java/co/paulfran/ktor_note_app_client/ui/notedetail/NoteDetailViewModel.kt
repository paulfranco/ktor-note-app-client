package co.paulfran.ktor_note_app_client.ui.notedetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.paulfran.ktor_note_app_client.other.Event
import co.paulfran.ktor_note_app_client.other.Resource
import co.paulfran.ktor_note_app_client.repositories.NoteRepository
import kotlinx.coroutines.launch

class NoteDetailViewModel @ViewModelInject constructor(
        private val repository: NoteRepository
): ViewModel() {

    private val _addOwnerStatus = MutableLiveData<Event<Resource<String>>>()
    val addOwnerStatus: LiveData<Event<Resource<String>>> = _addOwnerStatus

    fun addOwnerToNote(owner: String, noteID: String) {
        _addOwnerStatus.postValue(Event(Resource.loading(null)))
        if(owner.isEmpty() || noteID.isEmpty()) {
            _addOwnerStatus.postValue(Event(Resource.error("The owner can't be empty", null)))
            return
        }
        viewModelScope.launch {
            val result = repository.addOwnerToNote(owner, noteID)
            _addOwnerStatus.postValue(Event(result))
        }
    }

    fun observeNoteById(noteId: String) = repository.observeNoteById(noteId)
}