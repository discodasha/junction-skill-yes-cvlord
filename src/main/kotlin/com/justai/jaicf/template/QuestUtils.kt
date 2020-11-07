package com.justai.jaicf.template

import com.justai.jaicf.template.scenario.Controller

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

val cons1 = Consequence(10, 20, "Кейс1", null)
val cons2 = Consequence(-30, 0, "kdfjghfkdhg", 1)

enum class StatusChange {
    MORE, LESS, TOO_LITTLE, TOO_MUCH, STILL
}

class QuestCases() {
    private val usedCases = listOf<Int>()
    private val dateset = listOf(Situation(0,"1111", "1111", cons1, cons2)) // TODO - init!

    fun getRandomSituation(): Situation {
        return dateset
            .filter { usedCases.indexOf(it.id) == -1 }
            .random()
    }

    fun getNextSituation(currentStateInfo: Controller): Situation {
        return dateset.first { it.id == currentStateInfo.currentConsequence.transition }
    }

}

val questCases =  QuestCases()