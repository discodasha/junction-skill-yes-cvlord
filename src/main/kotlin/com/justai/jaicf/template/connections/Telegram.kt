package com.justai.jaicf.template.connections

import com.justai.jaicf.channel.telegram.TelegramChannel
import com.justai.jaicf.template.templateBot


fun main() {
    TelegramChannel(templateBot, "1481559613:AAEYTeJGGKpGOH48XTvY5mW_CEo2GfUH7Q8").run()
}