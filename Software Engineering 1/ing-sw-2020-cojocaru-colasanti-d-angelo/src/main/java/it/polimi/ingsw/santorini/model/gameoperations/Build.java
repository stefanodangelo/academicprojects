package it.polimi.ingsw.santorini.model.gameoperations;

import it.polimi.ingsw.santorini.model.BlockType;
import it.polimi.ingsw.santorini.model.GameMap;
import it.polimi.ingsw.santorini.model.Position;
import it.polimi.ingsw.santorini.model.gameoperations.exceptions.RulesIncompleteException;
import it.polimi.ingsw.santorini.model.gameoperations.rules.BRules;
import it.polimi.ingsw.santorini.model.gameoperations.rules.applicable.ApplicableBRules;
import it.polimi.ingsw.santorini.model.gameoperations.rules.expandable.ExpandableRulesContainer;
import it.polimi.ingsw.santorini.model.gameoperations.state.BState;

import java.util.List;

/**
 * Brief Build GameOperation dedicated to the building phase of the game. It provides the rules needed by the game operation
 * in order to perform its task. Besides this also performs the block type selection and the building action.
 * It uses BRules and BState to obtain and execute the needed rules
 * @see GameOperation
 * @see BRules
 * @see BState
 */
public class Build extends GameOperation<BState, ApplicableBRules> {

    /**
     * Brief Constructor for default rules
     */
    public Build() {
        super(new BState(), new BRules());
    }

    /**
     * Brief Constructor for optional operation with default rules
     * @param isOptional Boolean true if the operation must be optional
     */
    public Build(Boolean isOptional) {
        super(new BState(), new BRules(), isOptional);
    }

    /**
     * Brief Constructor for optional operation with custom rules
     * @param rules ApplicableBRules
     * @param isOptional Boolean true if the operation must be optional
     * @throws RulesIncompleteException if the provided rules are not complete
     * @see RulesIncompleteException
     */
    public Build(ApplicableBRules rules, Boolean isOptional) throws RulesIncompleteException {
        super(new BState(), rules, isOptional);
    }

    /**
     * Brief Constructor for operation with custom rules
     * @param rules ApplicableBRules
     * @throws RulesIncompleteException if the provided rules are not complete
     * @see RulesIncompleteException
     */
    public Build(ApplicableBRules rules) throws RulesIncompleteException {
        super(new BState(), rules);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeCustom() {
        blockTypeSelection();
        build();
        delegate().onBoardChanged(GameMap.getBoard(), null);
    }

    /**
     * Brief Performs the BlockType selection
     */
    private void blockTypeSelection() {
        List<BlockType> allowedBlockTypes = rules.applyAllowedBlockTypes();
        if (allowedBlockTypes.size() > 1) {
            delegate().onBoardChanged(GameMap.getBoard(), null);
            delegate().blockTypeSelection(gameState().getCurrentPlayer().getId(), allowedBlockTypes, this::onBlockTypeSelected);
        }
        else if (allowedBlockTypes.size() == 1) state.setChosenBlockType(allowedBlockTypes.get(0));
    }

    /**
     * Brief Handles the chosen BlockType
     * @param blockType The chosen BlockType
     */
    private void onBlockTypeSelected(BlockType blockType) {
        state.setChosenBlockType(blockType);
    }

    /**
     * Brief Performs the building
     */
    private void build() {
        rules.applyBuilding();
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
        this.rules.merge(expansionRules.bRules());
    }
}
