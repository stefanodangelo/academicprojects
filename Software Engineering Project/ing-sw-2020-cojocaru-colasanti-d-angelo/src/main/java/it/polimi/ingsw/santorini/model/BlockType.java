package it.polimi.ingsw.santorini.model;

/**
 * Brief Enumeration helpful to represent the type of a Cellable block
 */
public enum BlockType {
    GROUND,
    LEVEL1,
    LEVEL2,
    LEVEL3,
    DOME;

    /**
     * @param level is the level of the wanted type of block
     * @return the type of block corresponding to the level passed as a parameter
     */
    public static BlockType typeByLevel(int level) {
        return BlockType.values()[level];
    }
}
