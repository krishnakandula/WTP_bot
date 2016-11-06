package Tasks;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.telegram.telegrambots.api.methods.ActionType;
import org.telegram.telegrambots.api.methods.send.SendChatAction;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

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
    private Message message;
    public boolean hasPokemon;
    private String pokemonName;
    private TelegramLongPollingBot bot;

    public GetPokemonTask(Message message, TelegramLongPollingBot bot){
        this.message = message;
        this.bot = bot;
    }

    public void run() {
        //Parse message to get pokemon
        parseMessage();
        if(hasPokemon) {
            SendChatAction chatAction = new SendChatAction();
            chatAction.setChatId(message.getChatId().toString()).setAction(ActionType.TYPING);
            try {
                bot.sendChatAction(chatAction);
            } catch (TelegramApiException e){
                System.out.println(e.getMessage());
            }
            getDataWithNameOrId(pokemonName);
        }
    }

    private void parseMessage(){
        String pattern = "/pokemon ";
        if(message.getText().length() <= pattern.length()) {
            hasPokemon = false;
            return;
        }
        hasPokemon = true;
        pokemonName = message.getText().substring(pattern.length());
    }

    private void parseResponse(String jsonString){
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

        String responseStr = String.format(nameString + "\n"
                                            + "Ability: %s\n"
                                            + "Hidden Ability: %s", abilityString, hiddenAbilityString);

        sendResponse(responseStr);
    }

    private void sendResponse(String response){
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(message.getChatId().toString());
        sendMessageRequest.setText(response);
        try {
            bot.sendMessage(sendMessageRequest);
        } catch (TelegramApiException e){
            System.out.println(e.getMessage());
        }
    }

    private void getDataWithNameOrId(String name){
        StringBuilder BASE_URL = new StringBuilder("https://www.pokeapi.co/api/v2/pokemon/");
        BASE_URL.append(name.toLowerCase() + "/");
        System.out.println(BASE_URL.toString());
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

            parseResponse(jsonBody.toString());
            connection.disconnect();
        } catch (IOException e){
            System.out.println(e.getMessage());
            hasPokemon = false;
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            hasPokemon = false;
        }
    }
}
