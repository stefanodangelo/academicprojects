package it.polimi.ingsw.santorini.view.gui.scenes;

import it.polimi.ingsw.santorini.view.gui.scenes.delegates.GUISceneDelegate;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Brief A JavaFX's Scene customization to make it easy to standardize a scene's size, title, color. It also comes with a
 * dedicated GUISceneDelegate reference
 * @param <T> The type of the bound GUISceneDelegate
 * @see Scene
 * @see GUISceneDelegate
 */
public abstract class GUIScene<T extends GUISceneDelegate> extends Scene {

    /**
     * Brief the title of the stage
     */
    private String title;

    /**
     * Brief the delegate
     */
    private T delegate;

    /**
     * Brief tells if the scene has been loaded
     */
    private Boolean loaded = false;

    /**
     * Brief it loads the scene
     */
    public void loadScene() {
        customLoadScene();
        loaded = true;
    }

    /**
     * Brief it loads the scene
     */
    public abstract void customLoadScene();

    /**
     * Brief constructor that sets the scene's width, height, title and color
     * @param width double the width
     * @param height double the height
     * @param title String the title
     * @param color Color the color
     */
    public GUIScene(double width, double height, String title, Color color) {
        super(new StackPane(), width, height, color);
        this.title = title;
    }

    /**
     * Brief constructor that sets the scene's width, height, title
     * @param width double the width
     * @param height double the height
     * @param title String the title
     */
    public GUIScene(double width, double height, String title) {
        this(width, height, title, Color.BLACK);
    }

    /**
     * Brief runs code safely on JavaFX Thread
     * @param runnable is the class to be run
     */
    protected void runSafely(Runnable runnable) {
        Platform.runLater(runnable);
    }

    /**
     * Brief runs code on another Thread different from JavaFX Thread
     * @param runnable is the class to be run
     */
    protected void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }

    /**
     * Brief getter for the root node
     * @return StackPane root
     */
    public StackPane root() {
        return (StackPane) getRoot();
    }

    /**
     * Brief getter for title
     * @return String title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Brief setter for title
     * @param title String title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Brief getter for delegate
     * @return T delegate
     */
    public T delegate() {
        return delegate;
    }

    /**
     * Brief setter for delegate
     * @param delegate T delegate
     */
    public void setDelegate(T delegate) {
        this.delegate = delegate;
    }

    /**
     * Brief getter for loaded state
     * @return Boolean loaded
     */
    public Boolean isLoaded() {
        return loaded;
    }
}
