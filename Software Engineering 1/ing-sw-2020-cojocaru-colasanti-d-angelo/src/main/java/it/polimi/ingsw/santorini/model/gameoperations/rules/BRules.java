package it.polimi.ingsw.santorini.model.gameoperations.rules;

import it.polimi.ingsw.santorini.model.BlockType;
import it.polimi.ingsw.santorini.model.GameMap;
import it.polimi.ingsw.santorini.model.Position;
import it.polimi.ingsw.santorini.model.gameoperations.GameMarker;
import it.polimi.ingsw.santorini.model.gameoperations.exceptions.ExpansionAllowanceException;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.*;
import it.polimi.ingsw.santorini.model.gameoperations.rules.applicable.ApplicableBRules;
import it.polimi.ingsw.santorini.model.gameoperations.rules.expandable.ExpandableBRules;
import it.polimi.ingsw.santorini.model.gameoperations.rules.factory.DefaultBuildFactory;
import it.polimi.ingsw.santorini.model.gameoperations.state.BState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Brief Build Rules are Rules dedicated entirely to the Build GameOperation
 * @see it.polimi.ingsw.santorini.model.gameoperations.Build
 */
public class BRules extends Rules<BState, BRules> implements ApplicableBRules, ExpandableBRules {

    /**
     * Brief The rule dedicated for the allowed selectable positions during a Move operation
     */
    private final ExpPredicate<BState, GameMarker> allowedPositions = new ExpPredicate<>();

    /**
     * Brief The rule dedicated for the allowed selectable block types during a Build operation
     */
    private final ExpListFunction<BState, BlockType, GameMarker> allowedBlockTypes = new ExpListFunction<>();

    /**
     * Brief The rule dedicated for the building action during a Build operation
     */
    private final ExpConsumer<BState, GameMarker> building = new ExpConsumer<>();

    /**
     * Brief Default constructor that initializes default expandable rules
     */
    public BRules() {
        this(true);
    }

    /**
     * Brief Constructor that allows to directly load default expansions for all rules
     * @param loadDefaultRules Boolean true if default rules are wanted
     */
    public BRules(Boolean loadDefaultRules) { if (loadDefaultRules) loadDefaultRules(); }

    /**
     * {@inheritDoc}
     */
    @Override
    public BRules toRules() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExpPredicate<BState, GameMarker> allowedPositions() {
        return allowedPositions.asExpandableOnly();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExpListFunction<BState, BlockType, GameMarker> allowedBlockTypes() {
        return allowedBlockTypes.asExpandableOnly();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExpConsumer<BState, GameMarker> building() {
        return building.asExpandableOnly();
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
    public List<BlockType> applyAllowedBlockTypes() {
        return allowedBlockTypes.apply(getState());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyBuilding() {
        building.accept(getState());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadDefaultRules() {
        try {
            allowedPositions.merge(allowedPositionsDefault());
            allowedBlockTypes.merge(allowedBlockTypesDefault());
            building.merge(buildingDefault());
        } catch (ExpansionAllowanceException e) {
            e.printStackTrace();
        }
    }

    /**
     * Brief Provides the default allowedPositions rule
     * @return ExpPredicate the default allowedPositions rule
     */
    private ExpPredicate<BState, GameMarker> allowedPositionsDefault() {
        return DefaultBuildFactory.allowedPositionsDefault();
    }

    /**
     * Brief Provides the default allowedBlockTypes rule
     * @return ExpListFunction the default allowedBlockTypes rule
     */
    private ExpListFunction<BState, BlockType, GameMarker> allowedBlockTypesDefault() throws ExpansionAllowanceException {
        ExpListFunction<BState, BlockType, GameMarker> allowedBlockTypesDefault = new ExpListFunction<>();
        allowedBlockTypesDefault.addExpansion(DefaultBuildFactory.allowedBlockTypesDefaultExpansion());
        return allowedBlockTypesDefault;
    }

    /**
     * Brief Provides the default building rule
     * @return ExpConsumer the default building rule
     */
    private ExpConsumer<BState, GameMarker> buildingDefault() throws ExpansionAllowanceException {
        ExpConsumer<BState, GameMarker> buildingDefault = new ExpConsumer<>();
        buildingDefault.addExpansion(DefaultBuildFactory.buildingDefaultExpansion());
        return buildingDefault;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(BRules rules) {
        try {
            allowedPositions.merge(rules.allowedPositions);
            allowedBlockTypes.merge(rules.allowedBlockTypes);
            building.merge(rules.building);
        } catch (ExpansionAllowanceException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<Expandable<?, GameMarker, ?, ?>> getExpandableList() {
        return Arrays.asList(allowedPositions, allowedBlockTypes, building);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void completeRules() {
        if (isComplete()) return;
        try {
            if (allowedPositions.isEmpty()) allowedPositions.merge(allowedPositionsDefault());
            if (allowedBlockTypes.isEmpty()) allowedBlockTypes.merge(allowedBlockTypesDefault());
            if (building.isEmpty()) building.merge(buildingDefault());
        } catch (ExpansionAllowanceException e) {
            e.printStackTrace();
        }
    }

    /**
     * Brief Returns Applicable instance of this
     * @return ApplicableBRules
     */
    public ApplicableBRules asApplicable() {
        setPermissions(null, true);
        return this;
    }
}
