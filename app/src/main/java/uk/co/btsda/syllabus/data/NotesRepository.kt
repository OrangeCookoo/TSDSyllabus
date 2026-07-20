package uk.co.btsda.syllabus.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "syllabus_notes")

/**
 * Stores user overrides for technique notes. A technique with no stored value
 * falls back to its pre-populated [Technique.defaultNote].
 */
class NotesRepository(private val context: Context) {

    /** Map of technique id -> user-overridden note. Missing keys use defaults. */
    val overrides: Flow<Map<String, String>> = context.dataStore.data.map { prefs ->
        prefs.asMap().entries.associate { (k, v) -> k.name to v.toString() }
    }

    suspend fun setNote(id: String, note: String) {
        context.dataStore.edit { it[stringPreferencesKey(id)] = note }
    }

    /** Removes a user override, restoring the pre-populated default note. */
    suspend fun resetNote(id: String) {
        context.dataStore.edit { it.remove(stringPreferencesKey(id)) }
    }
}
