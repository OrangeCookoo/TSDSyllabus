package uk.co.btsda.syllabus.data

import uk.co.btsda.syllabus.data.Belt.BLUE
import uk.co.btsda.syllabus.data.Belt.BROWN
import uk.co.btsda.syllabus.data.Belt.GREEN
import uk.co.btsda.syllabus.data.Belt.ORANGE
import uk.co.btsda.syllabus.data.Belt.RED
import uk.co.btsda.syllabus.data.Belt.RED_TAG
import uk.co.btsda.syllabus.data.Belt.WHITE
import uk.co.btsda.syllabus.data.Category.BO_STAFF
import uk.co.btsda.syllabus.data.Category.FEET
import uk.co.btsda.syllabus.data.Category.HANDS
import uk.co.btsda.syllabus.data.Category.SELF_DEFENSE

/**
 * The complete, pre-populated Tang Soo Do one-step syllabus.
 * Notes are the author's own short-hand and can be overridden per technique
 * by the user at runtime (see [NotesRepository]).
 */
object SyllabusData {

    val techniques: List<Technique> = buildList {
        // ---------------------------------------------------------------- HANDS
        addAll(
            listOf(
                Technique(HANDS, WHITE, 1, "RH block elbow"),
                Technique(HANDS, WHITE, 2, "Double elbow"),
                Technique(HANDS, WHITE, 3, "Chop"),
                Technique(HANDS, WHITE, 4, "Block middle punch"),
                Technique(HANDS, WHITE, 5, "Block + face punch"),
                Technique(HANDS, ORANGE, 6, "Block triple punch"),
                Technique(HANDS, ORANGE, 7, "Block chop"),
                Technique(HANDS, ORANGE, 8, "Elbow smash"),
                Technique(HANDS, ORANGE, 9, "Nut smash"),
                Technique(HANDS, ORANGE, 10, "Tricky 10"),
                Technique(HANDS, GREEN, 11, "Block knee to stomach, hook to face"),
                Technique(HANDS, GREEN, 12, "Block, ear box, knee to face"),
                Technique(HANDS, GREEN, 13, "Double chop (spinning)"),
                Technique(HANDS, GREEN, 14, "Block kick to back of knee, TD punch"),
                Technique(HANDS, GREEN, 15, "Block clothesline plus lift leg, TD punch"),
                Technique(HANDS, BROWN, 16, "Block step double elbow stomach"),
                Technique(HANDS, BROWN, 17, "Block grab eye poke back fist"),
                Technique(HANDS, BROWN, 18, "Block palm to chin, twist TD punch"),
                Technique(HANDS, BROWN, 19, "Reverse sweep thing, punch"),
                Technique(HANDS, BROWN, 20, "Weird snake arm thing"),
                Technique(HANDS, RED, 21, "Block grab step elbow down to spine"),
                Technique(HANDS, RED, 22, "Block spin under arm, knee, kick, punch"),
                Technique(HANDS, RED, 23, "Scissor arms TD"),
                Technique(HANDS, RED, 24, "Block + uppercut groin, chop"),
                Technique(HANDS, RED, 25, "Block + elbow choke, knee stomach"),
                Technique(HANDS, BLUE, 26, "Like the knife defense – over shoulder"),
                Technique(HANDS, BLUE, 27, "In to out – single hand to neck + knee"),
                Technique(HANDS, BLUE, 28, "Marry me"),
                Technique(HANDS, BLUE, 29, "Weird step in, high block groin then flip"),
                Technique(HANDS, BLUE, 30, "Kneck squeeze, break back over knee"),
            )
        )

        // ----------------------------------------------------------------- FEET
        addAll(
            listOf(
                Technique(FEET, WHITE, 1, "Front"),
                Technique(FEET, WHITE, 2, "Side"),
                Technique(FEET, WHITE, 3, "Round"),
                Technique(FEET, WHITE, 4, "Back"),
                Technique(FEET, WHITE, 5, "Step side"),
                Technique(FEET, ORANGE, 6, "Duck step RH kick"),
                Technique(FEET, ORANGE, 7, "Push kick"),
                Technique(FEET, ORANGE, 8, "Block grab side kick"),
                Technique(FEET, ORANGE, 9, "Block grab RH kick"),
                Technique(FEET, ORANGE, 10, "Block grab hook"),
                Technique(FEET, GREEN, 11, "Block punch with out-to-in, left wheel"),
                Technique(FEET, GREEN, 12, "Block with out-to-in side"),
                Technique(FEET, GREEN, 13, "Front round"),
                Technique(FEET, GREEN, 14, "Step left in-out, right RH kick"),
                Technique(FEET, GREEN, 15, "Front in-out, block hook RH"),
                Technique(FEET, BROWN, 16, "X grab pull, hook face, knee"),
                Technique(FEET, BROWN, 17, "Pat down, spinning hook"),
                Technique(FEET, BROWN, 18, "Pat down, spinning hook, round"),
                Technique(FEET, BROWN, 19, "Slide catch. Trip and face stamp"),
                Technique(FEET, BROWN, 20, "Dai apchagi"),
                Technique(FEET, RED, 21, "Pat down front, spinning in-out"),
                Technique(FEET, RED, 22, "Block RH, side back kick"),
                Technique(FEET, RED, 23, "Pat 360 RH"),
                Technique(FEET, RED, 24, "Pat jump back"),
                Technique(FEET, RED, 25, "Pat 360 out-in"),
                Technique(FEET, BLUE, 26, "Left + double block, round spin hook"),
                Technique(FEET, BLUE, 27, "Low front kick block, front jump round"),
                Technique(FEET, BLUE, 28, "Sweep"),
                Technique(FEET, BLUE, 29, "Block as step back, jump front round"),
                Technique(FEET, BLUE, 30, "Block as step back, spin in to out"),
            )
        )

        // --------------------------------------------------------- SELF DEFENSE
        addAll(
            listOf(
                Technique(SELF_DEFENSE, WHITE, 1, "Punch out chop"),
                Technique(SELF_DEFENSE, WHITE, 2, "Pull out chop"),
                Technique(SELF_DEFENSE, WHITE, 3, "Block + palm strike"),
                Technique(SELF_DEFENSE, WHITE, 4, "Wrist lock into crook of neck, twist down"),
                Technique(SELF_DEFENSE, WHITE, 5, "Kneck grab push away"),
                Technique(SELF_DEFENSE, ORANGE, 6, "RH shoulder grab – spin arm bar knock"),
                Technique(SELF_DEFENSE, ORANGE, 7, "Collar – face, wrist lock pull down"),
                Technique(SELF_DEFENSE, ORANGE, 8, "Policemen punch out, reverse push elbow"),
                Technique(SELF_DEFENSE, ORANGE, 9, "Neck + arm. Punch out elbow, nut rip"),
                Technique(SELF_DEFENSE, ORANGE, 10, "Headlock. Ball chop"),
                Technique(SELF_DEFENSE, GREEN, 11, "Wrist lock pull down"),
                Technique(SELF_DEFENSE, GREEN, 12, "Tell the time. Kick face"),
                Technique(SELF_DEFENSE, GREEN, 13, "Arm bar – elbow face as step through"),
                Technique(SELF_DEFENSE, GREEN, 14, "Shoot wrist up grab, side kick ribs"),
                Technique(SELF_DEFENSE, GREEN, 15, "Buddy. Punch out, grab head and knee"),
                Technique(SELF_DEFENSE, BROWN, 16, "Grab fist left, elbow right hook"),
                Technique(SELF_DEFENSE, BROWN, 17, "Double neck grab release, ear, head, knee"),
                Technique(SELF_DEFENSE, BROWN, 18, "Lapels – weave twist ridgehand"),
                Technique(SELF_DEFENSE, BROWN, 19, "Under arms – headbutt, leg grab stamp"),
                Technique(SELF_DEFENSE, BROWN, 20, "Over arms – pushout, elbow x2, jmp back"),
                Technique(SELF_DEFENSE, RED, 21, "Chop wrist pull down, twist spin thing"),
                Technique(SELF_DEFENSE, RED, 22, "RH block duck under arm, pull up"),
                Technique(SELF_DEFENSE, RED, 23, "LH block step across, brk elbow, elbow"),
                Technique(SELF_DEFENSE, RED, 24, "Reverse attack, 2 hand block arm bar"),
                Technique(SELF_DEFENSE, RED, 25, "Knife to neck – 2 options"),
                Technique(SELF_DEFENSE, BLUE, 26, "Knife to groin. X block, horse stance"),
                Technique(SELF_DEFENSE, BLUE, 27, "Punch to kidney – scissor knife towards"),
                Technique(SELF_DEFENSE, BLUE, 28, "Knife in back, spin and grab"),
                Technique(SELF_DEFENSE, BLUE, 29, "Tang bong – rear naked choke"),
                Technique(SELF_DEFENSE, BLUE, 30, "Tan bong – disarm, stomach + knee"),
            )
        )

        // ------------------------------------------------------------- BO STAFF
        addAll(
            listOf(
                Technique(BO_STAFF, BROWN, 1, "Overhead block, front kick"),
                Technique(BO_STAFF, BROWN, 2, "OH side"),
                Technique(BO_STAFF, BROWN, 3, "OH round"),
                Technique(BO_STAFF, BROWN, 4, "OH push + spear"),
                Technique(BO_STAFF, BROWN, 5, "OH Bo one side then other + side kick"),
                Technique(BO_STAFF, RED, 6, "Block clear, spear to neck"),
                Technique(BO_STAFF, RED, 7, "Block clear, single strike to head"),
                Technique(BO_STAFF, RED, 8, "Block fancy thing to ankle"),
                Technique(BO_STAFF, RED, 9, "Block clear, spin and spear, horse stance"),
                Technique(BO_STAFF, RED, 10, "Block clear, double head strike"),
                Technique(BO_STAFF, RED_TAG, 11, "Block slide, head strike"),
                Technique(BO_STAFF, RED_TAG, 12, "Block slide, double head strike"),
                Technique(BO_STAFF, RED_TAG, 13, "Block ball strike, head strike"),
                Technique(BO_STAFF, RED_TAG, 14, "Double block, spinning hook kick"),
                Technique(BO_STAFF, RED_TAG, 15, "As 14 + round house"),
                Technique(BO_STAFF, BLUE, 16, "Low block slide, jump thing, head strike", groupLabel = "Baseball bat"),
                Technique(BO_STAFF, BLUE, 17, "Low block, let go side strike to head"),
                Technique(BO_STAFF, BLUE, 18, "Low block, let go overhead strike"),
                Technique(BO_STAFF, BLUE, 19, "Double block, front round"),
                Technique(BO_STAFF, BLUE, 20, "Double block, skip side"),
                Technique(BO_STAFF, BLUE, 21, "Front kick, head strike", groupLabel = "Triple strike"),
                Technique(BO_STAFF, BLUE, 22, "Side + spear"),
                Technique(BO_STAFF, BLUE, 23, "RH + groin"),
                Technique(BO_STAFF, BLUE, 24, "Push + step through spear"),
                Technique(BO_STAFF, BLUE, 25, "Dodge RH kick + side kick"),
                Technique(BO_STAFF, BLUE, 26, "Head strike", groupLabel = "Crossed bo staff – block then disarm"),
                Technique(BO_STAFF, BLUE, 27, "Step through take down, spear"),
                Technique(BO_STAFF, BLUE, 28, "Jump choke"),
                Technique(BO_STAFF, BLUE, 29, "Triple level strike"),
                Technique(BO_STAFF, BLUE, 30, "Spinning thing"),
            )
        )
    }

    /** Techniques for one category, in syllabus order. */
    fun byCategory(category: Category): List<Technique> =
        techniques.filter { it.category == category }

    /** Belts present in a category, in rank order. */
    fun beltsIn(category: Category): List<Belt> =
        byCategory(category).map { it.belt }.distinct()

    fun default(id: String): String? = techniques.firstOrNull { it.id == id }?.defaultNote
}
