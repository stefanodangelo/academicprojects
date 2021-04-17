package it.polimi.ingsw.santorini.view.gui.scenes.utils;

import it.polimi.ingsw.santorini.communication.ImmutablePosition;

public interface MapViewDelegate {
    void onPositionSelected(ImmutablePosition position);
}
