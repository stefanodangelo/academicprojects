package it.polimi.ingsw.santorini.model.gameoperations;

import it.polimi.ingsw.santorini.model.GameMap;
import it.polimi.ingsw.santorini.model.Position;
import it.polimi.ingsw.santorini.model.gameoperations.exceptions.RulesIncompleteException;
import it.polimi.ingsw.santorini.model.gameoperations.rules.MRules;
import it.polimi.ingsw.santorini.model.gameoperations.rules.applicable.ApplicableMRules;
import it.polimi.ingsw.santorini.model.gameoperations.rules.expandable.ExpandableRulesContainer;
import it.polimi.ingsw.santorini.model.gameoperations.state.MState;

import java.util.List;

/**
 * Brief Move GameOperation dedicated to the movement phase of the game. It provides the rules needed by the game operation
 * in order to perform its task. Besides this also performs the win check and the movement action.
 * It uses MRules and MState to obtain and execute the needed rules
 * @see GameOperation
 * @see MRules
 * @see MState
 */
public class Move extends GameOperation<MState, ApplicableMRules> {

    /**
     * Brief Constructor for default rules
     */
    public Move() {
        super(new MState(), new MRules());
    }

    /**
     * Brief Constructor for optional operation with default rules
     * @param isOptional Boolean true if the operation must be optional
     */
    public Move(Boolean isOptional) {
        super(new MState(), new MRules(), isOptional);
    }

    /**
     * Brief Constructor for optional operation with custom rules
     * @param rules ApplicableBRules
     * @param isOptional Boolean true if the operation must be optional
     * @throws RulesIncompleteException if the provided rules are not complete
     * @see RulesIncompleteException
     */
    public Move(ApplicableMRules rules, Boolean isOptional) throws RulesIncompleteException {
        super(new MState(), rules, isOptional);
    }

    /**
     * Brief Constructor for operation with custom rules
     * @param rules ApplicableBRules
     * @throws RulesIncompleteException if the provided rules are not complete
     * @see RulesIncompleteException
     */
    public Move(ApplicableMRules rules) throws RulesIncompleteException {
        super(new MState(), rules);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeCustom() {
        winCheck();
        move();
    }

    /**
     * Brief Performs the win check
     */
    private void winCheck() {
        winCheck(rules.applyWinCondition());
    }

    /**
     * Brief Performs the movement
     */
    private void move() {
        rules.applyMovement();
        delegate().onBoardChanged(GameMap.getBoard(), null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<Position> allowedPositions() {
        return rules.applyAllowedPositions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void expandRules(ExpandableRulesContainer expansionRules) {
        super.expandRules(expansionRules);
        this.rules.merge(expansionRules.mRules());
    }
}
