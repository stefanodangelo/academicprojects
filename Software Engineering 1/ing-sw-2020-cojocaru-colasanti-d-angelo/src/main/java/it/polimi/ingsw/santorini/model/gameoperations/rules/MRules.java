package it.polimi.ingsw.santorini.model.gameoperations.rules;


import it.polimi.ingsw.santorini.model.GameMap;
import it.polimi.ingsw.santorini.model.Position;
import it.polimi.ingsw.santorini.model.gameoperations.GameMarker;
import it.polimi.ingsw.santorini.model.gameoperations.exceptions.ExpansionAllowanceException;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.*;
import it.polimi.ingsw.santorini.model.gameoperations.rules.applicable.ApplicableMRules;
import it.polimi.ingsw.santorini.model.gameoperations.rules.expandable.ExpandableMRules;
import it.polimi.ingsw.santorini.model.gameoperations.rules.factory.DefaultMoveFactory;
import it.polimi.ingsw.santorini.model.gameoperations.state.MState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Brief Move Rules are Rules dedicated entirely to the Move GameOperation
 * @see it.polimi.ingsw.santorini.model.gameoperations.Move
 */
public class MRules extends Rules<MState, MRules> implements ApplicableMRules, ExpandableMRules {

    /**
     * Brief The rule dedicated for the allowed selectable positions during a Move operation
     */
    private final ExpPredicate<MState, GameMarker> allowedPositions = new ExpPredicate<>();

    /**
     * Brief The rule dedicated for the win condition during a Move operation
     */
    private final ExpPredicate<MState, GameMarker> winCondition = new ExpPredicate<>();

    /**
     * Brief The rule dedicated for the movement action during a Move operation
     */
    private final ExpConsumer<MState, GameMarker> movement = new ExpConsumer<>();

    /**
     * Brief Default constructor that initializes default expandable rules
     */
    public MRules() {
        this(true);
    }

    /**
     * Brief Constructor that allows to directly load default expansions for all rules
     * @param loadDefaultRules Boolean true if default rules are wanted
     */
    public MRules(Boolean loadDefaultRules) {
        if (loadDefaultRules) loadDefaultRules();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MRules toRules() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExpPredicate<MState, GameMarker> allowedPositions() {
        return allowedPositions.asExpandableOnly();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExpPredicate<MState, GameMarker> winCondition() {
        return winCondition.asExpandableOnly();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExpConsumer<MState, GameMarker> movement() {
        return movement.asExpandableOnly();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Position> applyAllowedPositions() {
        List<Position> allowedPositionsResult = new ArrayList<>();
        List<Position> possiblePositions = GameMap.getNeighboringPositions(getState().activeWorkerPosition());
        possiblePositions.forEach(position -> {
            getState().setChosenPosition(position);
            if (allowedPositions.test(getState())) allowedPositionsResult.add(position);
        });
        return allowedPositionsResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean applyWinCondition() {
        return winCondition.test(getState());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyMovement() {
        movement.accept(getState());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadDefaultRules() {
        try {
            allowedPositions.merge(allowedPositionsDefault());
            winCondition.merge(winConditionDefault());
            movement.merge(movementDefault());
        } catch (ExpansionAllowanceException e) {
            e.printStackTrace();
        }
    }

    /**
     * Brief Provides the default allowedPositions rule
     * @return ExpPredicate the default allowedPositions rule
     */
    private ExpPredicate<MState, GameMarker> allowedPositionsDefault() {
        return DefaultMoveFactory.allowedPositionsDefault();
    }

    /**
     * Brief Provides the default winCondition rule
     * @return ExpPredicate the default winCondition rule
     */
    private ExpPredicate<MState, GameMarker> winConditionDefault() throws ExpansionAllowanceException {
        ExpPredicate<MState, GameMarker> victoryConditionDefault = new ExpPredicate<>();
        victoryConditionDefault.addExpansion(DefaultMoveFactory.winConditionDefaultExpansion());
        return victoryConditionDefault;
    }

    /**
     * Brief Provides the default movement rule
     * @return ExpConsumer the default movement rule
     */
    private ExpConsumer<MState, GameMarker> movementDefault() throws ExpansionAllowanceException {
        ExpConsumer<MState, GameMarker> movementDefault = new ExpConsumer<>();
        movementDefault.addExpansion(DefaultMoveFactory.movementDefaultExpansion());
        return movementDefault;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(MRules rules) {
        try {
            allowedPositions.merge(rules.allowedPositions);
            winCondition.merge(rules.winCondition);
            movement.merge(rules.movement);
        } catch (ExpansionAllowanceException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<Expandable<?, GameMarker, ?, ?>> getExpandableList() {
        return Arrays.asList(allowedPositions, winCondition, movement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void completeRules() {
        if (isComplete()) return;
        try {
            if (allowedPositions.isEmpty()) allowedPositions.merge(allowedPositionsDefault());
            if (winCondition.isEmpty()) winCondition.merge(winConditionDefault());
            if (movement.isEmpty()) movement.merge(movementDefault());
        } catch (ExpansionAllowanceException e) {
            e.printStackTrace();
        }
    }

    /**
     * Brief Returns Applicable instance of this
     * @return ApplicableMRules
     */
    public ApplicableMRules asApplicable() {
        setPermissions(null, true);
        return this;
    }
}
