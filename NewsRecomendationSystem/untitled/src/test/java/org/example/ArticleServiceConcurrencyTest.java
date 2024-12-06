package org.example;

import org.example.exceptions.APIException;
import org.example.service.ArticleService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArticleServiceConcurrencyTest {
    public static void main(String[] args) {
        // Initialize ArticleService
        ArticleService articleService = new ArticleService();

        // Number of concurrent threads (simulating concurrent users)
        int numThreads = 10;

        // Create a thread pool for concurrency
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        // Submit tasks to fetch articles concurrently
        for (int i = 0; i < numThreads; i++) {
            int userId = i + 1; // Simulate different user IDs
            executorService.submit(() -> {
                System.out.println("User " + userId + " is fetching articles...");
                try {
                    articleService.fetchArticles(); // Fetch articles
                } catch (APIException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("User " + userId + " completed fetching articles.");
            });
        }

        // Shut down the executor service
        executorService.shutdown();

        // Wait for all threads to finish
        while (!executorService.isTerminated()) {
            // Busy wait
        }

        System.out.println("All users have completed fetching articles.");
    }
}
