package co.paulfran.ktor_note_app_client.ui.notes

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import co.paulfran.ktor_note_app_client.R
import co.paulfran.ktor_note_app_client.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_notes.*

class NotesFragment : BaseFragment(R.layout.fragment_notes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabAddNote.setOnClickListener {
            findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(""))
        }
    }
}