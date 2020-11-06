package com.justai.jaicf.template.scenario

import com.justai.jaicf.model.scenario.Scenario
import com.justai.jaicf.template.QuestCases
import com.justai.jaicf.template.StatusChange


val questCases =  QuestCases()

object QuestScenario : Scenario() {

    init {

        state("caseStart") {

            action {
                //
                val stateInfo = Controller(context)
                stateInfo.currentSituation = questCases.getSituation()
                reactions.say(stateInfo.currentSituation.text)
            }

            state("yes") {
                activators {
                    regex("да")
                }

                action {
                    val stateInfo = Controller(context)
                    stateInfo.currentConsequence = stateInfo.currentSituation.yesVariant
                    stateInfo.updateScores()
                    reactions.go("/caseStart/handler")

                }
            }

            state("no") {
                activators {
                    regex("да")
                }

                action {
                    val stateInfo = Controller(context)
                    stateInfo.currentConsequence = stateInfo.currentSituation.noVariant
                    stateInfo.updateScores()
                    reactions.go("/caseStart/handler")
                }
            }


            state("handler") {
                action {
                    val stateInfo = Controller(context)
                    val dissidentStatus = getDissidentStatus(stateInfo.dissidentScore, stateInfo.currentConsequence)
                    val sectantStatus = getSectantStatus(stateInfo.dissidentScore, stateInfo.currentConsequence)

                    if (dissidentStatus == StatusChange.TOO_LITTLE) {
                        reactions.go("Хуячим в проигрыш")
                        return@action
                    }
                    if (dissidentStatus == StatusChange.TOO_MUCH) {
                        reactions.go("Тоже хуячим в проигрыш")
                        return@action
                    }

                    if (sectantStatus == StatusChange.TOO_LITTLE) {
                        reactions.go("Хуячим в проигрыш")
                        return@action
                    }
                    if (sectantStatus == StatusChange.TOO_MUCH) {
                        reactions.go("Тоже хуячим в проигрыш")
                        return@action
                    }

                    val replyDissident = when (dissidentStatus) {
                        StatusChange.LESS -> "уровень диссиденства уменьшился"
                        StatusChange.MORE -> "уровень диссиденства увеличился"
                        StatusChange.STILL -> "уровень диссиденства не изменился"
                        else -> null
                    }

                    val replySectant = when (sectantStatus) {
                        StatusChange.LESS -> "уровень фанатизма уменьшился"
                        StatusChange.MORE -> "уровень фанатизма увеличился"
                        StatusChange.STILL -> "уровень фанатизма не изменился"
                        else -> null
                    }

                    reactions.say("$replyDissident, ${stateInfo.dissidentScore}")
                    reactions.say("$replySectant, ${stateInfo.sectantScore}")

                    reactions.go("/caseStart")
                }
            }


            fallback {
                reactions.say("Братец, не напрягай деда, скажи \"да\" или \"нет\"")
            }



            state ("стейт для проигрыша 1") {

            }


            state ("стейт для проигрыша 2") {

            }


            state ("стейт для проигрыша 3") {

            }


            state ("стейт для проигрыша 4") {

            }
        }

    }
}