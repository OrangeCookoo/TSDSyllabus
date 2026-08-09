package uk.co.btsda.syllabus.data

import java.net.URLEncoder

/**
 * A Tang Soo Do form (hyung), belonging to an empty-hand belt rank.
 */
data class Form(val belt: Belt, val name: String, val description: String? = null) {

    val videoQuery: String get() = "Bristol Tang Soo Do Academy $name"

    /**
     * A YouTube link for this form: the exact BTSDA video when its id is known
     * (see [FormVideoLinks]), otherwise an in-channel search for the form name.
     */
    val videoUrl: String
        get() {
            FormVideoLinks.ids[name]?.let { return "https://www.youtube.com/watch?v=$it" }
            val encoded = URLEncoder.encode(videoQuery, "UTF-8")
            return "https://www.youtube.com/results?search_query=$encoded"
        }
}

/** Known BTSDA form video ids, keyed by [Form.name]. Add entries as confirmed. */
object FormVideoLinks {
    val ids: Map<String, String> = mapOf(
        // "Pyung Ahn O Dan" to "...",
    )
}

/** The forms (hyungs) required at each belt, in syllabus order. */
object Forms {
    val all: List<Form> = listOf(
        Form(Belt.WHITE, "Ki Cho Hyung Il Bu", "Basic form no.1"),
        Form(Belt.WHITE, "Ki Cho Hyung E Bu", "Basic form no.2"),
        Form(Belt.ORANGE, "Ki Cho Hyung Sam Bu", "Basic form no.3"),
        Form(Belt.ORANGE, "Pyung Ahn Cho Dan"),
        Form(Belt.GREEN, "Pyung Ahn E Dan"),
        Form(Belt.GREEN, "Pyung Ahn Sam Dan"),
        Form(Belt.BROWN, "Pyung Ahn Sah Dan"),
        Form(Belt.BROWN, "Pyung Ahn O Dan"),
        Form(Belt.RED, "Bassai"),
        Form(Belt.RED, "Naihanchi Cho Dan"),
        Form(Belt.BLUE, "Sipsoo"),
    )

    fun forBelt(belt: Belt): List<Form> = all.filter { it.belt == belt }

    /** Belts that have at least one form (empty-hand ranks). */
    val beltsWithForms: List<Belt> = all.map { it.belt }.distinct()
}
