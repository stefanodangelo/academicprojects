package it.polimi.ingsw.santorini.model.gameoperations;

import it.polimi.ingsw.santorini.model.*;
import it.polimi.ingsw.santorini.model.gameoperations.exceptions.RulesIncompleteException;
import it.polimi.ingsw.santorini.model.gameoperations.result.GameOperationResult;
import it.polimi.ingsw.santorini.model.gameoperations.rules.applicable.ApplicableRules;
import it.polimi.ingsw.santorini.model.gameoperations.rules.expandable.ExpandableRulesContainer;
import it.polimi.ingsw.santorini.model.gameoperations.state.GameOperationState;
import it.polimi.ingsw.santorini.model.gameoperations.state.GameState;
import it.polimi.ingsw.santorini.model.utils.TurnMessage;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static it.polimi.ingsw.santorini.model.gameoperations.GameMarker.*;

/**
 * Brief Represents a Game Operation. A Game Operation is a executable process that take use of rules and of a delegate
 * to compute the allowed choices a user can take and actually request a choice from them during the game play.
 * It also makes use of a state to store changes step by step during the whole process.
 * After execution it provides a GameOperationResult to update the situation of the execution pipeline between some
 * consecutive game operations.
 * It also handles the parts of worker/position selection, and win/loss checks processes
 * @param <T> The type of the supported GameOperationState to use for the rules application
 * @param <S> The type of the needed rules for the execution of the operation
 * @see it.polimi.ingsw.santorini.model.gameoperations.rules.Rules
 * @see GameOperationState
 * @see GameOperationResult
 */
public abstract class  GameOperation<T extends GameOperationState, S extends ApplicableRules<T, ?>>
        implements ExpandableGameOperation {

    /**
     * Brief The state of the operation
     */
    protected T state;

    /**
     * Brief The rules of the operation
     */
    protected S rules;

    /**
     * Brief The onCompletion Consumer that is called when execution is done
     */
    private Consumer<GameOperationResult> onCompletion;

    /**
     * Brief Flag that indicates whether the operation may be skipped by the user
     */
    private final Boolean optional;

    /**
     * Brief Flag that indicates whether the operation must perform a worker selection before execution
     */
    private Boolean requiresWorkerSelection;

    /**
     * Brief Map that stores the allowed positions that a user can choose from when performing a movement or a building
     */
    private final HashMap<Worker, List<Position>> allowedPositionsByWorker = new HashMap<>();

    /**
     * Brief Constructor that sets the state, the rules and tells if the operation is optional
     * @param state The state involved
     * @param rules The rules involved
     * @param isOptional The isOptional flag involved
     * @throws RulesIncompleteException if the rules are incomplete
     * @see RulesIncompleteException
     */
    public GameOperation(T state, S rules, Boolean isOptional) throws RulesIncompleteException {
        this.state = state;
        rules.setState(state);
        if (!rules.isComplete()) throw new RulesIncompleteException();
        this.rules = rules;
        this.optional = isOptional;
        requiresWorkerSelection = false;
    }

    /**
     * Brief Constructor that sets the state, the rules. The operation is supposed to be mandatory
     * @param state The state involved
     * @param rules The rules involved
     * @throws RulesIncompleteException if the rules are incomplete
     * @see RulesIncompleteException
     */
    public GameOperation(T state, S rules) throws RulesIncompleteException {
        this(state, rules, false);
    }

    /**
     * Brief Method used when requesting the operation to perform a worker selection before execution
     */
    public void requiresWorkerSelection() {
        this.requiresWorkerSelection = true;
    }

    /**
     * Brief Execution method: it executes the operation using the provided gameState and then reports the result
     * through onCompletion Consumer. If the operation is optional, it may be skipped
     * @param gameState The GameState provided
     * @param onCompletion The Consumer called after execution with tre produced result
     */
    public void execute(GameState gameState, Consumer<GameOperationResult> onCompletion) {
        setup(gameState, onCompletion);
        if (isOptional()) {
            delegate().onBoardChanged(GameMap.getBoard(), null);
            delegate().areOptionalOperationsSkipped(gameState.getCurrentPlayer().getId(), Objects.requireNonNull(OperationType.getTypeByClass(this.getClass())).getName(), (skipped) -> {
                result().setWasSkipped(skipped);
                if (!skipped) execute();
            });
        }
        else execute();
        deployResult();
    }

    /**
     * Brief Execution method: it executes the operation using the provided gameState and then reports the result
     * through onCompletion Consumer. The operation was not skipped
     */
    private void execute() {
        Boolean loss = executeWorkerSelection();
        if (!loss) {
            executePositionSelection();
            executeCustom();
        }
    }

    /**
     * Brief Custom method for additional execution to make any operation fully customisable.
     * It is called as the last execution method
     */
    protected abstract void executeCustom();

    /**
     * Brief Executes the worker selection
     * @return Boolean true if there are not available choices for movement of building after worker selection, false otherwise
     */
    private Boolean executeWorkerSelection() {
        List<Worker> activeWorkers = getActiveWorkers();
        setAllowedPositionsByWorker(activeWorkers);
        Boolean loss = lossCheck();
        if (!loss) {
            activeWorkers = filterActiveWorkers(activeWorkers);
            if (requiresWorkerSelection) workerSelection(activeWorkers);
            delegate().onActiveWorker(gameState().getActiveWorker());
            state.setInitialPosition(state.activeWorkerPosition());
        }
        return loss;
    }

    /**
     * Brief Executes the position selection
     */
    private void executePositionSelection() {
        List<Position> allowedPositions = allowedPositionsByWorker.get(gameState().getActiveWorker());
        delegate().onBoardChanged(GameMap.getBoard(), allowedPositions);
        delegate().positionSelection(gameState().getCurrentPlayer().getId(), allowedPositions, (TurnMessage.positionSelectionMessage + Objects.requireNonNull(OperationType.getTypeByClass(this.getClass())).getName()), gameState().getActiveWorker().getPosition(), this::onPositionSelected);
    }

    /**
     * Brief Method that handles the chosen position during position selection
     * @param position Position the chosen position
     */
    private void onPositionSelected(Position position) {
        state.setChosenPosition(position);
    }

    /**
     * Brief Provides the list of possible active workers (workers that may be available for any use)
     * @return the list of active workers
     */
    private List<Worker> getActiveWorkers() {
        return (requiresWorkerSelection) ?
                currentPlayer().getWorkers() :
                Collections.singletonList(gameState().getActiveWorker());
    }

    /**
     * Brief Method that filters the active workers that are actually available for any use.
     * @param activeWorkers The list of active workers that must be filtered
     * @return the filtered active workers' list
     */
    private List<Worker> filterActiveWorkers(List<Worker> activeWorkers) {
        activeWorkers = activeWorkers.stream()
                .filter((worker) -> !allowedPositionsByWorker.get(worker).isEmpty())
                .collect(Collectors.toList());
        return activeWorkers;
    }

    /**
     * Brief Setup method that makes all ready before the operation execution
     * @param gameState The GameState provided by the execute caller
     * @param onCompletion The Consumer that must be called at the end of the execution
     */
    private void setup(GameState gameState, Consumer<GameOperationResult> onCompletion) {
        setupGameState(gameState);
        this.onCompletion = onCompletion;
        resetFilter();
    }

    /**
     * Brief Specialized setup method for the GameState
     * @param gameState The GameState provided by the execute caller
     */
    private void setupGameState(GameState gameState) {
        state.setGameState(gameState);
        setResultActiveVariables();
    }

    /**
     * Brief Populates the local map accessed by worker for allowed positions storing
     * @param workers The list of workers that must be put in the map
     */
    private void setAllowedPositionsByWorker(List<Worker> workers) {
        workers.forEach((worker) -> putAllowedPositions(worker, allowedPositions(worker)));
    }

    /**
     * Brief Puts the allowed positions in a local map accessed by worker
     * @param key The Worker key
     * @param value The intValueOf
     */
    private void putAllowedPositions(Worker key, List<Position> value) {
        allowedPositionsByWorker.put(key, value);
    }

    /**
     * Brief Performs the worker selection by actually delegating the worker selection
     * @param activeWorkers the possible choices
     */
    private void workerSelection(List<Worker> activeWorkers) {
        if(activeWorkers.size() > 1){
            delegate().onBoardChanged(GameMap.getBoard(), null);
            delegate().workerSelection(gameState().getCurrentPlayer().getId(), activeWorkers, this::onWorkerSelected);
        } else onWorkerSelected(activeWorkers.get(0));
    }

    /**
     * Brief Handles the worker choice
     * @param worker The chosen Worker
     */
    private void onWorkerSelected(Worker worker) {
        gameState().setActiveWorker(worker);
        result().setActiveWorker(worker);
        allowedPositionsByWorker.remove(state.inactiveWorker());
    }

    /**
     * Brief Resets the overall rules' filter
     */
    private void resetFilter() {
        rules.setFilter(filterByCurrentPlayer(currentPlayer()));
    }

    /**
     * Brief Calls the onCompletion Consumer (provided by the execute request) returning the produced operation result
     */
    protected void deployResult() {
        onCompletion.accept(state.result());
    }

    /**
     * Brief Sets the active fields of the result conforming them to the ones present in the provided GameState
     */
    private void setResultActiveVariables() {
        result().setActiveWorker(gameState().getActiveWorker());
        result().setActivePosition(gameState().getActivePosition());
    }

    /**
     * Brief getter for requiresWorkerSelection
     * @return Boolean requiresWorkerSelection
     */
    public Boolean getRequiresWorkerSelection() {
        return requiresWorkerSelection;
    }

    /**
     * Brief Tell whether this operation may be skipped
     * @return Boolean true if the user may skip the operation, false otherwise
     */
    public Boolean isOptional() {
        return optional;
    }

    /**
     * Getter for the delegate
     * @return GameDelegate
     */
    protected GameDelegate delegate() {
        return state.delegate();
    }

    /**
     * Brief Getter for the result
     * @return GameOperationResult
     */
    protected GameOperationResult result() {
        return state.result();
    }

    /**
     * Getter for the game state
     * @return GameState
     */
    protected GameState gameState() {
        return state.gameState();
    }

    /**
     * Getter for the current player
     * @return Player
     */
    protected Player currentPlayer() {
        return state.currentPlayer();
    }

    /**
     * Brief Handles win check using the current win condition
     * @param win Boolean true if the current player won, false otherwise
     */
    protected void winCheck(Boolean win) {
        if (win) result().setWinReport(true);
    }

    /**
     * Brief Handles the loss check using the loss condition
     * @return Boolean true if the current player lost, false otherwise
     */
    private Boolean lossCheck() {
        Boolean loss = lossCondition();
        result().setWinReport((loss) ? false : null);
        return loss;
    }

    /**
     * Brief The current allowed positions rule's result used by the operation
     * @return list of allowed positions
     */
    protected abstract List<Position> allowedPositions();

    /**
     * Brief Uses the current allowed positions rule for a specified worker
     * @param worker the worker to be used for the rule
     * @return list of allowed positions
     */
    private List<Position> allowedPositions(Worker worker) {
        gameState().setActiveWorker(worker);
        return allowedPositions();
    }

    /**
     * Brief The loss condition
     * @return Boolean true if the current player lost the game during this operation
     */
    private Boolean lossCondition() {
        return allowedPositionsByWorker.values().stream().allMatch(List::isEmpty);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void expandRules(ExpandableRulesContainer expansionRules) {
        result().expansionRules().merge(expansionRules);
    }
}
