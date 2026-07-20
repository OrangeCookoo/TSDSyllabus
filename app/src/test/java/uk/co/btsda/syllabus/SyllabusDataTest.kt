package uk.co.btsda.syllabus

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import uk.co.btsda.syllabus.data.Belt
import uk.co.btsda.syllabus.data.Category
import uk.co.btsda.syllabus.data.SyllabusData

class SyllabusDataTest {

    @Test
    fun everyCategoryHasThirtyTechniques() {
        for (category in Category.entries) {
            assertEquals(
                "Category $category should have 30 techniques",
                30,
                SyllabusData.byCategory(category).size
            )
        }
        assertEquals(120, SyllabusData.techniques.size)
    }

    @Test
    fun techniqueIdsAreUnique() {
        val ids = SyllabusData.techniques.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun numbersAreContiguousOneToThirtyPerCategory() {
        for (category in Category.entries) {
            val numbers = SyllabusData.byCategory(category).map { it.number }.sorted()
            assertEquals((1..30).toList(), numbers)
        }
    }

    @Test
    fun allDefaultNotesArePopulated() {
        assertTrue(SyllabusData.techniques.all { it.defaultNote.isNotBlank() })
    }

    @Test
    fun blockRangesGroupInFives() {
        val t7 = SyllabusData.byCategory(Category.HANDS).first { it.number == 7 }
        assertEquals(6, t7.blockStart)
        assertEquals(10, t7.blockEnd)

        val t1 = SyllabusData.byCategory(Category.FEET).first { it.number == 1 }
        assertEquals(1, t1.blockStart)
        assertEquals(5, t1.blockEnd)

        val t30 = SyllabusData.byCategory(Category.BO_STAFF).first { it.number == 30 }
        assertEquals(26, t30.blockStart)
        assertEquals(30, t30.blockEnd)
    }

    @Test
    fun emptyHandVideoTitlesUseOneStepSparringSeries() {
        val hand3 = SyllabusData.byCategory(Category.HANDS).first { it.number == 3 }
        assertEquals("Il Soo Sik Dae Ryun (1 Step Sparring) 1-5", hand3.videoTitle)

        val foot18 = SyllabusData.byCategory(Category.FEET).first { it.number == 18 }
        assertEquals("Il Soo Sik Dae Ryun (1 Step Sparring) 16-20", foot18.videoTitle)

        val sd27 = SyllabusData.byCategory(Category.SELF_DEFENSE).first { it.number == 27 }
        assertEquals("Il Soo Sik Dae Ryun (1 Step Sparring) 26-30", sd27.videoTitle)
    }

    @Test
    fun boStaffVideoTitlesUseBoStaffSeries() {
        val bo17 = SyllabusData.byCategory(Category.BO_STAFF).first { it.number == 17 }
        assertEquals("Bo Staff 1 Steps 16-20", bo17.videoTitle)
    }

    @Test
    fun videoUrlPointsAtBtsdaChannelSearch() {
        val t = SyllabusData.techniques.first()
        assertTrue(t.videoUrl.startsWith("https://www.youtube.com/@bristoltangsoodoacademy/search?query="))
        // "1 Step Sparring" -> the space becomes + and parentheses are encoded
        assertTrue(t.videoUrl.contains("Sparring"))
    }

    @Test
    fun boStaffUsesRedTagBeltButOtherCategoriesDoNot() {
        assertTrue(SyllabusData.beltsIn(Category.BO_STAFF).contains(Belt.RED_TAG))
        assertFalse(SyllabusData.beltsIn(Category.HANDS).contains(Belt.RED_TAG))
    }

    @Test
    fun knownAuthorNotesArePreserved() {
        val hand10 = SyllabusData.byCategory(Category.HANDS).first { it.number == 10 }
        assertEquals("Tricky 10", hand10.defaultNote)

        val feet20 = SyllabusData.byCategory(Category.FEET).first { it.number == 20 }
        assertEquals("Dai apchagi", feet20.defaultNote)
    }
}
