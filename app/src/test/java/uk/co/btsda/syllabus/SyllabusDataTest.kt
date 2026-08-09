package uk.co.btsda.syllabus

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import uk.co.btsda.syllabus.data.Belt
import uk.co.btsda.syllabus.data.Category
import uk.co.btsda.syllabus.data.Forms
import uk.co.btsda.syllabus.data.SyllabusData
import uk.co.btsda.syllabus.data.VideoLinks
import uk.co.btsda.syllabus.data.quizDrawWeight

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
    fun timestampsForTheTwentySixToThirtyBlock() {
        fun tech(c: Category, n: Int) = SyllabusData.byCategory(c).first { it.number == n }
        assertEquals("https://www.youtube.com/watch?v=dcsgmRmmAss", tech(Category.HANDS, 26).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=dcsgmRmmAss&t=8s", tech(Category.HANDS, 27).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=dcsgmRmmAss&t=40s", tech(Category.FEET, 26).videoUrl)
        assertEquals("https://www.youtube.com/watch?v=dcsgmRmmAss&t=104s", tech(Category.SELF_DEFENSE, 30).videoUrl)
    }

    @Test
    fun everyEmptyHandTechniqueHasATimestampExceptBlockStarts() {
        // Blocks start at 1,6,11,16,21,26 -> those open at 0 (no offset); the
        // rest of the empty-hand syllabus should carry a timestamp.
        val blockStarts = setOf(1, 6, 11, 16, 21, 26)
        for (c in listOf(Category.HANDS, Category.FEET, Category.SELF_DEFENSE)) {
            for (t in SyllabusData.byCategory(c)) {
                val hasTs = t.videoUrl.contains("&t=")
                // Hands block-starts open at 0; feet/self-defense always mid-clip.
                val expectTs = !(c == Category.HANDS && t.number in blockStarts)
                assertEquals("${c}#${t.number} timestamp presence", expectTs, hasTs)
            }
        }
    }

    @Test
    fun beltViewGroupsAllCategoriesForABelt() {
        assertEquals(
            listOf(
                Belt.WHITE, Belt.ORANGE, Belt.GREEN, Belt.BROWN, Belt.BROWN_TAG,
                Belt.RED, Belt.RED_TAG, Belt.BLUE
            ),
            SyllabusData.beltsRanked
        )
        // Red belt: hands 21-25, feet 21-25, self defense 21-25, bo staff 11-15.
        val red = SyllabusData.byBelt(Belt.RED)
        assertEquals(20, red.size)
        assertTrue(red.map { it.category }.toSet().size >= 3)
    }

    @Test
    fun quizPoolRespectsBeltAndScope() {
        assertEquals(listOf(Belt.WHITE, Belt.ORANGE, Belt.GREEN), SyllabusData.beltAndBelow(Belt.GREEN))
        // White has no bo staff -> 15 empty-hand techniques; nothing below it.
        assertEquals(15, SyllabusData.quizPool(Belt.WHITE, includeBelow = false).size)
        assertEquals(15, SyllabusData.quizPool(Belt.WHITE, includeBelow = true).size)
        // Red alone = 20; red and below = White15 + Orange15 + Green15 +
        // Brown20 + BrownTag5 + Red20 = 90.
        assertEquals(20, SyllabusData.quizPool(Belt.RED, includeBelow = false).size)
        assertEquals(90, SyllabusData.quizPool(Belt.RED, includeBelow = true).size)
    }

    @Test
    fun formsAreDefinedPerEmptyHandBelt() {
        assertEquals(11, Forms.all.size)
        assertEquals(2, Forms.forBelt(Belt.WHITE).size)
        assertEquals(2, Forms.forBelt(Belt.ORANGE).size)
        assertEquals(2, Forms.forBelt(Belt.GREEN).size)
        assertEquals(2, Forms.forBelt(Belt.BROWN).size)
        assertEquals(2, Forms.forBelt(Belt.RED).size)
        assertEquals(1, Forms.forBelt(Belt.BLUE).size)
        // Bo-staff-only belts have no forms.
        assertTrue(Forms.forBelt(Belt.BROWN_TAG).isEmpty())
        assertTrue(Forms.forBelt(Belt.RED_TAG).isEmpty())
        assertEquals(
            listOf(Belt.WHITE, Belt.ORANGE, Belt.GREEN, Belt.BROWN, Belt.RED, Belt.BLUE),
            Forms.beltsWithForms
        )
        assertEquals("Sipsoo", Forms.forBelt(Belt.BLUE).single().name)
    }

    @Test
    fun everyFormLinksDirectlyToItsVideo() {
        assertTrue(Forms.all.all { it.videoUrl.startsWith("https://www.youtube.com/watch?v=") })
        val bassai = Forms.forBelt(Belt.RED).first { it.name == "Bassai" }
        assertEquals("https://www.youtube.com/watch?v=KaxkLdtlT6g", bassai.videoUrl)
        assertEquals(
            "https://www.youtube.com/watch?v=XmgnZGCY3xI",
            Forms.forBelt(Belt.BLUE).single().videoUrl
        )
        assertEquals(
            "https://www.youtube.com/watch?v=xA7850ho2Ms",
            Forms.forBelt(Belt.WHITE).first { it.name == "Ki Cho Hyung Il Bu" }.videoUrl
        )
    }

    @Test
    fun quizDrawWeightRisesWithMissesAndResetsAtZero() {
        assertEquals(1, quizDrawWeight(0))   // fresh / just got it right
        assertEquals(4, quizDrawWeight(1))   // missed once -> 4x as likely
        assertEquals(7, quizDrawWeight(2))   // missed twice
        assertEquals(1, quizDrawWeight(-3))  // guards against negatives
    }

    @Test
    fun boStaffBeltAssignmentMatchesCorrectedMapping() {
        fun bo(n: Int) = SyllabusData.byCategory(Category.BO_STAFF).first { it.number == n }
        assertEquals(Belt.BROWN, bo(3).belt)      // 1-5
        assertEquals(Belt.BROWN_TAG, bo(8).belt)  // 6-10
        assertEquals(Belt.RED, bo(13).belt)       // 11-15
        assertEquals(Belt.RED_TAG, bo(18).belt)   // 16-20
        assertEquals(Belt.BLUE, bo(21).belt)      // 21-30
        assertEquals(Belt.BLUE, bo(30).belt)
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
