package com.justai.jaicf.template

data class Situation (
    val id: Int,
    val text: String,
    val tts: String,
    val yesVariant: Consequence,
    val noVariant: Consequence
)

data class Consequence (
    val scoreDissident: Int,
    val scoreSectant: Int,
    val text: String,
    val transition: Int?
)

val cons1 = Consequence(10, 20, "ghdbtn", null)
val cons2 = Consequence(-30, 0, "kdfjghfkdhg", 1)
val sampleDateset = listOf(Situation(0,"1111", "1111", cons1, cons2)) // TODO - init!

enum class StatusChange {
    MORE, LESS, TOO_LITTLE, TOO_MUCH, STILL
}

class QuestCases() {
    private val usedCases = listOf<Int>()

    fun getSituation(): Situation {
        return sampleDateset
            .filter { usedCases.indexOf(it.id) == -1 }
            .random()
    }
}