package it.polimi.ingsw.santorini.view.gui.controllers;

import it.polimi.ingsw.santorini.view.gui.controllers.delegates.GUISceneControllerDelegate;
import it.polimi.ingsw.santorini.view.gui.scenes.GUIScene;
import it.polimi.ingsw.santorini.view.gui.scenes.delegates.GUISceneDelegate;
import javafx.application.Platform;

/**
 * Brief the controller that handles the logic and the requests of a GUIScene. It is also bound to a
 * GUISceneControllerDelegate to fulfill incoming requests.
 * @param <T> the type of the GUIScene
 * @param <S> the type of the GUISceneControllerDelegate
 * @see GUIScene
 * @see GUISceneControllerDelegate
 */
public abstract class GUISceneController<T extends GUIScene<?>, S extends GUISceneControllerDelegate> implements GUISceneDelegate {

    /**
     * Brief the wrapped scene
     */
    private final T scene;

    /**
     * Brief the delegate
     */
    private final S delegate;

    /**
     * Brief constructor that sets the controller's scene and delegate
     * @param scene the involved scene
     * @param delegate the involved delegate
     */
    public GUISceneController(T scene, S delegate) {
        this.scene = scene;
        this.delegate = delegate;
    }

    /**
     * Brief tells if the scene has been loaded
     */
    private Boolean loaded = false;

    /**
     * Brief loads the wrapped scene
     */
    protected void loadScene() {
        runSafely(() -> {
            scene.loadScene();
            loadingCompleted();
        });
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
     * Brief sends a message to the server asynchronously
     * @param message Object the message being sent
     */
    protected void sendMessage(Object message) {
        runAsync(() -> delegate.sendMessage(message));
    }

    /**
     * Brief getter for scene
     * @return T scene
     */
    public T scene() {
        return scene;
    }

    /**
     * Brief getter for delegate
     * @return S delegate
     */
    public S delegate() {
        return delegate;
    }


    /**
     * Brief getter for loaded state
     * @return Boolean loaded
     */
    public Boolean isLoaded() {
        return loaded;
    }

    /**
     * Brief true setter for loaded
     */
    public void loadingCompleted() {
        this.loaded = true;
    }
}
