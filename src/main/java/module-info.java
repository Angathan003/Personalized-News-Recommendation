module org.example.personalizednewsrecommendation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.personalizednewsrecommendation to javafx.fxml;
    exports org.example.personalizednewsrecommendation;
    exports org.example.personalizednewsrecommendation.controllers;
    opens org.example.personalizednewsrecommendation.controllers to javafx.fxml;
    exports org.example.personalizednewsrecommendation.utils;
    opens org.example.personalizednewsrecommendation.utils to javafx.fxml;
}