package it.polimi.ingsw.santorini.model.gameoperations.rules.expandable;

import it.polimi.ingsw.santorini.model.BlockType;
import it.polimi.ingsw.santorini.model.Position;
import it.polimi.ingsw.santorini.model.gameoperations.GameMarker;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.*;
import it.polimi.ingsw.santorini.model.gameoperations.rules.BRules;
import it.polimi.ingsw.santorini.model.gameoperations.state.BState;
import it.polimi.ingsw.santorini.model.gameoperations.state.MState;

/**
 * Brief Dedicated ExpandableRules for the Build operation
 * @see it.polimi.ingsw.santorini.model.gameoperations.Build
 */
public interface ExpandableBRules extends ExpandableRules<BState, BRules> {
    /**
     * Brief The expandable allowedPositions Build rule
     * @return the expandable allowedPositions Build rule
     */
    ExpPredicate<BState, GameMarker> allowedPositions();

    /**
     * Brief The expandable allowedBlockType Build rule
     * @return the expandable allowedBlockType Build rule
     */
    ExpListFunction<BState, BlockType, GameMarker> allowedBlockTypes();

    /**
     * Brief The expandable building Build rule
     * @return the expandable allowedBlockType Build rule
     */
    ExpConsumer<BState, GameMarker> building();
}
