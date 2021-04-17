package it.polimi.ingsw.santorini.model;

/**
 * Brief Interface that provides the methods necessary for GameObjects like blocks and workers to add themselves on top of a tile
 */

public interface Cellable {
    Boolean occupiesCell();
    Integer occupantId();
    Boolean blocksCell();
    Integer correspondingLevel();
}
