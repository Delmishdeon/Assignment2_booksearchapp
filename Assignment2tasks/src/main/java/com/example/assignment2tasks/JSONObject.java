package com.example.assignment2tasks;

import java.util.HashMap;
import java.util.Map;

public class JSONObject {
    private final Map<String, Object> data;

    // Constructor to initialize the internal data structure
    public JSONObject() {
        this.data = new HashMap<>();
    }

    // Constructor to initialize from a JSON string
    public JSONObject(String jsonString) {
        this();
        parseJSONString(jsonString);
    }

    // Method to put a key-value pair
    public void put(String key, Object value) {
        data.put(key, value);
    }

    // Method to get a value by key
    public Object get(String key) {
        if (!data.containsKey(key)) {
            throw new IllegalArgumentException("Key '" + key + "' not found in JSONObject.");
        }
        return data.get(key);
    }

    // Method to get a value as a string
    public String getString(String key) {
        return String.valueOf(get(key));
    }

    // Method to get a value as an integer
    public int getInt(String key) {
        Object value = get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        throw new IllegalArgumentException("Key '" + key + "' does not contain an integer.");
    }

    // Method to check if a key exists
    public boolean has(String key) {
        return data.containsKey(key);
    }

    // Parse a simple JSON string (not fully featured, for educational purposes)
    private void parseJSONString(String jsonString) {
        jsonString = jsonString.trim();

        if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
            jsonString = jsonString.substring(1, jsonString.length() - 1); // Remove braces

            String[] pairs = jsonString.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replace("\"", ""); // Remove quotes
                    String value = keyValue[1].trim().replace("\"", "");
                    put(key, value);
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid JSON string");
        }
    }

    // Convert the JSONObject back to a JSON string
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            sb.append("\"").append(entry.getKey()).append("\":");
            sb.append("\"").append(entry.getValue()).append("\",");
        }

        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1); // Remove the last comma
        }

        sb.append("}");
        return sb.toString();
    }
}
