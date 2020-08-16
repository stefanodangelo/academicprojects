package it.polimi.ingsw.santorini.view.gui.controllers.delegates;

/**
 * Brief the interface used as a delegate by a SettingsSceneController
 */
public interface SettingsSceneControllerDelegate extends GUISceneControllerDelegate{
    void onConnectionParametersChanged (String IP, Integer port);
}
