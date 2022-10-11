package nz.ac.auckland.se206.dictionary;

import java.util.List;

public class WordEntry {
  private List<String> definitions;

  public WordEntry(List<String> definitions) {
    this.definitions = definitions;
  }

  public List<String> getDefinitions() {
    return definitions;
  }
}
