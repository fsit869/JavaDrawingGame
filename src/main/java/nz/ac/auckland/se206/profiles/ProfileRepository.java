package nz.ac.auckland.se206.profiles;

// is responsible for accessing the database, and writing to the database. (json file)

import java.util.ArrayList;

public class ProfileRepository {

  public boolean deleteProfile() {
    return true;
  }

  public boolean saveProfile() {
    return true;
  }

  public ArrayList<String> getAllProfiles() {
    return new ArrayList<>();
  }
}
