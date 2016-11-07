package Commands;

import Tasks.GetPokemonTask;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import java.util.Arrays;

/**
 * Created by krish on 11/6/2016.
 */
public class SearchCommand extends BotCommand {
    public SearchCommand(){
        super("search", "search for a Pokémon");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        System.out.println(Arrays.toString(arguments));
//        GetPokemonTask getPokemonTask = new GetPokemonTask()

    }

}
