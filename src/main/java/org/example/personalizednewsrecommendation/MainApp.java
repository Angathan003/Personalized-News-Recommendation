package org.example.personalizednewsrecommendation;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.controllers.LoginScreen;
import org.example.personalizednewsrecommendation.services.ArticleManager;
import org.example.personalizednewsrecommendation.services.DatabaseManager;
import org.example.personalizednewsrecommendation.services.UserManager;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        String datasetPath = "D:\\Second year IIT\\OOD\\Personalized-News-Recommendation\\FrontEnd\\Personalized-News-Recommendation\\src\\main\\resources\\org\\example\\personalizednewsrecommendation\\CNN_Articels_clean.csv";

        // Initialize services
        ArticleManager articleManager = new ArticleManager(datasetPath);
        DatabaseManager databaseManager = new DatabaseManager();
        UserManager userManager = new UserManager(databaseManager);

        // Set the initial scene
        LoginScreen loginScreen = new LoginScreen(userManager, articleManager);
        primaryStage.setScene(loginScreen.getLoginScene(primaryStage));
        primaryStage.setTitle("Personalized News Recommendation System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
