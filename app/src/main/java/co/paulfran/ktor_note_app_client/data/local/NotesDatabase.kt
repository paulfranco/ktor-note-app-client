package co.paulfran.ktor_note_app_client.data.local

import androidx.room.Database
import androidx.room.TypeConverters
import co.paulfran.ktor_note_app_client.data.local.entities.Note

@Database(
    entities = [Note::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class NotesDatabase {

    abstract fun noteDao(): NoteDao

}