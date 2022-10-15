package nz.ac.auckland.se206.profiles;

// is responsible for accessing the repository, to be able to grab and write information for the
// rest of the program.

import java.io.IOException;
import java.util.List;
import nz.ac.auckland.se206.profiles.entities.Profile;

public class ProfileFactory {

  private final ProfileRepository profileRepository;

  public ProfileFactory() throws IOException {
    this.profileRepository = new ProfileRepository();
  }

  /**
   * Search for a Profile with such username, returns null if not found
   *
   * @param username of profile to find
   * @return Profile of such user
   */
  public Profile selectProfile(String username) {
    return profileRepository.selectProfile(username);
  }

  /**
   * Creates the user and puts it into the JSOn file.
   *
   * @param username of profile to create
   * @param profilePicturePath path to the profile picture of the user
   * @throws IOException e
   */
  public void createProfile(String username, String profilePicturePath) throws IOException {
    profileRepository.createProfile(username, profilePicturePath);
  }

  /**
   * used at starting screen when giving user different options to select for profile
   *
   * @return list of every single profile
   * @throws IOException d
   */
  public List<Profile> getAllProfiles() throws IOException {
    return ProfileRepository.getAllProfiles();
  }

  /**
   * Deletes a Profile, by setting all the attributes to their defaults. Does not remove the object
   * from JSON
   *
   * @param profile the Profile to delete
   * @throws IOException IO
   */
  public void deleteProfile(Profile profile) throws IOException {
    profileRepository.deleteProfile(profile);
  }

  /**
   * Saves a profile that is parsed into the function, rewrites the entire JSON file.
   *
   * @param profile the profile to save
   * @throws IOException IO
   */
  public void saveProfile(Profile profile) throws IOException {
    profileRepository.saveProfile(profile);
  }

  /**
   * Sorts the profile so that the empty profiles are in the end
   *
   * @throws IOException file exception
   */
  public void sortProfiles() throws IOException {
    profileRepository.sortProfiles();
  }
}
