package it.polimi.ingsw.santorini.view.gui.controllers.delegates;

/**
 * Brief the interface used as a delegate by a GUISceneController
 */
public interface GUISceneControllerDelegate {

    /**
     * Brief shows the Lobby scene
     */
    void showLobbyScene();

    /**
     * Brief shows the Setup scene
     */
    void showSetupScene();

    /**
     * Brief shows the Game scene
     */
    void showGameScene();

    /**
     * Brief shows the Settings scene
     */
    void showSettingsScene();

    /**
     * Brief shows the Home scene
     */
    void showHomeScene();

    /**
     * Brief sends a message to the server
     * @param message LobbyMessage message being sent
     */
    void sendMessage(Object message);
}
