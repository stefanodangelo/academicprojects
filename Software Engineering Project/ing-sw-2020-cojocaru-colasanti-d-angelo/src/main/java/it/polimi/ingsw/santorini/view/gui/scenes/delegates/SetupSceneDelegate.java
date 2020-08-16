package it.polimi.ingsw.santorini.view.gui.scenes.delegates;

import it.polimi.ingsw.santorini.view.Color;

import java.util.List;

public interface SetupSceneDelegate extends GUISceneDelegate {
    /**
     * Brief called on choice
     * @param choice the choice codified as integer
     */
    void onChoiceSelected(Integer choice);

    /**
     * Brief called on color selection
     * @param choice the choice codified as integer
     * @param workersColors the available colors
     */
    void onColorSelected(Integer choice, List<Color> workersColors);

    /**
     * Brief called on card selection
     * @param card the card codified as integer
     */
    void onCardSelected(Integer card);

    /**
     * Brief called on card deselection
     * @param card the card codified as integer
     */
    void onCardUnselected(Integer card);

    /**
     * Brief called on cards confirmation
     */
    void onCardsConfirmed();
}
