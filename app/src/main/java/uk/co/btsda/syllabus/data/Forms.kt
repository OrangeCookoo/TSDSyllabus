package uk.co.btsda.syllabus.data

import java.net.URLEncoder

/** Whether a form is an empty-hand hyung or a bo staff form. */
enum class FormType { EMPTY_HAND, BO_STAFF }

/**
 * A Tang Soo Do form (hyung), belonging to a belt rank.
 */
data class Form(
    val belt: Belt,
    val name: String,
    val description: String? = null,
    val type: FormType = FormType.EMPTY_HAND,
) {

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

/** Known BTSDA form video ids, keyed by [Form.name]. */
object FormVideoLinks {
    val ids: Map<String, String> = mapOf(
        // Empty-hand hyung.
        "Ki Cho Hyung Il Bu" to "xA7850ho2Ms",
        "Ki Cho Hyung E Bu" to "eRU1XWLhWw8",
        "Ki Cho Hyung Sam Bu" to "fGZHaDNMa2Y",
        "Pyung Ahn Cho Dan" to "g7jCHOS8Mik",
        "Pyung Ahn E Dan" to "Dp2DotewXDM",
        "Pyung Ahn Sam Dan" to "-R8qsIB85Hk",
        "Pyung Ahn Sah Dan" to "s8Aql1HXlBQ",
        "Pyung Ahn O Dan" to "eUSetwIN_1Q",
        "Bassai" to "KaxkLdtlT6g",
        "Naihanchi Cho Dan" to "ibhvaw9nttM",
        "Sipsoo" to "XmgnZGCY3xI",
        // Bo staff forms.
        "Bong Hyung Il Bu" to "QamIx3uVjgU",
        "Bong Hyung E Bu" to "Ad__yy0pAIU",
        "Bong Hyung Sam Bu" to "qbyg6v2dj5s",
    )
}

/** The forms (hyungs) required at each belt, in syllabus order. */
object Forms {
    val all: List<Form> = listOf(
        // Empty-hand hyung.
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
        // Bo staff forms.
        Form(Belt.BROWN, "Bong Hyung Il Bu", "Staff form no.1", FormType.BO_STAFF),
        Form(Belt.RED, "Bong Hyung E Bu", "Staff form no.2", FormType.BO_STAFF),
        Form(Belt.BLUE, "Bong Hyung Sam Bu", "Staff form no.3", FormType.BO_STAFF),
    )

    /** All forms for a belt (both empty-hand and bo staff). */
    fun forBelt(belt: Belt): List<Form> = all.filter { it.belt == belt }

    /** Forms for a belt of a given type. */
    fun forBelt(belt: Belt, type: FormType): List<Form> =
        all.filter { it.belt == belt && it.type == type }

    /** Belts that have at least one form. */
    val beltsWithForms: List<Belt> = all.map { it.belt }.distinct()
}
