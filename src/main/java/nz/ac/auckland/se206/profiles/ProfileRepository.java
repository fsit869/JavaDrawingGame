package nz.ac.auckland.se206.profiles;

// is responsible for accessing the database, and writing to the database. (json file)

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import nz.ac.auckland.se206.profiles.entities.Profile;

public class ProfileRepository {

  public boolean deleteProfile() {
    return true;
  }

  // should be extended to words, settings and stats.
  public boolean saveProfile(Profile profile) {

    return true;
  }

  public ArrayList<Profile> getAllProfiles() throws IOException {
    ArrayList<Profile> profiles = new ArrayList<>();

    // Path of the JSON to read from.
    Path profilePath = Path.of("src/main/resources/player_data.json");
    String rawProfiles = Files.readString(profilePath);

    // This streamer will parse in all the JSONs in the file.
    JsonStreamParser streamParser = new JsonStreamParser(rawProfiles);

    // For every JSON object in the file, this will run (until EOF)
    while (streamParser.hasNext()) {
      JsonElement element = streamParser.next();

      // checks if object is valid
      if (element.isJsonObject()) {
        Profile profile = new Gson().fromJson(element, Profile.class);
        profiles.add(profile);
      }
    }

    return profiles;
  }

  // purely for testing
  public static void main(String... args) throws Exception {
    ProfileRepository yes = new ProfileRepository();
    ArrayList<Profile> ok = yes.getAllProfiles();
  }
}
