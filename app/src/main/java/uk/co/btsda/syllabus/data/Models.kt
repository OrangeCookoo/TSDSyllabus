package uk.co.btsda.syllabus.data

import androidx.compose.ui.graphics.Color
import java.net.URLEncoder

/** The four sections of the Tang Soo Do one-step syllabus. */
enum class Category(val display: String, val emoji: String) {
    HANDS("Hands", "👊"),
    FEET("Feet", "🦶"),
    SELF_DEFENSE("Self Defense", "🛡️"),
    BO_STAFF("Bo Staff", "🥋")
}

/** Belt ranks. RED_TAG is only used by the Bo staff syllabus. */
enum class Belt(val display: String, val primary: Color, val onPrimary: Color, val accent: Color) {
    WHITE("White", Color(0xFFECEFF1), Color(0xFF263238), Color(0xFFB0BEC5)),
    ORANGE("Orange", Color(0xFFFB8C00), Color(0xFF3E2000), Color(0xFFFFB74D)),
    GREEN("Green", Color(0xFF2E9E4F), Color(0xFF06210F), Color(0xFF69D98A)),
    BROWN("Brown", Color(0xFF795548), Color(0xFFFFF3E0), Color(0xFFA98274)),
    RED("Red", Color(0xFFE53935), Color(0xFFFFEBEE), Color(0xFFFF867C)),
    RED_TAG("Red Tag", Color(0xFFC62828), Color(0xFFFFEBEE), Color(0xFFFF5F52)),
    BLUE("Blue", Color(0xFF1E88E5), Color(0xFFE3F2FD), Color(0xFF6AB7FF))
}

/**
 * A single syllabus technique.
 *
 * @param number the position within its [category] (1..30)
 * @param defaultNote the pre-populated short note (the author's own)
 * @param groupLabel optional header shown above this card to mark a sub-group
 */
data class Technique(
    val category: Category,
    val belt: Belt,
    val number: Int,
    val defaultNote: String,
    val groupLabel: String? = null
) {
    /** Stable identifier used as the persistence key for user overrides. */
    val id: String get() = "${category.name}_$number"

    /** First technique number of this technique's block-of-five (1, 6, 11 ...). */
    val blockStart: Int get() = ((number - 1) / 5) * 5 + 1

    /** Last technique number of this technique's block-of-five (5, 10, 15 ...). */
    val blockEnd: Int get() = blockStart + 4

    /**
     * The exact title of the BTSDA demonstration video that covers this
     * technique. Videos are published in blocks of five. The empty-hand
     * syllabus (hands, feet, self defense) shares the "Il Soo Sik Dae Ryun
     * (1 Step Sparring)" series; the bo staff syllabus has its own series.
     */
    val videoTitle: String
        get() = when (category) {
            Category.BO_STAFF -> "Bo Staff 1 Steps $blockStart-$blockEnd"
            else -> "Il Soo Sik Dae Ryun (1 Step Sparring) $blockStart-$blockEnd"
        }

    /**
     * A YouTube deep-link that searches the Bristol Tang Soo Do Academy channel
     * for [videoTitle], landing the user on the correct demonstration video.
     */
    val videoUrl: String
        get() {
            val encoded = URLEncoder.encode(videoTitle, "UTF-8")
            return "https://www.youtube.com/@bristoltangsoodoacademy/search?query=$encoded"
        }
}

/** Optional descriptive subtitle for a (category, belt) section header. */
fun beltSubtitle(category: Category, belt: Belt): String? = when (category) {
    Category.FEET -> if (belt == Belt.BROWN) "Opposite stance – front kick attack" else null
    Category.SELF_DEFENSE -> when (belt) {
        Belt.WHITE -> "Front single grabs"
        Belt.ORANGE -> "Back"
        Belt.GREEN -> "Side · high, low, middle, low, buddy"
        Belt.BROWN -> "Double grab"
        Belt.RED -> "Knife"
        else -> null
    }
    Category.BO_STAFF -> when (belt) {
        Belt.BROWN -> "Overhead block"
        Belt.RED -> "Bo's crossed, touching at top"
        Belt.RED_TAG -> "Spear attack · block in back stance"
        Belt.BLUE -> "Baseball bat · triple strike · crossed-bo disarm"
        else -> null
    }
    else -> null
}
