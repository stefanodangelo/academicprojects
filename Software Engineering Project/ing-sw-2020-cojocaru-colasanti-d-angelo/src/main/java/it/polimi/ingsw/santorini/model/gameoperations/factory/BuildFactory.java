package it.polimi.ingsw.santorini.model.gameoperations.factory;

import it.polimi.ingsw.santorini.model.BlockType;
import it.polimi.ingsw.santorini.model.Cell;
import it.polimi.ingsw.santorini.model.GameObject;
import it.polimi.ingsw.santorini.model.gameoperations.Build;
import it.polimi.ingsw.santorini.model.gameoperations.GameMarker;
import it.polimi.ingsw.santorini.model.gameoperations.custom.GameOperationParameters;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.ConsumerExpansion;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.ListFunctionExpansion;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.PredicateExpansion;
import it.polimi.ingsw.santorini.model.gameoperations.rules.BRules;
import it.polimi.ingsw.santorini.model.gameoperations.rules.factory.DefaultBuildFactory;
import it.polimi.ingsw.santorini.model.gameoperations.rules.factory.DefaultMoveFactory;
import it.polimi.ingsw.santorini.model.gameoperations.state.BState;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Brief BuildFactory for generating cards effects loadable from external file
 */
public abstract class BuildFactory {

    /**
     * Brief the dictionary of the Build effects
     */
    private static Map<String, Function<GameOperationParameters, Build>> buildEffects;

    /**
     * Brief tells if the dictionary has been already loaded
     */
    private static Boolean loaded = false;

    /**
     * Brief Enum that collects each effect with its corresponding rules generation function
     */
    public enum BuildEffect {
        DefaultBuild("defaultBuild", BuildFactory::defaultBuild),
        DomeAnywhere("domeAnywhere", BuildFactory::domeAnywhere),
        StoreBuildingPosition("storeBuildingPosition", BuildFactory::storeBuildingPosition),
        ExcludingStoredPosition("excludingStoredPosition", BuildFactory::excludingStoredPosition),
        OnStoredPositionNoDome("onStoredPositionNoDome", BuildFactory::onStoredPositionNoDome),
        NoMoveUp("noMoveUp", BuildFactory::noMoveUp),
        SwitchWorker("switchWorker", BuildFactory::switchWorker),
        Destroy("destroy", BuildFactory::destroy),
        NoOnPerimeter("noOnPerimeter", BuildFactory::noOnPerimeter),
        SwitchIfInactiveOnGround("switchIfInactiveOnGround", BuildFactory::switchIfInactiveOnGround),
        BuildUnderYourself("buildUnderYourself", BuildFactory::buildUnderYourself)
        ;

        /**
         * Brief the name of the effect
         */
        private final String name;

        /**
         * Brief the rules' generation function
         */
        private final Consumer<BRules> effect;

        /**
         * Brief Main constructor for name and rules' generation function
         * @param name the name of the effect
         * @param effect the rules' generation function
         */
        BuildEffect(String name, Consumer<BRules> effect) {
            this.name = name;
            this.effect = effect;
        }
    }

    /**
     * Brief loads all the effects contained in the BuildEffect enum
     */
    public static void loadEffects() {
        buildEffects = new HashMap<>();
        new ArrayList<>(Arrays.asList(BuildFactory.BuildEffect.values())).forEach(effect ->
                buildEffects.put(effect.name, getCustomEffect(effect.effect)));
    }

    /**
     * Brief Returns the requested by name effect
     * @param name the effect name
     * @param params the customization parameters
     * @return the customized Build
     */
    public static Build getBuild(String name, GameOperationParameters params) {
        if (!loaded) {
            loadEffects();
            loaded = true;
        }
        return buildEffects.get(name).apply(params);
    }

    /**
     * Brief Return the default Build with default rules
     * @return Build the default Build
     */
    public static Build getDefaultBuild() {
        return new Build();
    }

    /**
     * Brief Returns a customizable effect from a effect applier
     * @param effectApplier The effect applier
     * @return the customizable effect
     */
    public static Function<GameOperationParameters, Build> getCustomEffect(Consumer<BRules> effectApplier) {
        return (params) -> {
            BRules rules = new BRules(params.getHasDefaultRules());
            effectApplier.accept(rules);
            Build build = new Build(rules.asApplicable(), params.isOptional());
            if (params.getRequiresWorkerSelection()) build.requiresWorkerSelection();
            return build;
        };
    }

    /**
     * Brief The default Build effect applier
     * @param r rules
     */
    public static void defaultBuild(BRules r) {}

    /**
     * Brief Effect applier for being able to build domes at any level
     * @param r rules
     */
    public static void domeAnywhere(BRules r) {
        r.allowedBlockTypes().addExpansion(new ListFunctionExpansion<>(s ->
                Collections.singletonList(BlockType.DOME), true, GameMarker.defaultMarker()));
    }

    /**
     * Brief Effect applier for being able to store the chosen building position
     * @param r rules
     */
    public static void storeBuildingPosition(BRules r) {
        r.building().addExpansion(new ConsumerExpansion<>(s ->
                s.result().setActivePosition(s.chosenPosition()), GameMarker.defaultMarker()));
    }

    /**
     * Brief Effect applier for being able to exclude from choice the stored position
     * @param r rules
     */
    public static void excludingStoredPosition(BRules r) {
        r.allowedPositions().addExpansion(new PredicateExpansion<>(s ->
                !s.chosenPosition().equals(s.activePosition()), false, GameMarker.defaultMarker()));
    }

    /**
     * Brief Effect applier for being able to build on the stored position excluding dome
     * @param r rules
     */
    public static void onStoredPositionNoDome(BRules r) {
        r.allowedPositions().empty();
        r.allowedPositions().addExpansion(new PredicateExpansion<>(s ->
                s.chosenPosition().equals(s.activePosition()) &&
                        s.map().cellAt(s.chosenPosition()).getLevel() < 3, true, GameMarker.defaultMarker()
        ));
    }

    /**
     * Brief Effect applier for not being able to move up
     * @param r rules
     */
    public static void noMoveUp(BRules r) {
        r.building().addExpansion(new ConsumerExpansion<>(s ->
                s.result().expansionRules().mRules().allowedPositions().addExpansion(new PredicateExpansion<>(t ->
                        t.map().getLevelDifference(t.chosenPosition(), t.activeWorkerPosition()) <= 0
                        , false, s.currentPlayerAsTarget())), GameMarker.defaultMarker()));
    }

    /**
     * Brief Effect applier for being able to switch the active worker
     * @param r rules
     */
    public static void switchWorker(BRules r) {
        r.building().addExpansion(new ConsumerExpansion<>(s ->
                s.result().setActiveWorker(s.inactiveWorker()), GameMarker.defaultMarker()));
    }

    /**
     * Brief Effect applier for being able to destroy a block from the chosen position
     * @param r rules
     */
    public static void destroy(BRules r) {
        r.allowedPositions().addExpansion(new PredicateExpansion<>(s ->
                s.map().cellAt(s.chosenPosition()).getLevel() > 0
                , false, GameMarker.defaultMarker()));

        r.allowedBlockTypes().addExpansion(new ListFunctionExpansion<>(s ->
                Arrays.asList(BlockType.LEVEL1, BlockType.LEVEL2, BlockType.LEVEL3, BlockType.DOME),
                false, GameMarker.defaultMarker()));

        r.building().overwriteExpansion(new ConsumerExpansion<>(s ->
                s.map().cellAt(s.chosenPosition()).popGameObject(), GameMarker.defaultMarker()), 0);
    }

    /**
     * Brief Effect applier for not being able to build on perimeter
     * @param r rules
     */
    public static void noOnPerimeter(BRules r) {
        r.allowedPositions().addExpansion(new PredicateExpansion<>(s ->
                !s.map().perimeterSpace().contains(s.chosenPosition()), false, GameMarker.defaultMarker()));
    }

    /**
     * Brief Effect applier for being able to switch active worker if the inactive one is on the ground
     * @param r rules
     */
    public static void switchIfInactiveOnGround(BRules r) {
        r.building().addExpansion(new ConsumerExpansion<>(s -> {
            if (!s.map().cellAt(s.inactiveWorker().getPosition()).getLevel().equals(0))
                s.result().setAbortNextOperations(true);
            else
                s.result().setActiveWorker(s.inactiveWorker());}, GameMarker.defaultMarker()));
    }

    /**
     * Brief Effect applier for being able to build under yourself
     * @param r rules
     */
    public static void buildUnderYourself(BRules r) {
        r.allowedPositions().overwriteExpansion(new PredicateExpansion<>(s -> {
            Cell initialCell = s.map().cellAt(s.activeWorkerPosition());
            Cell finalCell = s.map().cellAt(s.chosenPosition());
            return !finalCell.equals(initialCell) || finalCell.getLevel() <= 2;},
                true, GameMarker.defaultMarker()), DefaultBuildFactory.Index.NoCurrentPosition.getIndex());

        r.allowedPositions().overwriteExpansion(new PredicateExpansion<>(s -> {
            Cell initialCell = s.map().cellAt(s.activeWorkerPosition());
            Cell finalCell = s.map().cellAt(s.chosenPosition());
            return !finalCell.isOccupied() || finalCell.equals(initialCell);},
                true, GameMarker.defaultMarker()), DefaultBuildFactory.Index.NoOccupied.getIndex());

        r.building().addExpansionFirst(new ConsumerExpansion<>(s -> {
            if (s.chosenPosition().equals(s.activeWorkerPosition())) {
                GameObject worker = s.map().cellAt(s.chosenPosition()).popGameObject();
                s.map().floatObject(worker, s.chosenPosition());
            }}, GameMarker.defaultMarker()));

        r.building().addExpansion(new ConsumerExpansion<>(s ->
                s.map().cellAt(s.activeWorkerPosition()).pushFloatingObject(), GameMarker.defaultMarker()));
    }
}
