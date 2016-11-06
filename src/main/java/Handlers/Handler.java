package Handlers;

import Tasks.GetPokemonTask;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by krish on 11/5/2016.
 */
public class Handler extends TelegramLongPollingBot {
    private static final String TOKEN = "270042279:AAELkYg0IyMYFPmfJ4zO5CGh6Sl0Pd-NX-M";
    @Override
    public String getBotToken() {
        return TOKEN;
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
