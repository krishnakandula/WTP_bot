import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import java.util.List;

/**
 * Created by krishnakandula on 11/5/16.
 */
public class Main {
    private static final String TOKEN = "270042279:AAELkYg0IyMYFPmfJ4zO5CGh6Sl0Pd-NX-M";

    public static void main(String... args){
        TelegramBot bot = TelegramBotAdapter.build(TOKEN);
        GetUpdates getUpdates = new GetUpdates().limit(1000).offset(0).timeout(10);
        GetUpdatesResponse updatesResponse = bot.execute(getUpdates);
        List<Update> updates = updatesResponse.updates();
        System.out.println(updates.size());
        for(Update u : updates)
            System.out.println(u.message().text());

    }
}
