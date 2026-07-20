package uk.co.btsda.syllabus

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppUiTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun launchesAndShowsFirstBeltAndTechniques() {
        composeRule.waitForIdle()

        // Category tab renders.
        composeRule.onAllNodesWithText("Hands", substring = true)[0].assertIsDisplayed()

        // White belt header for the first category is visible.
        composeRule.onNodeWithText("White Belt").assertIsDisplayed()

        // The author's pre-populated note for Hands #1 is shown.
        composeRule.onNodeWithText("RH block elbow").assertIsDisplayed()
    }

    @Test
    fun everyTechniqueOffersAWatchVideoAction() {
        composeRule.waitForIdle()
        composeRule.onAllNodesWithText("Watch video")[0].assertIsDisplayed()
    }
}
