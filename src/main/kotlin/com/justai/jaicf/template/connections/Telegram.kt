package com.justai.jaicf.template.connections

import com.justai.jaicf.channel.telegram.TelegramChannel
import com.justai.jaicf.template.templateBot


fun main() {
    TelegramChannel(templateBot, "630934328:AAFMitvk3doJJxAewTGdQF5tUEOO5raiirc").run()
}