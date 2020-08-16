package it.polimi.ingsw.santorini.view.gui.controllers;

import it.polimi.ingsw.santorini.view.gui.controllers.delegates.SettingsSceneControllerDelegate;
import it.polimi.ingsw.santorini.view.gui.scenes.SettingsScene;
import it.polimi.ingsw.santorini.view.gui.scenes.delegates.SettingsSceneDelegate;

import java.util.function.Consumer;

/**
 * Brief Sets the hostname and port for the connection with the server
 */
public class SettingsSceneController extends GUISceneController<SettingsScene, SettingsSceneControllerDelegate> implements SettingsSceneDelegate {

    private Consumer<String> IPRequest;
    private Consumer<Integer> portRequest;

    public SettingsSceneController(SettingsSceneControllerDelegate delegate) {
        super(new SettingsScene(), delegate);
        scene().setDelegate(this);
        loadScene();
        loadingCompleted();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDonePressed() {
        runSafely(() ->delegate().showHomeScene());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onApplyPressed(String IP, Integer port){
        runAsync(() -> {
            delegate().onConnectionParametersChanged(IP, port);
        });
    }
}
