package com.justai.jaicf.template.scenario

import com.justai.jaicf.channel.aimybox.aimybox
import com.justai.jaicf.model.scenario.Scenario
import com.justai.jaicf.template.StatusChange
import com.justai.jaicf.template.questCases


object QuestScenario : Scenario() {

    init {

        state("caseStart") {

            action {
                //
                val stateInfo = Controller(context)

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

                    if (stateInfo.currentConsequence.transition == null) {
                        val s = questCases.getRandomSituation()
                        if (s != null)
                            stateInfo.currentSituation = s
                        else {
                            reactions.say("Вы - мастер выживания и морального спокойствия! " +
                                    "Просто представьте, сколько денег вы сэкономили на психотерапевтах и психиатрах! " +
                                    "Поздравляю с победой, многоуважаемый!")
                            reactions.aimybox?.endConversation()
                            reactions.go("/")
                        }
                    }

                    else
                        stateInfo.currentSituation = questCases.getNextSituation(stateInfo)
                }
            }


            fallback {
                reactions.say("Братец, не напрягай деда, скажи \"да\" или \"нет\"")
            }



            state ("TOO_MUCH_DISSIDENT") {
                action {
                    reactions.say("Ваш уровень диссиденства стал настолько высок, " +
                            "что Билл Гейтс не смог это не заметить и вылетел вас чипировать. Чик-чик, голубчик.")
                    reactions.aimybox?.endConversation()
                    reactions.go("/")
                }
            }


            state ("TOO_LITTLE_DISSIDENT") {
                action {
                    reactions.say("Кажется, вы превратились в фанатичного сектанта!" +
                            " Осторожнее с антисептиком - ваши руки могут раствориться от такого количества спирта.")
                    reactions.aimybox?.endConversation()
                    reactions.go("/")
                }
            }


            state ("TOO_MUCH_SECTANT") {
                action {
                    reactions.say("У-у-у, уважаемый, полегче! " +
                            "Если так пойдет дальше, то ваша психика не выдержит и ... Но давайте не будем о грустном.")
                    reactions.aimybox?.endConversation()
                    reactions.go("/")
                }
            }


            state ("TOO_LITTLE_SECTANT") {
                action {
                    reactions.say("Кажется, вы всё-таки оказались диким диссидентом... " +
                            "Остоновитесь, подумойте, а то вышки 5G на ваш порадиоактивят и всё!")
                    reactions.aimybox?.endConversation()
                    reactions.go("/")
                }
            }
        }

    }
}