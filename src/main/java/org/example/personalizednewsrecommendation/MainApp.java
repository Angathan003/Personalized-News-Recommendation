package org.example.personalizednewsrecommendation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.controllers.LoginScreen;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginScreen loginScreen = new LoginScreen();
        primaryStage.setScene(loginScreen.getLoginScene(primaryStage));
        primaryStage.setTitle("Personalized News Recommendation System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
