package org.example.personalizednewsrecommendation.services;

import org.example.personalizednewsrecommendation.models.Article;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CSVImporter {

    public void importArticlesFromCSV(String filePath, Connection connection) {
        String query = "INSERT INTO articles (title, content, category) VALUES (?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement stmt = connection.prepareStatement(query)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(","); // Assuming CSV fields are separated by commas
                String title = fields[0];
                String content = fields[1];
                String category = fields[2];

                stmt.setString(1, title);
                stmt.setString(2, content);
                stmt.setString(3, category);
                stmt.executeUpdate();
            }
            System.out.println("Articles imported successfully from CSV.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
