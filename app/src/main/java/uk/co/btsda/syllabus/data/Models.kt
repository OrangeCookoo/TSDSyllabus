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
     * The search query used to locate this technique's demonstration video:
     * the academy name plus the exact block title, so the correct BTSDA upload
     * is the top result.
     */
    val videoQuery: String get() = "Bristol Tang Soo Do Academy $videoTitle"

    /** True when we have the exact BTSDA video and can open it directly. */
    val hasDirectVideo: Boolean get() = VideoLinks.ids.containsKey(videoTitle)

    /**
     * A YouTube deep-link for this technique's demonstration video.
     *
     * When the exact BTSDA video id is known ([VideoLinks]) it links straight
     * to that video. Otherwise it falls back to the canonical
     * `results?search_query=` endpoint, which the YouTube app and browsers open
     * directly to the results list (the correct BTSDA upload is the top hit).
     */
    val videoUrl: String
        get() {
            VideoLinks.ids[videoTitle]?.let { videoId ->
                return VideoLinks.watchUrl(videoId, VideoLinks.startSeconds[id])
            }
            val encoded = URLEncoder.encode(videoQuery, "UTF-8")
            return "https://www.youtube.com/results?search_query=$encoded"
        }
}

/**
 * Known BTSDA demonstration videos, keyed by their exact block title
 * ([Technique.videoTitle]). Techniques whose block appears here open the
 * video directly; the rest fall back to a channel search. Add entries as the
 * remaining videos are confirmed.
 */
object VideoLinks {
    val ids: Map<String, String> = mapOf(
        // Bo staff series.
        "Bo Staff 1 Steps 1-5" to "nZnX0kgs_Sw",
        "Bo Staff 1 Steps 6-10" to "qRzaGcJJ2E0",
        "Bo Staff 1 Steps 11-15" to "gjniNGbuPh4",
        "Bo Staff 1 Steps 16-20" to "-hYMwVNhH8o",
        "Bo Staff 1 Steps 21-25" to "dom7Iq__hqE",
        "Bo Staff 1 Steps 26-30" to "JLr2zCxpXME",
        // Empty-hand series (hands, feet, self defense share these videos).
        "Il Soo Sik Dae Ryun (1 Step Sparring) 1-5" to "I4dj-SSDh3Q",
        "Il Soo Sik Dae Ryun (1 Step Sparring) 6-10" to "oB6q-AxH0cs",
        "Il Soo Sik Dae Ryun (1 Step Sparring) 11-15" to "flCo2tl5_3Y",
        "Il Soo Sik Dae Ryun (1 Step Sparring) 16-20" to "wotAiTXp9KU",
        "Il Soo Sik Dae Ryun (1 Step Sparring) 21-25" to "O_qJ9UYpzKU",
        "Il Soo Sik Dae Ryun (1 Step Sparring) 26-30" to "dcsgmRmmAss",
    )

    /**
     * Optional start time (in whole seconds) within a block video for an
     * individual technique, keyed by [Technique.id] (e.g. "BO_STAFF_17").
     * Since one video covers five techniques, a timestamp jumps straight to
     * the relevant technique. Add entries over time; anything not listed just
     * opens the video at the start.
     *
     * Example: technique starting at 1:23 -> "BO_STAFF_17" to 83.
     */
    val startSeconds: Map<String, Int> = mapOf(
        // "BO_STAFF_16" to 5,
        // "BO_STAFF_17" to 41,
    )

    /** Builds a watch URL, adding a start time (&t=NNs) when one is provided. */
    fun watchUrl(videoId: String, startSeconds: Int? = null): String =
        "https://www.youtube.com/watch?v=$videoId" + (startSeconds?.let { "&t=${it}s" } ?: "")
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
