package it.polimi.ingsw.santorini.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlockTest {
    Block block;
    static List<BlockType> blockTypes;

    @BeforeAll
    static void init() {
        blockTypes = Arrays.asList(BlockType.values());
    }

    @RepeatedTest(4)
    void getTypeTest(RepetitionInfo repetitionInfo) {
        BlockType testedBlockType = blockTypes.get(repetitionInfo.getCurrentRepetition() - 1);
        block = new Block(testedBlockType);
        assertEquals(testedBlockType, block.getType(), "Block getType() returned a different intValueOf than the initialized one");
    }

    @RepeatedTest(4)
    void blocksCellTest(RepetitionInfo repetitionInfo){
        BlockType testedBlockType = blockTypes.get(repetitionInfo.getCurrentRepetition() - 1);
        block = new Block(testedBlockType);
        if(!block.getType().equals(BlockType.DOME)) assertFalse(block.blocksCell());
        else assertTrue(block.blocksCell(), "the domes should make the cell unreachable");
    }
}