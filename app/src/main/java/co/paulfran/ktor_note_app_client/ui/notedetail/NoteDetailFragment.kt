package co.paulfran.ktor_note_app_client.ui.notedetail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.paulfran.ktor_note_app_client.R
import co.paulfran.ktor_note_app_client.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_note_detail.*

class NoteDetailFragment : BaseFragment(R.layout.fragment_note_detail) {

    private val args: NoteDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabEditNote.setOnClickListener {
            findNavController().navigate(
                    NoteDetailFragmentDirections.actionNoteDetailFragmentToAddEditNoteFragment(args.id)
            )
        }
    }
}