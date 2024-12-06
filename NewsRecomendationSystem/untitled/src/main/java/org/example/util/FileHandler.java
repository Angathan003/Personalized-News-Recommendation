package org.example.util;

import com.google.gson.reflect.TypeToken;
import  org.example.model.Article;
import com.google.gson.Gson;
import org.example.model.User;
import java.io.*;
import java.util.HashMap;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileHandler {
    private static final String ARTICLE_DATA_FILE = "articles.json";
    private static final String USER_DATA_FILE = "users.dat";
    private Gson gson;

    public FileHandler() {
        this.gson = new Gson();
    }

    public Map<String, User> readUsersFromFile() {
        Map<String, User> users = new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_FILE))) {
            users = (Map<String, User>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("User data file not found. Starting with empty user database.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<Article> readArticlesFromFile() {
        List<Article> articles = new ArrayList<>();
        try (Reader reader = new FileReader(ARTICLE_DATA_FILE)) {
            Type listType = new TypeToken<ArrayList<Article>>() {}.getType();
            articles = gson.fromJson(reader, listType);
        } catch (FileNotFoundException e) {
            System.out.println("Article data file not found. Fetching new articles.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return articles;
    }

    public void writeUsersToFile(Map<String, User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeArticlesToFile(List<Article> articles) {
        try (Writer writer = new FileWriter(ARTICLE_DATA_FILE)) {
            gson.toJson(articles, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
