package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class FxmlLoaderData {
    private Parent root;
    private FXMLLoader fxmlLoader;

    public FxmlLoaderData(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
        try {
            this.root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Parent getRoot() {
        return root;
    }

    public FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

}
