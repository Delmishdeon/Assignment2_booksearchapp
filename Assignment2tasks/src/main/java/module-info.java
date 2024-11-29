module com.example.assignment2tasks {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    // Third-party libraries
    requires org.controlsfx.controls;       // ControlsFX
    requires com.dlsc.formsfx;              // FormsFX
        // ValidatorFX
    requires org.kordamp.ikonli.javafx;     // Ikonli icons
    requires org.kordamp.bootstrapfx.core;  // BootstrapFX


    // JSON and HTTP-related modules
    requires java.net.http;                 // For making API calls (HttpClient)
    requires org.json;                      // For org.json.JSONObject and JSONArray
    requires json.simple;                   // For JSON.simple parsing
    requires java.desktop;                  // For AWT/Swing compatibility (optional)

    // Kotlin Standard Library
    requires kotlin.stdlib;                 // Kotlin Standard Library

    // Optional modules (only if needed for your project)
    requires jdk.javadoc;                   // Java documentation module (optional)

    // Open the package for reflection access by FXMLLoader
    opens com.example.assignment2tasks to javafx.fxml; // Allow reflection for FXMLLoader

    // Export the main package
    exports com.example.assignment2tasks;   // Export main package for external access
}
