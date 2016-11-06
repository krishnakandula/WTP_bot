package Handlers;

import Config.BotConfig;
import Tasks.GetPokemonTask;
import org.telegram.telegrambots.api.methods.send.SendChatAction;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

/**
 * Created by Krishna Chaitanya Kandula on 11/5/2016.
 */
public class Handler extends TelegramLongPollingBot {
    @Override
    public String getBotToken() {
        return BotConfig.BOT_TOKEN;
    }

    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            Message message = update.getMessage();
            if(message.hasText()){
                GetPokemonTask getPokemonTask = new GetPokemonTask(message, this);
                Thread thread = new Thread(getPokemonTask);
                thread.start();

            }
        }
    }

    public String getBotUsername() {
        return "WTP_bot";
    }
}
