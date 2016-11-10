package Tasks;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.telegram.telegrambots.api.methods.ActionType;
import org.telegram.telegrambots.api.methods.send.SendChatAction;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Krishna Chaitanya Kandula on 11/5/2016.
 */
public class GetPokemonTask extends GetDataTask {
    private String pokemonName;
    private static final String LOG_TAG = GetPokemonTask.class.getSimpleName();
    private static final String ENDPOINT = "pokemon/";

    public GetPokemonTask(String pokemonName, String chatId, AbsSender sender){
        this.pokemonName = pokemonName;
        this.sender = sender;
        this.chatId = chatId;
    }

    @Override
    protected void doInBackground() {
        String url = buildBaseURL(pokemonName);
        String jsonResponse = getData(url);
        if(jsonResponse != null)
            parseResponse(jsonResponse);
    }

    @Override
    protected void parseResponse(String jsonString){
        final String formsArrKey = "forms";
        final String pokemonNameKey = "name";
        final String abilityArrKey = "abilities";
        final String abilityObjKey = "ability";
        final String abilityNameKey = "name";

        JSONObject mainObj = new JSONObject(jsonString);
        String nameString = mainObj.getJSONArray(formsArrKey).getJSONObject(0).getString(pokemonNameKey);
        JSONArray abilitiesObj = mainObj.getJSONArray(abilityArrKey);
        String abilityString = abilitiesObj.getJSONObject(1).getJSONObject(abilityObjKey).getString(abilityNameKey);
        String hiddenAbilityString = abilitiesObj.getJSONObject(0).getJSONObject(abilityObjKey).getString(abilityNameKey);
        JSONArray typesArr = mainObj.getJSONArray("types");

        //Get types
        StringBuilder typesStringBuilder = new StringBuilder();
        for(int i = 0; i < typesArr.length(); i++) {
            typesStringBuilder.append(capitalizeFirstLetter(typesArr.getJSONObject(i).getJSONObject("type").getString("name")));
            if(i < typesArr.length() - 1)
                typesStringBuilder.append("/");
        }
        String spriteUrl = mainObj.getJSONObject("sprites").getString("front_default");

        String responseStr = String.format(capitalizeFirstLetter(nameString) + "\n"
                                            + "Type: %s\n"
                                            + "Ability: %s\n"
                                            + "Hidden Ability: %s\n",
                                            typesStringBuilder.toString(), capitalizeFirstLetter(abilityString),
                                            capitalizeFirstLetter(hiddenAbilityString));

        sendTextResponse(responseStr);
        sendPictureResponse(spriteUrl);
    }

    @NotNull
    private String buildBaseURL(String id){
        StringBuilder baseURL = new StringBuilder(BASE_URL);
        baseURL.append(ENDPOINT).append(id).append("/");
        return baseURL.toString();
    }
}