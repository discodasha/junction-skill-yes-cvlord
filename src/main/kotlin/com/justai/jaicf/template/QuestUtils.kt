package com.justai.jaicf.template

import com.justai.jaicf.template.scenario.Controller

data class Situation (
    val id: Int,
    val independent: Int,
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

val cons1 = Consequence(10, 90, "Реакция 1", null)
val cons2 = Consequence(-30, 0, "Реакция 2", 1)

enum class StatusChange {
    MORE, LESS, TOO_LITTLE, TOO_MUCH, STILL
}

class QuestCases() {
    private val usedCases = mutableListOf<Int>()
    val dateset = parseGameData()

    fun getRandomSituation(): Situation? {
        if (usedCases.size == dateset.size) {
            return null
        }

        val situation = dateset
            .filter { usedCases.indexOf(it.id) == -1 }
            .filter { it.independent == 1 }
            .random()
        usedCases.add(situation.id)
        return situation
    }

    fun getNextSituation(currentStateInfo: Controller): Situation {
        usedCases.add(currentStateInfo.currentConsequence.transition!!)
        return dateset.first { it.id == currentStateInfo.currentConsequence.transition }
    }

}

