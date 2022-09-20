package nz.ac.auckland.se206.profiles;

// is responsible for accessing the database, and writing to the database. (json file)

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import nz.ac.auckland.se206.profiles.entities.Profile;

public class ProfileRepository {

  private final List<Profile> profiles;

  protected ProfileRepository() throws IOException {
    this.profiles = getAllProfiles();
  }

  /**
   * Selects the specific profile with the parsed in username
   *
   * @param username of profile to select
   * @return Profile object of chosen profile
   */
  protected Profile selectProfile(String username) {
    for (Profile profile : profiles) {
      // Searches for the specified username
      if (profile.getUsername().equals(username)) {
        return profile;
      }
    }
    throw new NoSuchElementException("Profile not found");
  }

  /**
   * Creates a new Profile by populating the object with username and picture path.
   *
   * @param username of the Profile
   * @param profilePicturePath path to the profile's picture
   * @return if the user was successfully created, or user already exists
   * @throws IOException IO
   */
  protected boolean createProfile(String username, String profilePicturePath) throws IOException {
    username = username.trim();
    // Loop through all the profiles
    for (Profile profile : profiles) {
      if (profile.getUsername().equalsIgnoreCase(username)) {
        System.out.println("User already created");
        return false;
      }

      // Overwrite. If profile is empty, default empty profile is ""
      if (profile.getUsername().equals("")) {
        profile.setUsername(username);
        profile.setProfilePicturePath(profilePicturePath);
        saveProfile(profile);
        System.out.println("Successfully Created");
        return true;
      }
    }

    // In this case, all 5 slots of users are taken
    throw new IndexOutOfBoundsException("All 5 user slots are taken");
  }

  /**
   * Saves the profile that was parsed into the function into the JSON file.
   *
   * @param saveProfile profile to save into the JSON file
   * @throws IOException IO
   */
  protected void saveProfile(Profile saveProfile) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    StringBuilder json = new StringBuilder("[\n");
    FileWriter fileWriter = new FileWriter("src/main/resources/player_data.json");

    // Formats the JSON into the proper format.
    for (int i = 0; i < profiles.size(); i++) {

      // Check if the current profile is the same, if not, write to JSON.
      if (profiles.get(i).getUsername().equals(saveProfile.getUsername())) {
        json.append(gson.toJson(saveProfile, Profile.class));
      } else {
        json.append(gson.toJson(profiles.get(i), Profile.class));
      }
      if (i != profiles.size() - 1) {
        json.append(",\n");
      }
    }
    json.append("\n]");

    // Save to the JSON file
    fileWriter.write(String.valueOf(json));
    fileWriter.close();
  }

  /**
   * Deletes a Profile, that was parsed into the function. Empties the contents in the JSON file.
   *
   * @param deleteProfile Profile to be deleted
   * @throws IOException IO
   */
  protected void deleteProfile(Profile deleteProfile) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    FileWriter fileWriter = new FileWriter("src/main/resources/player_data.json");
    StringBuilder json = new StringBuilder("[\n");

    // Formats the JSON into the proper format.
    for (int i = 0; i < profiles.size(); i++) {

      // Check if the current profile is the same, if not, write to JSON.
      if (profiles.get(i).getUsername().equals(deleteProfile.getUsername())) {
        deleteProfile.setUsername("");
        deleteProfile.setProfilePicturePath("");
        deleteProfile.resetData();
        json.append(gson.toJson(deleteProfile, Profile.class));
      } else {
        json.append(gson.toJson(profiles.get(i), Profile.class));
      }

      // End of the file
      if (i != profiles.size() - 1) {
        json.append(",\n");
      }
    }
    json.append("\n]");

    // Writes to the JSON file
    fileWriter.write(String.valueOf(json));
    fileWriter.close();
  }

  /**
   * Returns all the Profiles as a list of profiles. For displaying all the profiles to the user for
   * them to select a Profile
   *
   * @return List of all Profiles
   * @throws IOException IO
   */
  protected static List<Profile> getAllProfiles() throws IOException {
    List<Profile> profiles = new ArrayList<>();

    // Path of the JSON to read from.
    Path profilePath = Path.of("src/main/resources/player_data.json");
    String rawProfiles = Files.readString(profilePath);

    // This streamer will parse in all the JSONs in the file.
    JsonStreamParser streamParser = new JsonStreamParser(rawProfiles);

    // For every JSON object in the file, this will run (until EOF)
    while (streamParser.hasNext()) {
      JsonElement page = streamParser.next();

      // JSON file is an array of elements, so needs to convert into array.
      if (page.isJsonArray()) {
        JsonArray elements = (JsonArray) page;
        for (JsonElement element : elements) {
          Profile profile = new Gson().fromJson(element, Profile.class);
          profiles.add(profile);
        }
      }
    }

    return profiles;
  }
}
