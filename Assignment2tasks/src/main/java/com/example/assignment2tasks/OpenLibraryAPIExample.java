package com.example.assignment2tasks;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenLibraryAPIExample extends Application {

    @FXML
    private TextField searchBox; // Search field for book title

    @FXML
    private Button searchButton; // Button to trigger search

    @FXML
    private ListView<String> bookListView; // ListView to display search results

    @FXML
    private Label titleLabel; // Label for displaying selected book title

    @FXML
    private Label authorLabel; // Label for displaying selected book author

    @FXML
    private Label yearLabel; // Label for displaying selected book year

    private Stage primaryStage;
    private Scene searchScene;
    private Scene detailScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        // Create UI components for the search scene
        searchBox = new TextField();
        searchBox.setPromptText("Enter book title");

        searchButton = new Button("Search");
        bookListView = new ListView<>();

        VBox searchLayout = new VBox(15, searchBox, searchButton, bookListView);

        // Set up the search scene
        searchScene = new Scene(searchLayout, 400, 400);
        searchScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm()); // Load the CSS
        primaryStage.setScene(searchScene);
        primaryStage.setTitle("Book Search App");
        primaryStage.show();

        // Set up search button action
        searchButton.setOnAction(e -> handleSearchAction());

        // Set up list view selection action
        bookListView.setOnMouseClicked(e -> handleListViewClick());
    }

    // Method to load books from Open Library API based on search query
    private void loadBooksFromAPI(String searchQuery) {
        String urlString = "https://openlibrary.org/search.json?q=" + searchQuery.replaceAll(" ", "+");

        try {
            // Open connection
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                // Parse the response data as JSON
                JSONParser parser = new JSONParser();
                JSONObject jsonResponse = (JSONObject) parser.parse(content.toString());
                JSONArray docs = (JSONArray) jsonResponse.get("docs");

                // Clear previous results
                bookListView.getItems().clear();

                // Loop through the array and add books that match the search query
                for (int i = 0; i < docs.size(); i++) {
                    JSONObject book = (JSONObject) docs.get(i);
                    String title = (String) book.getOrDefault("title", "N/A");

                    // Add the book title to ListView
                    bookListView.getItems().add(title);
                }
            } else {
                System.out.println("Request failed with code " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to handle search button click
    private void handleSearchAction() {
        String searchQuery = searchBox.getText(); // Get the search query from the TextField
        loadBooksFromAPI(searchQuery); // Fetch books from the API based on the search query
    }

    // Method to handle selection from the ListView
    private void handleListViewClick() {
        String selectedBookTitle = bookListView.getSelectionModel().getSelectedItem(); // Get the selected book title
        if (selectedBookTitle != null) {
            displayBookDetails(selectedBookTitle); // Display details of the selected book
        }
    }

    // Method to display book details on a different scene
    private void displayBookDetails(String selectedBookTitle) {
        // Create UI components for the detail scene
        VBox detailLayout = new VBox(15);
        Button backButton = new Button("Back to Results");

        // Back button action to go back to the search scene
        backButton.setOnAction(e -> primaryStage.setScene(searchScene));

        // Labels for book details
        titleLabel = new Label("Title: ");
        authorLabel = new Label("Author: ");
        yearLabel = new Label("First Published Year: ");

        // Add components to the layout
        detailLayout.getChildren().addAll(backButton, titleLabel, authorLabel, yearLabel);

        // Set up the detail scene
        detailScene = new Scene(detailLayout, 400, 400);
        detailScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm()); // Load the CSS

        // Load book details from API
        String urlString = "https://openlibrary.org/search.json?q=" + selectedBookTitle.replaceAll(" ", "+");

        try {
            // Open connection
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                // Parse the response data as JSON
                JSONParser parser = new JSONParser();
                JSONObject jsonResponse = (JSONObject) parser.parse(content.toString());
                JSONArray docs = (JSONArray) jsonResponse.get("docs");

                // Loop through the array to find the selected book
                for (int i = 0; i < docs.size(); i++) {
                    JSONObject book = (JSONObject) docs.get(i);
                    String title = (String) book.getOrDefault("title", "N/A");

                    if (title.equals(selectedBookTitle)) {
                        // Extract the author and publication year
                        JSONArray authors = (JSONArray) book.get("author_name");
                        String author = authors != null && !authors.isEmpty() ? (String) authors.get(0) : "Unknown Author";
                        Long firstPublishYear = (Long) book.get("first_publish_year");

                        // Update the labels with book details
                        titleLabel.setText("Title: " + title);
                        authorLabel.setText("Author: " + author);
                        yearLabel.setText("First Published Year: " + (firstPublishYear != null ? firstPublishYear : "N/A"));
                        break;
                    }
                }
            } else {
                System.out.println("Request failed with code " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Switch to the detail scene
        primaryStage.setScene(detailScene);
    }
}
