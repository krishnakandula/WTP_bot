package Commands;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by krishnakandula on 11/6/16.
 */
public class HelloCommand extends BotCommand {

    public HelloCommand() {
        super("hello", "say hi to the bot!");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String response = String.format("Hello %s!", user.getFirstName());
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText(response);
        try{
            absSender.sendMessage(message);
        } catch (TelegramApiException e){
            System.out.println(e.getMessage());
        }
    }
}
