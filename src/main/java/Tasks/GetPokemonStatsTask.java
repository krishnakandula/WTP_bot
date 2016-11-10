package Tasks;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.AbsSender;

/**
 * Created by krishnakandula on 11/9/16.
 */
public class GetPokemonStatsTask extends GetDataTask {
    private static final String ENDPOINT = "pokemon/";
    private final String pokemonId;

    public GetPokemonStatsTask(String pokemonId, String chatId, AbsSender sender){
        this.sender = sender;
        this.chatId = chatId;
        this.pokemonId = pokemonId;
    }

    @Override
    protected void doInBackground() {
        String url = buildBaseURL(pokemonId);
        String jsonResponse = getData(url);
        if(jsonResponse != null)
            parseResponse(jsonResponse);
    }

    @Override
    protected void parseResponse(String jsonString) {
        JSONObject mainObj = new JSONObject(jsonString);
        String nameString = mainObj.getJSONArray("forms").getJSONObject(0).getString("name");
        JSONArray statsArr = mainObj.getJSONArray("stats");
        StringBuilder statsBuilder = new StringBuilder(capitalizeFirstLetter(nameString) + " Base Stats:\n");
        for(int i = 0; i < statsArr.length(); i++) {
            JSONObject statObj = statsArr.getJSONObject(i);
            JSONObject statNameObj = statObj.getJSONObject("stat");
            statsBuilder.append(String.format("%s: %d\n",
                                                capitalizeFirstLetter(statNameObj.getString("name")), statObj.getInt("base_stat")));
        }
        sendTextResponse(statsBuilder.toString());
    }

    private String buildBaseURL(String id){
        StringBuilder base = new StringBuilder(BASE_URL);
        base.append(ENDPOINT).append(id).append("/");
        return base.toString();
    }
}