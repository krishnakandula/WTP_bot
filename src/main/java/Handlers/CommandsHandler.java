package Handlers;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;

/**
 * Created by krishnakandula on 11/6/16.
 */
public class CommandsHandler extends TelegramLongPollingCommandBot {

    @Override
    public void processNonCommandUpdate(Update update) {

    }

    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return null;
    }
}
