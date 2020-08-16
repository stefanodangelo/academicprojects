package it.polimi.ingsw.santorini.model.gameoperations.rules.expandable;

import it.polimi.ingsw.santorini.model.gameoperations.GameMarker;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.*;
import it.polimi.ingsw.santorini.model.gameoperations.rules.MRules;
import it.polimi.ingsw.santorini.model.gameoperations.state.MState;

/**
 * Brief Dedicated ExpandableRules for the Move operation
 * @see it.polimi.ingsw.santorini.model.gameoperations.Move
 */
public interface ExpandableMRules extends ExpandableRules<MState, MRules> {
    /**
     * Brief The expandable allowedPositions Move rule
     * @return the expandable allowedPositions Move rule
     */
    ExpPredicate<MState, GameMarker> allowedPositions();

    /**
     * Brief The expandable winCondition Move rule
     * @return the expandable winCondition Move rule
     */
    ExpPredicate<MState, GameMarker> winCondition();

    /**
     * Brief The expandable movement Move rule
     * @return The expandable movement Move rule
     */
    ExpConsumer<MState, GameMarker> movement();
}
