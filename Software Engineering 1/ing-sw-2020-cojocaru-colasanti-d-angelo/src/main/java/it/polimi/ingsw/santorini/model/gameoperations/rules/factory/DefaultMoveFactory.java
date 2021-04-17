package it.polimi.ingsw.santorini.model.gameoperations.rules.factory;

import it.polimi.ingsw.santorini.model.GameMap;
import it.polimi.ingsw.santorini.model.Position;
import it.polimi.ingsw.santorini.model.gameoperations.GameMarker;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.ConsumerExpansion;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.ExpPredicate;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.PredicateExpansion;
import it.polimi.ingsw.santorini.model.gameoperations.state.MState;

/**
 * Brief The factory for Move Rules and their expansions
 */
public abstract class DefaultMoveFactory {

    /**
     * Brief the index enum that stores the order that default expansions are added
     */
    public enum Index {
        NoCurrentPosition(0),
        NoOccupied(1),
        NoBlocked(2),
        OneLevelUp(3);

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
    public static ExpPredicate<MState, GameMarker> allowedPositionsDefault() {
        ExpPredicate<MState, GameMarker> allowedPositionsDefault = new ExpPredicate<>();

        allowedPositionsDefault.addExpansion(new PredicateExpansion<>(DefaultMoveFactory::allowedPositionsPredicate1,
                true, GameMarker.defaultMarker()));

        allowedPositionsDefault.addExpansion(new PredicateExpansion<>(DefaultMoveFactory::allowedPositionsPredicate2,
                false, GameMarker.defaultMarker()));

        allowedPositionsDefault.addExpansion(new PredicateExpansion<>(DefaultMoveFactory::allowedPositionsPredicate3,
                false, GameMarker.defaultMarker()));

        allowedPositionsDefault.addExpansion(new PredicateExpansion<>(DefaultMoveFactory::allowedPositionsPredicate4,
                false, GameMarker.defaultMarker()));

        return allowedPositionsDefault;
    }

    /**
     * Brief The predicate for allowedPosition rule added as the default expansion in position 1.
     * Makes sure that the chosen position will be different from the current one
     * @param s the state
     * @return the result
     */
    public static Boolean allowedPositionsPredicate1(MState s) {
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
    public static Boolean allowedPositionsPredicate2(MState s) {
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
    public static Boolean allowedPositionsPredicate3(MState s) {
        Position finalPos = s.chosenPosition();
        GameMap map = s.map();
        return !map.cellAt(finalPos).isBlocked();
    }

    /**
     * Brief The predicate for allowedPosition rule added as the default expansion in position 4
     * Makes sure that the chosen position will be at a height that differs of a maximum of one level
     * @param s the state
     * @return the result
     */
    public static Boolean allowedPositionsPredicate4(MState s) {
        Position initialPos = s.activeWorkerPosition();
        Position finalPos = s.chosenPosition();
        GameMap map = s.map();
        return map.getLevelDifference(finalPos, initialPos) <= 1;
    }

    /**
     * Brief The default Expansion for winCondition rule.
     * @return PredicateExpansion
     */
    public static PredicateExpansion<MState, GameMarker> winConditionDefaultExpansion() {
        return new PredicateExpansion<>(DefaultMoveFactory::winConditionDefaultPredicate,
                true, GameMarker.defaultMarker());
    }

    /**
    $ * Brief The predicate for winCondition rule added as the default expansion
     * Makes sure that the current player wins if he goes up one level and finds himself on 3rd level
     * @param s the state
     * @return the result
     */
    public static Boolean winConditionDefaultPredicate(MState s) {
        Position initialPos = s.activeWorkerPosition();
        Position finalPos = s.chosenPosition();
        GameMap map = s.map();
        return map.cellAt(finalPos).getLevel().equals(3) &&
                map.getLevelDifference(finalPos, initialPos).equals(1);
    }

    /**
     * Brief The default Expansion for movement rule.
     * @return ConsumerExpansion
     */
    public static ConsumerExpansion<MState, GameMarker> movementDefaultExpansion() {
        return new ConsumerExpansion<>(DefaultMoveFactory::movementDefaultConsumer,
                GameMarker.defaultMarker());
    }

    /**
     * Brief The consumer for movement rule added as the default expansion
     * Makes sure that the active worker moves on the chosen position
     * @param s the state
     */
    public static void movementDefaultConsumer(MState s) {
        s.map().move(s.activeWorkerPosition(), s.chosenPosition());
    }
}
