package it.polimi.ingsw.santorini.model.gameoperations.state;

import it.polimi.ingsw.santorini.model.Block;
import it.polimi.ingsw.santorini.model.BlockType;

/**
 * Brief Specialised state dedicated to the Build operation
 * @see GameOperationState
 * @see it.polimi.ingsw.santorini.model.gameoperations.Build
 */
public class BState extends GameOperationState {

    /**
     * Brief The chosen BlockType during block type selection process
     */
    private BlockType chosenBlockType;

    /**
     * Brief Getter for the chosenBlockType
     * @return BlockType
     */
    public BlockType getChosenBlockType() {
        return chosenBlockType;
    }

    /**
     * Brief returns a new Block of the chosenBlockType type
     * @return Block
     */
    public Block getChosenBlock() {
        return new Block(chosenBlockType);
    }

    /**
     * Brief Setter for the chosenBlockType
     * @param chosenBlockType the chosen BlockType
     */
    public void setChosenBlockType(BlockType chosenBlockType) {
        this.chosenBlockType = chosenBlockType;
    }
}
