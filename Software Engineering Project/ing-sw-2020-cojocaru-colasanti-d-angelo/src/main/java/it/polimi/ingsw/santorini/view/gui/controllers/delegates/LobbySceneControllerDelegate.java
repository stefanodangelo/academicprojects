package it.polimi.ingsw.santorini.view.gui.controllers.delegates;

/**
 * Brief the interface used as a delegate by a LobbySceneController
 */
public interface LobbySceneControllerDelegate extends GUISceneControllerDelegate {
    /**
     * Brief called on chosen name
     * @param name the name
     */
    void onChosenName(String name);
}
