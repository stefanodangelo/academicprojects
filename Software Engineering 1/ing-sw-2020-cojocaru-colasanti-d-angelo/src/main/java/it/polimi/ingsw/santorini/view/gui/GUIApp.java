package it.polimi.ingsw.santorini.view.gui;

import it.polimi.ingsw.santorini.view.gui.scenes.GUIScene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Brief the singleton class that is responsible of letting the ClientApp to run JavaFX code on JavaFX's main thread. It launches JavaFX Application
 * and gives back the reference to the caller
 * @see Application
 */
public class GUIApp extends Application {

    /**
     * Brief the singleton instance
     */
    private static GUIApp instance;

    /**
     * Brief an Object lock used to wait until Application has been loaded
     */
    private static final Object lock = new Object();

    /**
     * Brief takes trace whether the stage has been already shown or not
     */
    private boolean stageShown = false;

    /**
     * Brief the Application's stage
     */
    private static Stage stage;

    public GUIApp() {
        synchronized (lock) {
            instance = this;
            lock.notify();
        }
    }

    /**
     * Brief builds a GUIApp instance making sure Application is loaded
     * @return GUIApp instance
     */
    public static GUIApp getInstance() {
        synchronized (lock) {
            while(instance == null) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return instance;
        }
    }

    /**
     * Brief starts the loading of the Application thus it also calls the constructor
     */
    public static void runApp() {
        new Thread(() -> Application.launch(GUIApp.class)).start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage stage) {
        GUIApp.stage = stage;
        Scene scene = new Scene(new StackPane());
        stage.setScene(scene);
    }

    /**
     * Brief sets the given scene to be displayed in the stage
     * @param scene GUIScene the given scene
     */
    public void setScene(GUIScene<?> scene) {
        Platform.runLater(() -> {
            stage.setTitle(scene.getTitle());
            stage.setScene(scene);
            stage.sizeToScene();
            if (!stageShown) {
                stage.show();
                stageShown = true;
            }
        });
    }
}
