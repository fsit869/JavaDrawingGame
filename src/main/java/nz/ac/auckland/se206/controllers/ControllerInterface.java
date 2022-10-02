package nz.ac.auckland.se206.controllers;

public interface ControllerInterface {
    /**
     * Method called for view when entire app is initialized. This is called at start of program
     */
    void initialize();

    /**
     * Method called whenever the view is loaded
     */
    void refresh();
}
