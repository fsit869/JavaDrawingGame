package nz.ac.auckland.se206.dictionary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DictionaryLookUp {

  private static final String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

  public static WordInfo searchWordInfo(String query) throws IOException, WordNotFoundException {

    // Create the HTTP request client
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(API_URL + query).build();
    Response response = client.newCall(request).execute();
    ResponseBody responseBody = response.body();

    // Response
    String jsonString = responseBody.string();

    // Check if word exists
    try {
      JSONObject jsonObj = (JSONObject) new JSONTokener(jsonString).nextValue();
      String title = jsonObj.getString("title");

      throw new WordNotFoundException(query, title);
    } catch (ClassCastException e) {
    }

    // Add all values of the entries
    JSONArray jArray = (JSONArray) new JSONTokener(jsonString).nextValue();
    List<WordEntry> entries = new ArrayList<WordEntry>();

    // Add all defintiions
    JSONObject jsonEntryObj = jArray.getJSONObject(0);
    JSONArray jsonMeanings = jsonEntryObj.getJSONArray("meanings");
    List<String> definitions = new ArrayList<String>();

    JSONObject jsonMeaningObj = jsonMeanings.getJSONObject(0);

    // Lookup definitions
    JSONArray jsonDefinitions = jsonMeaningObj.getJSONArray("definitions");
    JSONObject jsonDefinitionObj = jsonDefinitions.getJSONObject(0);
    String definition = jsonDefinitionObj.getString("definition");
    if (!definition.isEmpty()) {
      definitions.add(definition);
    }

    // Return the word with all the data.
    WordEntry wordEntry = new WordEntry(definitions);
    entries.add(wordEntry);
    return new WordInfo(query, entries);
  }
}
