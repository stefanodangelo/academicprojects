package it.polimi.ingsw.santorini.view.gui.scenes.delegates;

/**
 * Brief This interface offers methods to the HomeScene and is implemented by HomeSceneController
 */
public interface HomeSceneDelegate extends GUISceneDelegate{
    /**
     * Brief Handles the transition from HomeScene to SettingsScene
     */
    void onSettingsPressed();

    /**
     * Brief Connects the client to the server and transitions to the LobbyScene
     */
    void onStart();
}

