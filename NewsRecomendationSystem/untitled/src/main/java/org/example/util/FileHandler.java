package org.example.util;

import org.example.model.Article;
import org.example.model.User;

import java.io.*;
import java.util.List;
import java.util.Map;

public class FileHandler {
    private static final String USERS_FILE = "users.dat";
    private static final String ARTICLES_FILE = "articles.dat";

    /**
     * Writes the user database to a file.
     *
     * @param userDatabase The map of username to User objects.
     * @throws IOException If writing to the file fails.
     */
    public void writeUsersToFile(Map<String, User> userDatabase) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(userDatabase);
        }
    }

    /**
     * Reads the user database from a file.
     *
     * @return The map of username to User objects, or null if the file doesn't exist.
     * @throws IOException            If reading the file fails.
     * @throws ClassNotFoundException If the User class is not found.
     */
    public Map<String, User> readUsersFromFile() throws IOException, ClassNotFoundException {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            System.out.println("User data file not found. Starting with empty user database.");
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            return (Map<String, User>) ois.readObject();
        }
    }

    /**
     * Writes the articles list to a file.
     *
     * @param articles The list of Article objects.
     * @throws IOException If writing to the file fails.
     */
    public void writeArticlesToFile(List<Article> articles) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARTICLES_FILE))) {
            oos.writeObject(articles);
        }
    }

    /**
     * Reads the articles list from a file.
     *
     * @return The list of Article objects, or null if the file doesn't exist.
     * @throws IOException            If reading the file fails.
     * @throws ClassNotFoundException If the Article class is not found.
     */
    public List<Article> readArticlesFromFile() throws IOException, ClassNotFoundException {
        File file = new File(ARTICLES_FILE);
        if (!file.exists()) {
            System.out.println("Articles data file not found. Starting with empty articles list.");
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARTICLES_FILE))) {
            return (List<Article>) ois.readObject();
        }
    }
}
