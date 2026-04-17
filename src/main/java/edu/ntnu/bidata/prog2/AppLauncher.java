package edu.ntnu.bidata.prog2;

import edu.ntnu.bidata.prog2.ui.view.WindowView;
import javafx.application.Application;

/**
 * Application entry point for the Millions stock market simulation.
 * Launches the JavaFX GUI defined in {@link WindowView}.
 */
public class AppLauncher {

    /**
     * Main method — starts the JavaFX application.
     *
     * @param args command-line arguments passed to the JavaFX runtime
     */
    public static void main(String[] args) {
        Application.launch(WindowView.class, args);
    }
}