package com.example.assignment2tasks;

import org.json.JSONArray;
import org.json.JSONObject;  // Import the correct JSONObject class

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class APIUtility {
    public List<Book> fetchBooks(String apiUrl) {
        List<Book> books = new ArrayList<>();
        try {
            // Set up connection
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == 200) {
                // Read and parse JSON response
                String jsonResponse = readApiResponse(connection);

                // Parse the response using org.json
                JSONObject jsonObject = new JSONObject(jsonResponse);

                // Assuming the response contains a "books" array
                JSONArray booksArray = jsonObject.getJSONArray("books");

                // Loop through the books array and add each book to the list
                for (int i = 0; i < booksArray.length(); i++) {
                    JSONObject bookJson = booksArray.getJSONObject(i);
                    String title = bookJson.getString("title");
                    String author = bookJson.getString("author");
                    String description = bookJson.getString("description");

                    books.add(new Book(title, author, description));
                }
            } else {
                System.out.println("Error: Could not connect to API");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    private String readApiResponse(HttpURLConnection connection) {
        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            StringBuilder resultJson = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                resultJson.append((char) c);
            }
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
