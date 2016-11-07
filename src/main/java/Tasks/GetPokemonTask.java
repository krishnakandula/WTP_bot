package Tasks;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.telegram.telegrambots.api.methods.ActionType;
import org.telegram.telegrambots.api.methods.send.SendChatAction;
import org.telegram.telegrambots.api.methods.send.SendMessage;
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
public class GetPokemonTask implements Runnable {
    private String pokemonName;
    private String chatId;
    private AbsSender absSender;

    private static final String LOG_TAG = GetPokemonTask.class.getSimpleName();

    public GetPokemonTask(String pokemonName, String chatId, AbsSender absSender){
        this.pokemonName = pokemonName;
        this.absSender = absSender;
        this.chatId = chatId;
    }

    public void run() {
        SendChatAction chatAction = new SendChatAction();
        chatAction.setChatId(chatId).setAction(ActionType.TYPING);
        try {
            absSender.sendChatAction(chatAction);
        } catch (TelegramApiException e){System.out.println(e.getMessage());}
        getDataWithNameOrId(pokemonName);
    }

    private String parseResponse(String jsonString){
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

        String responseStr = String.format(formatJsonString(nameString) + "\n"
                                            + "Ability: %s\n"
                                            + "Hidden Ability: %s",
                                            formatJsonString(abilityString), formatJsonString(hiddenAbilityString));

        return responseStr;
    }

    private void sendResponse(String response){
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(chatId);
        sendMessageRequest.setText(response);
        try {
            absSender.sendMessage(sendMessageRequest);
        } catch (TelegramApiException e){
            BotLogger.error(e.getMessage(), LOG_TAG, e);
        }
    }

    private void getDataWithNameOrId(String name){
        StringBuilder BASE_URL = new StringBuilder("https://www.pokeapi.co/api/v2/pokemon/");
        BASE_URL.append(name.toLowerCase() + "/");
        try {
            URL url = new URL(BASE_URL.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if(connection.getResponseCode() != 200)
                throw new RuntimeException("Failed: HTTTP error code: " + connection.getResponseCode());

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonBody = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null)
                jsonBody.append(line);

            sendResponse(parseResponse(jsonBody.toString()));
            connection.disconnect();
        } catch (IOException e){
            //TODO: Replace souts with BotLogger
            sendResponse("Error getting data. Please try again later.");
            BotLogger.error(e.getMessage(), LOG_TAG, e);
        } catch (RuntimeException e){
            sendResponse("Error getting data.");
            BotLogger.error(e.getMessage(), LOG_TAG, e);
        }
    }

    private String formatJsonString(String str){
        str = str.substring(0, 1).toUpperCase().concat(str.substring(1));
        return str;
    }
}