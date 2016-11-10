package Tasks;

import org.telegram.telegrambots.api.methods.ActionType;
import org.telegram.telegrambots.api.methods.send.SendChatAction;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by krishnakandula on 11/9/16.
 */
public abstract class GetDataTask implements Runnable{
    protected String chatId;
    protected AbsSender sender;
    protected static final String LOG_TAG = GetDataTask.class.getSimpleName();
    protected static final String BASE_URL = "https://www.pokeapi.co/api/v2/";
    protected abstract void doInBackground();
    protected abstract void parseResponse(String jsonString);

    public void run() {
        SendChatAction chatAction = new SendChatAction();
        chatAction.setChatId(chatId).setAction(ActionType.TYPING);
        try{
            sender.sendChatAction(chatAction);
        } catch(TelegramApiException e){
            BotLogger.error(e.getMessage(), LOG_TAG, e);
        }
        doInBackground();
    }

    protected String getData(String baseURL){
        try {
            URL url = new URL(baseURL);
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

            connection.disconnect();
            return jsonBody.toString();
        } catch (IOException e){
            sendTextResponse("Error getting data. Please try again later");
            BotLogger.error(e.getMessage(), LOG_TAG, e);
        } catch (RuntimeException e){
            sendTextResponse("Error getting data");
            BotLogger.error(e.getMessage(), LOG_TAG, e);
        }
        return null;
    }

    protected void sendTextResponse(String response){
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(chatId);
        sendMessageRequest.setText(response);
        try {
            sender.sendMessage(sendMessageRequest);
        } catch (TelegramApiException e){
            BotLogger.error(e.getMessage(), LOG_TAG, e);
        }
    }

    protected void sendPictureResponse(String pictureURL){
        SendPhoto sendSprite = new SendPhoto();
        sendSprite.setChatId(chatId).setPhoto(pictureURL);
        try{
            sender.sendPhoto(sendSprite);
        } catch (TelegramApiException e){
            BotLogger.error(LOG_TAG, e.getMessage(), e);
        }
    }

    protected String capitalizeFirstLetter(String str){
        return str.substring(0, 1).toUpperCase().concat(str.substring(1));
    }
}
