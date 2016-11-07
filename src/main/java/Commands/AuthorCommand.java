package Commands;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

/**
 * Created by krish on 11/6/2016.
 */
public class AuthorCommand extends BotCommand {
    private static final String LOG_TAG = AuthorCommand.class.getSimpleName();
    public AuthorCommand(){
        super("author", "the author of this bot");
    }
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        String response = "Author: Krishna C Kandula";
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString()).setText(response);
        try{
            absSender.sendMessage(message);
        } catch (TelegramApiException e){
            BotLogger.error(e.getMessage(), LOG_TAG, e);
        }
    }
}
