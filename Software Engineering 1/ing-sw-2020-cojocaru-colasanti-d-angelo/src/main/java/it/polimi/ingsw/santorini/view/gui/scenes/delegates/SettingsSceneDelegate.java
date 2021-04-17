package it.polimi.ingsw.santorini.view.gui.scenes.delegates;

/**
 * Brief This interface offers methods to the SettingsScene and is implemented by SettingsSceneController
 */
public interface SettingsSceneDelegate extends GUISceneDelegate{
    /**
     * Brief Brings back to the HomeScene
     */
    void onDonePressed();

    /**
     * Brief Communicates IP and Port to the server
     * @param IP is the IP chosen by the user
     * @param port is the port chosen by the user
     */
    void onApplyPressed(String IP, Integer port);
}
