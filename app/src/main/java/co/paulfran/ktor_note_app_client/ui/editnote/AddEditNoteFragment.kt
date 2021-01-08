package co.paulfran.ktor_note_app_client.ui.editnote

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import co.paulfran.ktor_note_app_client.R
import co.paulfran.ktor_note_app_client.data.local.entities.Note
import co.paulfran.ktor_note_app_client.other.Constants.DEFAULT_NOTE_COLOR
import co.paulfran.ktor_note_app_client.other.Constants.KEY_LOGGED_IN_EMAIL
import co.paulfran.ktor_note_app_client.other.Constants.NO_EMAIL
import co.paulfran.ktor_note_app_client.ui.BaseFragment
import co.paulfran.ktor_note_app_client.ui.dialogs.ColorPickerDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_edit_note.*
import kotlinx.android.synthetic.main.item_note.view.*
import java.util.*
import javax.inject.Inject

const val FRAGMENT_TAG = "AddEditNoteFragment"

@AndroidEntryPoint
class AddEditNoteFragment : BaseFragment(R.layout.fragment_add_edit_note) {

    private val viewModel: AddEditViewModel by viewModels()

    private val args: AddEditNoteFragmentArgs by navArgs()

    private var currentNote: Note? = null
    private var currentNoteColor = DEFAULT_NOTE_COLOR

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.id.isNotEmpty()) {
            viewModel.getNoteById(args.id)
            subscribeToObservers()
        }

        if (savedInstanceState != null) {
            val colorPickerDialog = parentFragmentManager.findFragmentByTag(FRAGMENT_TAG) as ColorPickerDialogFragment?

            colorPickerDialog?.setPositiveListener {
                changeViewNoteCorlor(it)
            }
        }

        viewNoteColor.setOnClickListener {
            ColorPickerDialogFragment().apply {
                setPositiveListener {
                    changeViewNoteCorlor(it)
                }
            }.show(parentFragmentManager, FRAGMENT_TAG)
        }
    }

    private fun subscribeToObservers() {

    }

    override fun onPause() {
        super.onPause()
        saveNote()
    }

    private fun saveNote() {
        val authEmail = sharedPref.getString(KEY_LOGGED_IN_EMAIL, NO_EMAIL) ?: NO_EMAIL
        val title = etNoteTitle.text.toString()
        val content = etNoteContent.text.toString()
        if (title.isNotEmpty() || content.isNotEmpty()) {
            return
        }
        val date = System.currentTimeMillis()
        val color = currentNoteColor
        val id = currentNote?.id ?: UUID.randomUUID().toString()
        val owners = currentNote?.owners ?: listOf(authEmail)
        val note = Note(title, content, date, owners, color, id = id)
        viewModel.insertNote(note)
    }

    private fun changeViewNoteCorlor(colorString: String) {
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.circle_shape, null)
        drawable?.let {
            val wrappedDrawable = DrawableCompat.wrap(it)
            val color = Color.parseColor("#$colorString}")
            DrawableCompat.setTint(wrappedDrawable, color)
            viewNoteColor.background = wrappedDrawable
    }

}