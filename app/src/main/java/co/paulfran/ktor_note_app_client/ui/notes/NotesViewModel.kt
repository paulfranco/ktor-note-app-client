package co.paulfran.ktor_note_app_client.ui.notes

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import co.paulfran.ktor_note_app_client.data.local.entities.Note
import co.paulfran.ktor_note_app_client.repositories.NoteRepository
import co.paulfran.ktor_note_app_client.other.Event
import co.paulfran.ktor_note_app_client.other.Resource


class NotesViewModel @ViewModelInject constructor(
        private val repository: NoteRepository
): ViewModel() {

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _allNotes = _forceUpdate.switchMap {
        repository.getAllNotes().asLiveData(viewModelScope.coroutineContext)
    }.switchMap {
        MutableLiveData(Event(it))
    }

    val allNotes: LiveData<Event<Resource<List<Note>>>> = _allNotes

    fun syncAllNotes() = _forceUpdate.postValue(true)
}