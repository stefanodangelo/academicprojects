package it.polimi.ingsw.santorini.view.gui.scenes.delegates;

import it.polimi.ingsw.santorini.communication.ImmutablePosition;

/**
 * Brief the game scene delegate
 * @see it.polimi.ingsw.santorini.view.gui.scenes.GameScene
 */
public interface GameSceneDelegate extends GUISceneDelegate {
    /**
     * Brief called on position selected
     * @param position the selected position
     */
    void onPositionSelected(ImmutablePosition position);

    /**
     * Brief called on skip request choice
     * @param skip the skip choice
     */
    void onSkipOrDoAnswer(Boolean skip);

    /**
     * Brief called on undo choice
     * @param undo the undo choice
     */
    void onUndoAnswer(Boolean undo);

    /**
     * Brief called on block selection
     * @param choice the chosen block (in ordinal order)
     */
    void onBlockSelected(Integer choice);
}
