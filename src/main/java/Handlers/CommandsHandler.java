package Handlers;

import Commands.AuthorCommand;
import Commands.HelloCommand;
import Commands.SearchCommand;
import Commands.StatsCommand;
import Config.BotConfig;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;

/**
 * Created by krishnakandula on 11/6/16.
 */
public class CommandsHandler extends TelegramLongPollingCommandBot {

    public CommandsHandler(){
        register(new HelloCommand());
        register(new AuthorCommand());
        register(new SearchCommand());
        register(new StatsCommand());
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        //Do nothing
    }

    public String getBotUsername() {
        return BotConfig.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BotConfig.BOT_TOKEN;
    }
}
