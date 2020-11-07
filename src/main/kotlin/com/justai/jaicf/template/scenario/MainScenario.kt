package com.justai.jaicf.template.scenario

import com.justai.jaicf.model.scenario.Scenario
import com.justai.jaicf.template.Consequence
import com.justai.jaicf.template.questCases

object MainScenario : Scenario(dependencies = listOf(QuestScenario)) {

    init {
        state("start") {
            activators {
                regex("/start")
            }
            action {
                reactions.say("Привет! Маленький дисклеймер: этот навык не призывает к ковид-диссиденству. " +
                        "Он создавался только в развлекательных целях " +
                        "и не пропагандирует халатное отношение к мерам безопасности. " +
                        "Пожалуйста, будьте ответственны.")
                reactions.say("Начнем?")
            }


            state("letsgo") {
                activators {
                    regex("да")
                    intent("Yes")
                }

                action {
                    val stateInfo = Controller(context)
                    stateInfo.currentSituation = questCases.getRandomSituation()!!
                    stateInfo.dissidentScore = 50
                    stateInfo.sectantScore = 50
                    stateInfo.currentConsequence = Consequence(0, 0, "", null)
                    reactions.say("В мире пандемия COVID-19. От нас - ситуации, от вас - ответ \"да\" или \"нет\". " +
                            "Сохраняйте баланс между ковид-диссиденством и слепым фанатизмом. " +
                            "Главное - ваш ментальный баланс. Начинаем.")
                    reactions.go("/cases")
                }
            }
        }

        state("end"){
            action {
                reactions.say("Хотите заново начать?")
            }

            state("yes") {
                activators {
                    intent("Yes")
                }

                action {
                    reactions.go("start")
                }
            }

            fallback {
                reactions.say("Захотите начать заново - /start и всё завертится сначала!")
            }
        }

        fallback {
            reactions.sayRandom(
                "Sorry, I didn't get that...",
                "Sorry, could you repeat please?"
            )
        }
    }
}