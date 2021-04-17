package it.polimi.ingsw.santorini.view.gui.controllers;

import it.polimi.ingsw.santorini.view.gui.controllers.delegates.HomeSceneControllerDelegate;
import it.polimi.ingsw.santorini.view.gui.scenes.HomeScene;
import it.polimi.ingsw.santorini.view.gui.scenes.delegates.HomeSceneDelegate;

/**
 * Brief Controller for the home scene
 */
public class HomeSceneController extends GUISceneController<HomeScene, HomeSceneControllerDelegate> implements HomeSceneDelegate {

    public HomeSceneController(HomeSceneControllerDelegate delegate){
        super(new HomeScene(), delegate);
        scene().setDelegate(this);
        loadScene();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSettingsPressed() {
        delegate().showSettingsScene();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart() {
        delegate().showLobbyScene();
        runAsync(() -> delegate().startGame());
    }
}
