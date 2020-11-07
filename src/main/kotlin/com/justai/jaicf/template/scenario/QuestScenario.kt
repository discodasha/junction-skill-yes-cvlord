package com.justai.jaicf.template.scenario

import com.justai.jaicf.channel.aimybox.aimybox
import com.justai.jaicf.model.scenario.Scenario
import com.justai.jaicf.template.StatusChange
import com.justai.jaicf.template.scenario.MainScenario.questCases

object QuestScenario : Scenario() {

    init {

        state("cases") {

            action {
                //
                val stateInfo = Controller(context)
                reactions.say("")
                reactions.say(stateInfo.currentSituation.text)
            }

            state("yes") {
                activators {
                    regex("да")
                    intent("Yes")
                }

                action {
                    val stateInfo = Controller(context)
                    stateInfo.currentConsequence = stateInfo.currentSituation.yesVariant
                    stateInfo.updateScores()
                    reactions.go("/cases/handler")

                }
            }

            state("no") {
                activators {
                    regex("нет")
                    intent("No")
                }

                action {
                    val stateInfo = Controller(context)
                    stateInfo.currentConsequence = stateInfo.currentSituation.noVariant
                    stateInfo.updateScores()
                    reactions.go("/cases/handler")
                }
            }


            state("handler") {
                action {
                    val stateInfo = Controller(context)
                    val dissidentStatus = getDissidentStatus(stateInfo.dissidentScore, stateInfo.currentConsequence)
                    val sectantStatus = getSectantStatus(stateInfo.sectantScore, stateInfo.currentConsequence)

                    if (dissidentStatus == StatusChange.TOO_LITTLE) {
                        reactions.go("/cases/TOO_LITTLE_DISSIDENT")
                        return@action
                    }
                    if (dissidentStatus == StatusChange.TOO_MUCH) {
                        reactions.go("/cases/TOO_MUCH_DISSIDENT")
                        return@action
                    }

                    if (sectantStatus == StatusChange.TOO_LITTLE) {
                        reactions.go("/cases/TOO_LITTLE_SECTANT")
                        return@action
                    }
                    if (sectantStatus == StatusChange.TOO_MUCH) {
                        reactions.go("/cases/TOO_MUCH_SECTANT")
                        return@action
                    }

                    val replyDissident = when (dissidentStatus) {
                        StatusChange.LESS -> "уровень диссиденства уменьшился до"
                        StatusChange.MORE -> "уровень диссиденства увеличился до"
                        StatusChange.STILL -> "уровень диссиденства не изменился и остался"
                        else -> null
                    }

                    val replySectant = when (sectantStatus) {
                        StatusChange.LESS -> "уровень фанатизма уменьшился до"
                        StatusChange.MORE -> "уровень фанатизма увеличился до"
                        StatusChange.STILL -> "уровень фанатизма не изменился и остался"
                        else -> null
                    }

                    reactions.say(stateInfo.currentConsequence.text)
                    reactions.say("Ваш $replyDissident ${stateInfo.dissidentScore}, " +
                            "$replySectant ${stateInfo.sectantScore}.")

                    if (stateInfo.currentConsequence.transition == null) {
                        val s = questCases.getRandomSituation()
                        if (s != null) {
                            stateInfo.currentSituation = s
                        }
                        else {
                            reactions.say("Вы - мастер выживания и морального спокойствия! " +
                                    "Просто представьте, сколько денег вы сэкономили на психотерапевтах и психиатрах! " +
                                    "Поздравляю с победой, многоуважаемый!")
                            reactions.say("Вы на ${stateInfo.dissidentScore}% диссидент.")
                            reactions.aimybox?.endConversation()
                            reactions.go("/end")
                            return@action
                        }
                    }
                    else {
                        stateInfo.currentSituation = questCases.getNextSituation(stateInfo)
                    }

                    reactions.go("/cases")
                }
            }


            fallback {
                reactions.say("Братец, не напрягай деда, скажи \"да\" или \"нет\".")
            }



            state ("TOO_MUCH_DISSIDENT") {
                action {
                    reactions.say("Ваш уровень диссиденства стал настолько высок, " +
                            "что Билл Гейтс не смог это не заметить и вылетел вас чипировать. Чик-чик, голубчик.")
                    reactions.aimybox?.endConversation()
                    reactions.go("/end")
                }
            }


            state ("TOO_LITTLE_DISSIDENT") {
                action {
                    reactions.say("Кажется, вы превратились в фанатичного сектанта!" +
                            " Осторожнее с антисептиком - ваши руки могут раствориться от такого количества спирта.")
                    reactions.aimybox?.endConversation()
                    reactions.go("/end")
                }
            }


            state ("TOO_MUCH_SECTANT") {
                action {
                    reactions.say("У-у-у, уважаемый, полегче! " +
                            "Если так пойдет дальше, то ваша психика не выдержит и ... Но давайте не будем о грустном.")
                    reactions.aimybox?.endConversation()
                    reactions.go("/end")
                }
            }


            state ("TOO_LITTLE_SECTANT") {
                action {
                    reactions.say("Кажется, вы всё-таки оказались диким диссидентом... " +
                            "Остоновитесь, подумойте, а то вышки 5G на ваш порадиоактивят и всё!")
                    reactions.aimybox?.endConversation()
                    reactions.go("/end")
                }
            }
        }

    }
}