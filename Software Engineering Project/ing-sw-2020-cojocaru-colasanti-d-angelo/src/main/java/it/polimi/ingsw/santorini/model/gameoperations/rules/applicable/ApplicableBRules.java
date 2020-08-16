package it.polimi.ingsw.santorini.model.gameoperations.rules.applicable;

import it.polimi.ingsw.santorini.model.BlockType;
import it.polimi.ingsw.santorini.model.Player;
import it.polimi.ingsw.santorini.model.Position;
import it.polimi.ingsw.santorini.model.gameoperations.rules.BRules;
import it.polimi.ingsw.santorini.model.gameoperations.state.BState;

import java.util.List;

/**
 * Brief Dedicated ApplicableRules for the Build operation
 * @see it.polimi.ingsw.santorini.model.gameoperations.Build
 */
public interface ApplicableBRules extends ApplicableRules<BState, BRules> {

    /**
     * Brief The application method for the allowedPositions Build rule
     * @return the list of allowed positions for the building process
     */
    List<Position> applyAllowedPositions();

    /**
     * Brief The application method for the allowedBlockTypes Build rule
     * @return the list of allowed block types for the building process
     */
    List<BlockType> applyAllowedBlockTypes();

    /**
     * Brief The application method for the building Build rule. It actually produces building effects on the game state
     */
    void applyBuilding();
}
