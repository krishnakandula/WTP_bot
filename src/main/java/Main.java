import Handlers.CommandsHandler;
import Handlers.Handler;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

/**
 * Created by krishnakandula on 11/5/16.
 */
public class Main {

    public static void main(String... args){
        System.setProperty("http.agent", "Chrome");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try{
            telegramBotsApi.registerBot(new CommandsHandler());
//            telegramBotsApi.registerBot(new Handler());
        } catch (TelegramApiException e){
            BotLogger.error(Main.class.getSimpleName(), e);
        }
    }
}
