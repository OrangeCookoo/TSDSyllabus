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
    fun emptyHandVideoUrlFallsBackToSearch() {
        val t = SyllabusData.techniques.first() // Hands #1, no direct id yet
        assertFalse(t.hasDirectVideo)
        assertTrue(t.videoUrl.startsWith("https://www.youtube.com/results?search_query="))
        assertTrue(t.videoQuery.startsWith("Bristol Tang Soo Do Academy "))
        assertTrue(t.videoQuery.contains("Il Soo Sik Dae Ryun (1 Step Sparring) 1-5"))
        assertTrue(t.videoUrl.contains("Sparring"))
    }

    @Test
    fun confirmedBoStaffBlocksLinkDirectlyToTheVideo() {
        fun bo(n: Int) = SyllabusData.byCategory(Category.BO_STAFF).first { it.number == n }
        assertEquals("https://www.youtube.com/watch?v=dom7Iq__hqE", bo(3).videoUrl)   // 1-5
        assertEquals("https://www.youtube.com/watch?v=qRzaGcJJ2E0", bo(8).videoUrl)   // 6-10
        assertEquals("https://www.youtube.com/watch?v=gjniNGbuPh4", bo(13).videoUrl)  // 11-15
        assertEquals("https://www.youtube.com/watch?v=-hYMwVNhH8o", bo(18).videoUrl)  // 16-20
        assertTrue(bo(3).hasDirectVideo)
    }

    @Test
    fun unconfirmedBoStaffBlocksStillUseSearch() {
        val bo23 = SyllabusData.byCategory(Category.BO_STAFF).first { it.number == 23 } // 21-25 pending
        assertFalse(bo23.hasDirectVideo)
        assertTrue(bo23.videoUrl.startsWith("https://www.youtube.com/results?search_query="))
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
