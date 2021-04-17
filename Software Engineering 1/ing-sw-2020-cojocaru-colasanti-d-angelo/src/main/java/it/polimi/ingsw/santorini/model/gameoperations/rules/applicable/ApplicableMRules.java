package it.polimi.ingsw.santorini.model.gameoperations.rules.applicable;


import it.polimi.ingsw.santorini.model.Position;
import it.polimi.ingsw.santorini.model.gameoperations.rules.MRules;
import it.polimi.ingsw.santorini.model.gameoperations.state.MState;

import java.util.List;

/**
 * Brief Dedicated ApplicableRules for the Move operation
 * @see it.polimi.ingsw.santorini.model.gameoperations.Move
 */
public interface ApplicableMRules extends ApplicableRules<MState, MRules> {
    /**
     * Brief The application method for the allowedPositions Move rule
     * @return list of allowed positions for the active worker movement
     */
    List<Position> applyAllowedPositions();

    /**
     * Brief The application method for the winCondition Move rule
     * @return Boolean true if the current player won through the movement, false otherwise
     */
    Boolean applyWinCondition();

    /**
     * Brief The application method for the movement Move rule. It actually produces movement effects on the game state
     */
    void applyMovement();
}
