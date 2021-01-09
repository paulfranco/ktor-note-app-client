package co.paulfran.ktor_note_app_client.ui.notedetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.paulfran.ktor_note_app_client.R
import co.paulfran.ktor_note_app_client.data.local.entities.Note
import co.paulfran.ktor_note_app_client.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import kotlinx.android.synthetic.main.fragment_note_detail.*

@AndroidEntryPoint
class NoteDetailFragment : BaseFragment(R.layout.fragment_note_detail) {

    private val viewModel: NoteDetailViewModel by viewModels()
    private val args: NoteDetailFragmentArgs by navArgs()
    private var currentNote: Note? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        fabEditNote.setOnClickListener {
            findNavController().navigate(
                    NoteDetailFragmentDirections.actionNoteDetailFragmentToAddEditNoteFragment(args.id)
            )
        }
    }

    private fun setMarkdownText(text: String) {
        val markwon = Markwon.create(requireContext())
        val markdown = markwon.toMarkdown(text)
        markwon.setParsedMarkdown(tvNoteContent, markdown)
    }

    private fun subscribeToObservers() {
        viewModel.observeNoteById(args.id).observe(viewLifecycleOwner, Observer {
            it?.let { note ->
                tvNoteTitle.text = note.title
                setMarkdownText(note.content)
                currentNote = note
            } ?: showSnackBar("Note not found")
        })
    }
}