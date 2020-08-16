package it.polimi.ingsw.santorini.model.gameoperations.rules.factory;

import it.polimi.ingsw.santorini.model.BlockType;
import it.polimi.ingsw.santorini.model.GameMap;
import it.polimi.ingsw.santorini.model.Position;
import it.polimi.ingsw.santorini.model.gameoperations.GameMarker;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.ConsumerExpansion;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.ExpPredicate;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.ListFunctionExpansion;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.PredicateExpansion;
import it.polimi.ingsw.santorini.model.gameoperations.state.BState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Brief The factory for Build Rules and their expansions
 */
public abstract class DefaultBuildFactory {

    /**
     * Brief the index enum that stores the order that default expansions are added
     */
    public enum Index {
        NoCurrentPosition(0),
        NoOccupied(1),
        NoBlocked(2);

        /**
         * Brief index
         */
        private final Integer index;

        /**
         * Brief getter for index
         * @return index
         */
        public Integer getIndex() {
            return index;
        }

        /**
         * Brief main constructor that associates an index
         * @param index index
         */
        Index(Integer index) {
            this.index = index;
        }
    }

    /**
     * Brief The default Expandable for allowedPositions rule
     * @return ExpPredicate
     */
    public static ExpPredicate<BState, GameMarker> allowedPositionsDefault() {
        ExpPredicate<BState, GameMarker> allowedPositionsDefault = new ExpPredicate<>();

        allowedPositionsDefault.addExpansion(new PredicateExpansion<>(DefaultBuildFactory::allowedPositionsPredicate1,
                true, GameMarker.defaultMarker()));

        allowedPositionsDefault.addExpansion(new PredicateExpansion<>(DefaultBuildFactory::allowedPositionsPredicate2,
                false, GameMarker.defaultMarker()));

        allowedPositionsDefault.addExpansion(new PredicateExpansion<>(DefaultBuildFactory::allowedPositionsPredicate3,
                false, GameMarker.defaultMarker()));

        return allowedPositionsDefault;
    }

    /**
     * Brief The predicate for allowedPosition rule added as the default expansion in position 1.
     * Makes sure that the chosen position will be different from the current one
     * @param s the state
     * @return the result
     */
    public static Boolean allowedPositionsPredicate1(BState s) {
        Position initialPos = s.activeWorkerPosition();
        Position finalPos = s.chosenPosition();
        return !finalPos.equals(initialPos);
    }

    /**
     * Brief The predicate for allowedPosition rule added as the default expansion in position 2.
     * Makes sure that the chosen position will not be occupied
     * @param s the state
     * @return the result
     */
    public static Boolean allowedPositionsPredicate2(BState s) {
        Position finalPos = s.chosenPosition();
        GameMap map = s.map();
        return !map.cellAt(finalPos).isOccupied();
    }

    /**
     * Brief The predicate for allowedPosition rule added as the default expansion in position 3.
     * Makes sure that the chosen position will not be blocked
     * @param s the state
     * @return the result
     */
    public static Boolean allowedPositionsPredicate3(BState s) {
        Position finalPos = s.chosenPosition();
        GameMap map = s.map();
        return !map.cellAt(finalPos).isBlocked();
    }

    /**
     * Brief The default Expansion for allowedBlockTypes rule.
     * @return ListFunctionExpansion
     */
    public static ListFunctionExpansion<BState, BlockType, GameMarker> allowedBlockTypesDefaultExpansion() {
        return new ListFunctionExpansion<>(DefaultBuildFactory::allowedBlockTypesDefaultListFunction,
                true, GameMarker.defaultMarker());
    }

    /**
     * Brief The function for allowedBlockTypes rule added as the default expansion.
     * Makes sure that the chosen block type will be the next one available for the chosen position
     * @param s the state
     * @return the default Block that can be added
     */
    public static List<BlockType> allowedBlockTypesDefaultListFunction(BState s) {
        int nextLevel = s.map().cellAt(s.chosenPosition()).getLevel() + 1;
        return new ArrayList<>(Collections.singletonList(BlockType.typeByLevel(nextLevel)));
    }

    /**
     * Brief The default Expansion for building rule.
     * @return PredicateExpansion
     */
    public static ConsumerExpansion<BState, GameMarker> buildingDefaultExpansion() {
        return new ConsumerExpansion<>(DefaultBuildFactory::buildingDefaultConsumer,
                GameMarker.defaultMarker());
    }

    /**
     * Brief The consumer for building rule added as the default expansion
     * Makes sure that the active worker builds the next available block type on the chosen position
     * @param s the state
     */
    public static void buildingDefaultConsumer(BState s) {
        s.map().addGameObject(s.getChosenBlock(), s.chosenPosition());
    }
}
