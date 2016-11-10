package Commands;

import Tasks.GetDataTask;
import Tasks.GetPokemonStatsTask;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

/**
 * Created by krish on 11/10/2016.
 */
public class StatsCommand extends BotCommand {
    private static final String LOG_TAG = StatsCommand.class.getSimpleName();

    public StatsCommand(){
        super("stats", "get the base stats for a Pokémon");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        String chatId = chat.getId().toString();

        if(arguments.length == 1) {
            GetDataTask task = new GetPokemonStatsTask(arguments[0].toLowerCase(), chatId, absSender);
            Thread thread = new Thread(task);
            thread.start();
        } else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);

            if(arguments.length < 1)
                sendMessage.setText("Please add a Pokémon to search for.");
            else
                sendMessage.setText("Too many arguments. Please enter only one Pokémon at a time.");
            try {
                absSender.sendMessage(sendMessage);
            } catch (TelegramApiException e){
                BotLogger.error(e.getMessage(), LOG_TAG, e);
            }
        }
    }
}