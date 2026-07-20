package uk.co.btsda.syllabus.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uk.co.btsda.syllabus.data.NotesRepository
import uk.co.btsda.syllabus.data.SyllabusData
import uk.co.btsda.syllabus.data.Technique

class SyllabusViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = NotesRepository(app)

    /** Current note for each technique id (override if present, else default). */
    val notes: StateFlow<Map<String, String>> =
        repo.overrides.map { overrides ->
            SyllabusData.techniques.associate { t ->
                t.id to (overrides[t.id] ?: t.defaultNote)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SyllabusData.techniques.associate { it.id to it.defaultNote }
        )

    /** Ids that currently have a user override (used to show a "custom" marker). */
    val customized: StateFlow<Set<String>> =
        repo.overrides.map { it.keys }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    fun noteFor(t: Technique): String = notes.value[t.id] ?: t.defaultNote

    fun saveNote(id: String, note: String) = viewModelScope.launch {
        repo.setNote(id, note.trim())
    }

    fun resetNote(id: String) = viewModelScope.launch {
        repo.resetNote(id)
    }
}
