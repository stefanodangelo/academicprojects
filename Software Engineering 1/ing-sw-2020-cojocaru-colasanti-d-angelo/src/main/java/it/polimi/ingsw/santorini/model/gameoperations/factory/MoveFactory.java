package it.polimi.ingsw.santorini.model.gameoperations.factory;

import it.polimi.ingsw.santorini.model.Cell;
import it.polimi.ingsw.santorini.model.GameMap;
import it.polimi.ingsw.santorini.model.GameObject;
import it.polimi.ingsw.santorini.model.Position;
import it.polimi.ingsw.santorini.model.gameoperations.GameMarker;
import it.polimi.ingsw.santorini.model.gameoperations.Move;
import it.polimi.ingsw.santorini.model.gameoperations.custom.GameOperationParameters;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.ConsumerExpansion;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.PredicateExpansion;
import it.polimi.ingsw.santorini.model.gameoperations.rules.MRules;
import it.polimi.ingsw.santorini.model.gameoperations.rules.factory.DefaultMoveFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Brief MoveFactory for generating cards effects loadable from external file
 */
public abstract class MoveFactory {

    /**
     * Brief the dictionary of the Move effects
     */
    private static Map<String, Function<GameOperationParameters, Move>> moveEffects;

    /**
     * Brief tells if the dictionary has been already loaded
     */
    private static Boolean loaded = false;

    /**
     * Brief Enum that collects each effect with its corresponding rules generation function
     */
    public enum MoveEffect {
        DefaultMove("defaultMove", MoveFactory::defaultMove),
        OccupiedPositionsToo("occupiedPositionsToo", MoveFactory::occupiedPositionsToo),
        StoreInitialPosition("storeInitialPosition", MoveFactory::storeInitialPosition),
        ExcludingStoredPosition("excludingStoredPosition", MoveFactory::excludingStoredPosition),
        TheOnlyOneMovingUp("theOnlyOneMovingUp", MoveFactory::theOnlyOneMovingUp),
        ForceEnemyBackwards("forceEnemyBackwards", MoveFactory::forceEnemyBackwards),
        WinIfDownTwoLevels("winIfDownTwoLevels", MoveFactory::winIfDownTwoLevels),
        NoWinIfEnemyOnPerimeter("noWinIfEnemyOnPerimeter", MoveFactory::noWinIfEnemyOnPerimeter)
        ;

        /**
         * Brief the name of the effect
         */
        private final String name;

        /**
         * Brief the rules' generation function
         */
        private final Consumer<MRules> effect;

        /**
         * Brief Main constructor for name and rules' generation function
         * @param name the name of the effect
         * @param effect the rules' generation function
         */
        MoveEffect(String name, Consumer<MRules> effect) {
            this.name = name;
            this.effect = effect;
        }
    }

    /**
     * Brief loads all the effects contained in the MoveEffect enum
     */
    public static void loadEffects() {
        moveEffects = new HashMap<>();
        new ArrayList<>(Arrays.asList(MoveEffect.values())).forEach(effect ->
                moveEffects.put(effect.name, getCustomEffect(effect.effect)));
    }

    /**
     * Brief Returns the requested by name effect
     * @param name the effect name
     * @param params the customization parameters
     * @return the customized Move
     */
    public static Move getMove(String name, GameOperationParameters params) {
        if (!loaded) {
            loadEffects();
            loaded = true;
        }
        return moveEffects.get(name).apply(params);
    }

    /**
     * Brief Return the default Move with default rules
     * @return Move the default Move
     */
    public static Move getDefaultMove() {
        Move op = new Move();
        op.requiresWorkerSelection();
        return op;
    }

    /**
     * Brief Returns a customizable effect from a effect applier
     * @param effectApplier The effect applier
     * @return the customizable effect
     */
    public static Function<GameOperationParameters, Move> getCustomEffect(Consumer<MRules> effectApplier) {
        return (params) -> {
            MRules rules = new MRules(params.getHasDefaultRules());
            effectApplier.accept(rules);
            Move move = new Move(rules.asApplicable(), params.isOptional());
            if (params.getRequiresWorkerSelection()) move.requiresWorkerSelection();
            return move;
        };
    }

    /**
     * Brief The default Move effect applier
     * @param r rules
     */
    public static void defaultMove(MRules r) {}

    /**
     * Brief Effect applier for being able to move on opponent-occupied positions too
     * @param r rules
     */
    public static void occupiedPositionsToo(MRules r) {
        r.allowedPositions().removeExpansion(DefaultMoveFactory.Index.NoOccupied.getIndex());

        r.allowedPositions().overwriteExpansion(new PredicateExpansion<>(s -> {
            Cell finalCell = s.map().cellAt(s.chosenPosition());
            return !finalCell.isOccupied() || !finalCell.getOccupantId().equals(s.currentPlayer().getId());
        }, false, GameMarker.defaultMarker()), DefaultMoveFactory.Index.NoOccupied.getIndex());

        r.movement().addExpansionFirst(new ConsumerExpansion<>(s -> {
            if (s.map().cellAt(s.chosenPosition()).isOccupied()) {
                GameObject enemyWorker = s.map().cellAt(s.chosenPosition()).popGameObject();
                s.map().floatObject(enemyWorker, s.initialPosition());}}, GameMarker.defaultMarker()));

        r.movement().addExpansion(new ConsumerExpansion<>(s ->
                s.map().cellAt(s.initialPosition()).pushFloatingObject(), GameMarker.defaultMarker()));
    }

    /**
     * Brief Effect applier for being able to store the initial worker position (before moving)
     * @param r rules
     */
    public static void storeInitialPosition(MRules r) {
        r.movement().addExpansion(new ConsumerExpansion<>(s -> s.result().setActivePosition(s.initialPosition()), GameMarker.defaultMarker()));
    }

    /**
     * Brief Effect applier for being able to exclude the stored position from choice
     * @param r rules
     */
    public static void excludingStoredPosition(MRules r) {
        r.allowedPositions().addExpansion(new PredicateExpansion<>(s -> !s.chosenPosition().equals(s.activePosition())
                , false, GameMarker.defaultMarker()));
    }

    /**
     * Brief Effect applier for being able to be the only one moving up. If you move up, the others will not
     * @param r rules
     */
    public static void theOnlyOneMovingUp(MRules r) {
        r.movement().addExpansion(new ConsumerExpansion<>(s -> {
            if (s.map().getLevelDifference(s.chosenPosition(), s.initialPosition()) >= 1) {
                int a = 2;
                s.result().expansionRules().mRules().allowedPositions().addExpansion(new PredicateExpansion<>(
                        t -> t.map().getLevelDifference(t.chosenPosition(), t.activeWorkerPosition()) <= 0
                        , false, s.enemiesAsTarget()));

            }}, GameMarker.defaultMarker()));
    }

    /**
     * Brief Effect applier for being able to force backwards (if possible) an enemy on the movement chosen position
     * @param r rules
     */
    public static void forceEnemyBackwards(MRules r) {
        r.allowedPositions().overwriteExpansion(new PredicateExpansion<>(s -> {
            Position finalPosition = s.chosenPosition();
            Cell finalCell = s.map().cellAt(finalPosition);
            Position backwardsPos = GameMap.getBackwardsPosition(s.activeWorkerPosition(), finalPosition);
            Boolean c1 = !finalCell.isOccupied();
            Boolean c2 = finalCell.isOccupied() && !finalCell.getOccupantId().equals(s.currentPlayer().getId()) &&
                    backwardsPos != null && s.map().cellAt(backwardsPos).isReachable();
            return c1 || c2;

        }, false, GameMarker.defaultMarker()), DefaultMoveFactory.Index.NoOccupied.getIndex());

        r.movement().addExpansionFirst(new ConsumerExpansion<>(s -> {
            Position finalPosition = s.chosenPosition();
            if (s.map().cellAt(finalPosition).isOccupied()) {
                Position backwardsPos = GameMap.getBackwardsPosition(s.initialPosition(), finalPosition);
                s.map().move(finalPosition, backwardsPos);}}, GameMarker.defaultMarker()));
    }

    /**
     * Brief Effect applier for being able to win if going down by two or more levels too
     * @param r rules
     */
    public static void winIfDownTwoLevels(MRules r) {
        r.winCondition().addExpansion(new PredicateExpansion<>(s ->
                s.map().getLevelDifference(s.activeWorkerPosition(), s.chosenPosition()) >= 2,
                true, GameMarker.defaultMarker()));
    }

    /**
     * Brief Effect applier for being able to not allow the enemies to win if they are on the perimeter space
     * @param r rules
     */
    public static void noWinIfEnemyOnPerimeter(MRules r) {
        r.winCondition().addExpansion(new PredicateExpansion<>(s -> {
            s.expansionRules().mRules().winCondition().addExpansion(new PredicateExpansion<>(t ->
                    !t.map().perimeterSpace().contains(t.chosenPosition()),
                    false, s.enemiesAsTarget())); return false;}, true, GameMarker.defaultMarker()));
    }
}
