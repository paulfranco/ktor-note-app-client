package co.paulfran.ktor_note_app_client.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locally_deleted_note_ids")
data class LocallyDeletedNoteId(
        @PrimaryKey(autoGenerate = false)
        val deletedNoteId: String
)