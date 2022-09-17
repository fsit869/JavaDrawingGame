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
   * Creates the user
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

  public void deleteProfile(Profile profile) throws IOException {
    profileRepository.deleteProfile(profile);
  }

  public void saveCurrentProfile(Profile profile) throws IOException {
    profileRepository.saveProfile(profile);
  }
}
