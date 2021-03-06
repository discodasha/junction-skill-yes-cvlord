package com.justai.jaicf.template

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.justai.jaicf.template.scenario.MainScenario

private val GAME_DATA_FILE = MainScenario::class.java.getResourceAsStream("/game-data.csv")
private const val SCORE_DELIMITER = "/"

private const val ID_COL = 0
private const val TEXT_COL = 1
private const val YES_SCORE_COL = 2
private const val YES_TEXT_COL = 3
private const val YES_TRANSITION_COL = 4
private const val NO_SCORE_COL = 5
private const val NO_TEXT_COL = 6
private const val NO_TRANSITION_COL = 7
private const val INDEPENDENT = 8

private fun parseScore(scoreData: String): List<Int> {
    return scoreData.split(SCORE_DELIMITER).map { it.toInt() }
}

private fun buildConsequence(scoreData: String, text: String, transition: String): Consequence {
    val score = parseScore(scoreData)
    return Consequence(
        scoreDissident = score[0],
        scoreSectant = score[1],
        text = text,
        transition = transition.toIntOrNull()
    )
}

private fun parseRow(row: List<String>): Situation {
    return Situation(
        id = row[ID_COL].toInt(),
        independent = row[INDEPENDENT].toInt(),
        text = row[TEXT_COL],
        tts = "",
        yesVariant = buildConsequence(row[YES_SCORE_COL], row[YES_TEXT_COL], row[YES_TRANSITION_COL]),
        noVariant = buildConsequence(row[NO_SCORE_COL], row[NO_TEXT_COL], row[NO_TRANSITION_COL])
    )
}

fun parseGameData(): List<Situation> {
    var situations: List<Situation> = ArrayList();
    csvReader().open(GAME_DATA_FILE) {
        situations = readAllAsSequence().map { parseRow(it) }.toList()
    }
    return situations
}
