package it.polimi.ingsw.santorini.view.gui.scenes.delegates;

public interface LobbySceneDelegate extends GUISceneDelegate {
    /**
     * Brief called on exit
     */
    void onExit();

    /**
     * Brief called when max number of players has been changed
     * @param numberOfPlayers Integer max number of players
     */
    void onNumberOfPlayerChanged(Integer numberOfPlayers);

    /**
     * Brief called when game starts
     */
    void onStart();

    /**
     * Brief called when name has been chosen
     * @param name String name
     */
    void onNameConfirmed(String name);
}
