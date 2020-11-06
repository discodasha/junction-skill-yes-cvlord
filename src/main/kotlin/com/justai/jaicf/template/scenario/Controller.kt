package com.justai.jaicf.template.scenario

import com.justai.jaicf.context.BotContext
import com.justai.jaicf.template.Consequence
import com.justai.jaicf.template.Situation
import com.justai.jaicf.template.StatusChange

class Controller(context: BotContext) {
    var currentSituation: Situation by context.session
    var currentConsequence: Consequence by context.session
    var dissidentScore: Int by context.session
    var sectantScore: Int by context.session

    fun updateScores() {
        dissidentScore += currentConsequence.scoreDissident
        sectantScore += currentConsequence.scoreSectant
    }
}

fun getDissidentStatus(currentScore: Int, consequence: Consequence): StatusChange {
    if (currentScore >= 100)
        return StatusChange.TOO_MUCH
    if (currentScore <= 0)
        return StatusChange.TOO_LITTLE
    if (consequence.scoreDissident == 0)
        return StatusChange.STILL
    if (consequence.scoreDissident < 0)
        return StatusChange.LESS
    return StatusChange.MORE
}

fun getSectantStatus(gameScore: Int, consequence: Consequence): StatusChange {
    if (gameScore >= 100)
        return StatusChange.TOO_MUCH
    if (gameScore <= 0)
        return StatusChange.TOO_LITTLE
    if (consequence.scoreSectant == 0)
        return StatusChange.STILL
    if (consequence.scoreSectant < 0)
        return StatusChange.LESS
    return StatusChange.MORE
}