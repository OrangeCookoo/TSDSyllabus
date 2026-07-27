package uk.co.btsda.syllabus.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.quizStatsStore: DataStore<Preferences> by preferencesDataStore(name = "quiz_stats")

/**
 * Tracks how many times each technique has been missed in the quiz. Missed
 * techniques are drawn more often (see [quizDrawWeight]); a correct answer
 * clears the count so the technique returns to normal frequency.
 */
class QuizStatsRepository(private val context: Context) {

    /** Map of technique id -> consecutive miss count (absent = 0). */
    val missCounts: Flow<Map<String, Int>> = context.quizStatsStore.data.map { prefs ->
        prefs.asMap().entries.associate { (k, v) -> k.name to ((v as? Int) ?: 0) }
    }

    suspend fun recordMiss(id: String) {
        context.quizStatsStore.edit { prefs ->
            val key = intPreferencesKey(id)
            prefs[key] = (prefs[key] ?: 0) + 1
        }
    }

    /** A correct answer resets the technique to normal frequency. */
    suspend fun recordCorrect(id: String) {
        context.quizStatsStore.edit { it.remove(intPreferencesKey(id)) }
    }
}

/**
 * Draw weight for a technique given how many times it has been missed.
 * A fresh technique weighs 1; each miss adds 3, so a once-missed technique is
 * four times as likely to be picked, and getting it right (count -> 0) returns
 * it to baseline.
 */
fun quizDrawWeight(missCount: Int): Int = 1 + missCount.coerceAtLeast(0) * 3
