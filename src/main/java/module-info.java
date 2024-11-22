module org.example.personalizednewsrecommendation {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.personalizednewsrecommendation to javafx.fxml;
    exports org.example.personalizednewsrecommendation;
}