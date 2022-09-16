package nz.ac.auckland.se206.profiles;

// is responsible for accessing the repository, to be able to grab and write information for the
// rest of the program.

import java.util.ArrayList;
import nz.ac.auckland.se206.profiles.entities.Profile;

public class ProfileFactory {

  public Profile selectProfile() {
    return null;
  }

  public boolean createProfile() {
    return true; // successful?
  }

  public ArrayList<Profile> getAllProfiles() {
    return new ArrayList<>();
  }

  public boolean deleteProfile() {
    return true;
  }

  public boolean saveCurrentProfile() {
    return true;
  }
}
