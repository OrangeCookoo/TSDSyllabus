package uk.co.btsda.syllabus

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import uk.co.btsda.syllabus.data.Belt
import uk.co.btsda.syllabus.data.Category
import uk.co.btsda.syllabus.data.SyllabusData
import uk.co.btsda.syllabus.data.VideoLinks

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
    fun everyTechniqueHasADirectVideoLink() {
        val notDirect = SyllabusData.techniques.filterNot { it.hasDirectVideo }
        assertTrue("All techniques should link directly: $notDirect", notDirect.isEmpty())
        assertTrue(SyllabusData.techniques.all {
            it.videoUrl.startsWith("https://www.youtube.com/watch?v=")
        })
    }

    @Test
    fun watchUrlAppendsTimestampWhenProvided() {
        assertEquals("https://www.youtube.com/watch?v=abc", VideoLinks.watchUrl("abc", null))
        assertEquals("https://www.youtube.com/watch?v=abc&t=83s", VideoLinks.watchUrl("abc", 83))
    }

    @Test
    fun boStaffBlocksLinkToTheCorrectVideo() {
        fun bo(n: Int) = SyllabusData.byCategory(Category.BO_STAFF).first { it.number == n }
        assertEquals("https://www.youtube.com/watch?v=nZnX0kgs_Sw", bo(3).videoUrl)   // 1-5
        assertEquals("https://www.youtube.com/watch?v=qRzaGcJJ2E0", bo(8).videoUrl)   // 6-10
        assertEquals("https://www.youtube.com/watch?v=gjniNGbuPh4", bo(13).videoUrl)  // 11-15
        assertEquals("https://www.youtube.com/watch?v=-hYMwVNhH8o", bo(18).videoUrl)  // 16-20
        assertEquals("https://www.youtube.com/watch?v=dom7Iq__hqE", bo(23).videoUrl)  // 21-25
        assertEquals("https://www.youtube.com/watch?v=JLr2zCxpXME", bo(28).videoUrl)  // 26-30
    }

    @Test
    fun emptyHandCategoriesShareTheOneStepSparringVideos() {
        fun tech(c: Category, n: Int) = SyllabusData.byCategory(c).first { it.number == n }
        // Block 1-5 video is the same across hands, feet and self defense
        // (the timestamp differs per technique within that one clip).
        val block15 = "https://www.youtube.com/watch?v=I4dj-SSDh3Q"
        assertTrue(tech(Category.HANDS, 1).videoUrl.startsWith(block15))
        assertTrue(tech(Category.FEET, 4).videoUrl.startsWith(block15))
        assertTrue(tech(Category.SELF_DEFENSE, 5).videoUrl.startsWith(block15))
        // A later block resolves to its own video (Hands #16 opens the 16-20 clip).
        assertEquals(
            "https://www.youtube.com/watch?v=wotAiTXp9KU",
            tech(Category.HANDS, 16).videoUrl
        )
    }

    @Test
    fun timestampsJumpIntoTheSharedBlockVideo() {
        fun tech(c: Category, n: Int) = SyllabusData.byCategory(c).first { it.number == n }
        // Hands #1 has no offset -> opens at the start.
        assertEquals("https://www.youtube.com/watch?v=I4dj-SSDh3Q", tech(Category.HANDS, 1).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=I4dj-SSDh3Q&t=9s", tech(Category.HANDS, 2).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=I4dj-SSDh3Q&t=30s", tech(Category.FEET, 1).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=I4dj-SSDh3Q&t=80s", tech(Category.SELF_DEFENSE, 5).videoUrl)
    }

    @Test
    fun timestampsForTheSixToTenBlock() {
        fun tech(c: Category, n: Int) = SyllabusData.byCategory(c).first { it.number == n }
        // Hands #6 starts the 6-10 clip.
        assertEquals("https://www.youtube.com/watch?v=oB6q-AxH0cs", tech(Category.HANDS, 6).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=oB6q-AxH0cs&t=10s", tech(Category.HANDS, 7).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=oB6q-AxH0cs&t=60s", tech(Category.FEET, 9).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=oB6q-AxH0cs&t=107s", tech(Category.SELF_DEFENSE, 10).videoUrl)
    }

    @Test
    fun timestampsForTheElevenToFifteenBlock() {
        fun tech(c: Category, n: Int) = SyllabusData.byCategory(c).first { it.number == n }
        // Hands #11 starts the 11-15 clip; #12 onward carry offsets.
        assertEquals("https://www.youtube.com/watch?v=flCo2tl5_3Y", tech(Category.HANDS, 11).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=flCo2tl5_3Y&t=11s", tech(Category.HANDS, 12).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=flCo2tl5_3Y&t=41s", tech(Category.FEET, 11).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=flCo2tl5_3Y&t=107s", tech(Category.SELF_DEFENSE, 15).videoUrl)
    }

    @Test
    fun timestampsForTheSixteenToTwentyBlock() {
        fun tech(c: Category, n: Int) = SyllabusData.byCategory(c).first { it.number == n }
        // Hands #16 starts the 16-20 clip; #17 onward carry offsets.
        assertEquals("https://www.youtube.com/watch?v=wotAiTXp9KU", tech(Category.HANDS, 16).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=wotAiTXp9KU&t=22s", tech(Category.HANDS, 17).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=wotAiTXp9KU&t=74s", tech(Category.FEET, 16).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=wotAiTXp9KU&t=154s", tech(Category.SELF_DEFENSE, 20).videoUrl)
    }

    @Test
    fun timestampsForTheTwentyOneToTwentyFiveBlock() {
        fun tech(c: Category, n: Int) = SyllabusData.byCategory(c).first { it.number == n }
        assertEquals("https://www.youtube.com/watch?v=O_qJ9UYpzKU", tech(Category.HANDS, 21).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=O_qJ9UYpzKU&t=9s", tech(Category.HANDS, 22).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=O_qJ9UYpzKU&t=43s", tech(Category.FEET, 21).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=O_qJ9UYpzKU&t=126s", tech(Category.SELF_DEFENSE, 25).videoUrl)
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
