package it.polimi.ingsw.santorini.model;

import it.polimi.ingsw.santorini.model.exceptions.IncorrectBlockTypeException;

/**
 * Brief A Block is a stackable object of the game that follows the pattern described in BlockType
 *       It's NOT possible to stack a block on a worker
 */
public class Block extends GameObject {
    private final BlockType type;

    public Block(BlockType type) {
        super();
        this.type = type;
    }

    /**
     * Brief Creates a block checking if the given level corresponds to a valid type of block
     * @param level is the level of the block being created
     */
    public Block(Integer level) {
        super();
        if(level < BlockType.LEVEL1.ordinal() || level > BlockType.DOME.ordinal()) throw new IncorrectBlockTypeException();
        this.type = BlockType.typeByLevel(level);
    }

    public BlockType getType() {
        return type;
    }

    /**
     * Brief overriden from Cellable
     * @return true only if the GameObject can occupy the Cell it is in, that is if it is a Worker
     */
    @Override
    public Boolean occupiesCell() {
        return false;
    }

    /**
     * Brief overriden from Cellable
     * @return worker's owner's Id if it is a Worker, otherwise null
     */
    @Override
    public Integer occupantId() {
        return null;
    }

    /**
     * Brief overriden from Cellable
     * @return true if the GameObject is a Dome type Block, otherwise false
     */
    @Override
    public Boolean blocksCell() {
        return type.equals(BlockType.DOME);
    }

    /**
     * Brief overriden from Cellable
     * @return the height at which the Cell is set by pushed GameObjects
     */
    @Override
    public Integer correspondingLevel() {
        return type.ordinal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return type == block.type;
    }

    @Override
    public String toString() {
        return "Block{" +
                "type=" + type +
                '}';
    }
}
