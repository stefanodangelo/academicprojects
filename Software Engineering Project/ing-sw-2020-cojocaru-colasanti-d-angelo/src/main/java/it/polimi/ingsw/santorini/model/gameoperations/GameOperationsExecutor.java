package it.polimi.ingsw.santorini.model.gameoperations;

import it.polimi.ingsw.santorini.model.*;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.Expansion;
import it.polimi.ingsw.santorini.model.gameoperations.result.GameOperationResult;
import it.polimi.ingsw.santorini.model.gameoperations.state.GameState;
import it.polimi.ingsw.santorini.model.gameoperations.state.immutable.ImmutableGameState;

import static it.polimi.ingsw.santorini.model.gameoperations.GameMarker.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Brief Represents a GameOperation executor that ca handle multiple operations in a row. It is responsible of
 * applying the results from operation to operation and handling wins and losses
 */
public class GameOperationsExecutor {

    private Player currentPlayer;
    private GameState gameState;
    private GameOperationResult lastResult;
    private Boolean abortNextOperations = false;
    private Boolean skippedWorkerSelection = false;

    private List<GameOperation<?,?>> operations;
    private ImmutableGameState stateBackup;
    private GameDelegate delegate;
    private Consumer<Boolean> onCompletion;
    private Boolean win = false;

    /**
     * Brief Executes all the operations for a single turn of the game
     * @param operations are the operation to execute
     * @param currentPlayer is the player who is currently playing the turn
     * @param gameState is the state of the game
     * @param onCompletion called when completed with the win report of current player
     */
    public void executeOperations(List<GameOperation<?,?>> operations, Player currentPlayer, GameState gameState, Consumer<Boolean> onCompletion) {
        reset(currentPlayer, gameState, operations, onCompletion);
        generateBackup();
        executeLoop();
    }

    /**
     * Brief executes the turn
     */
    private void executeLoop() {
        for (int index = 0; index < operations.size() && !abortNextOperations; index++) {
            GameOperation<?, ?> operation = operations.get(index);
            if (!(lastResult.getWasSkipped() && operation.isOptional())) {
                setupExecution(operation);
                executeOperation(operation);
            }
        }
        endExecution();
    }

    /**
     * Brief aborts the next operations' computing
     */
    private void abortNextOperations() {
        abortNextOperations = true;
    }

    /**
     * Brief flushes the expansions targeting current player to allow a correct execution of the next turn
     */
    private void flushExpansions() {
        removeExpansions(filterByDefaultMarker());
        removeExpansions(filterByTarget(currentPlayer));
        removeExpansions(filterByTrueMark());
    }

    /**
     * Brief ends the execution of the turn
     */
    private void endExecution() {
        //verifyWorkersMobility();
        if (victory()) endTurn();
        else requestUndoOrConfirm();
    }

    /**
     * Brief Checks if the workers of the current player can be moved or not, skipping the undo if necessary
     */
    private void verifyWorkersMobility() {
        for(int i = 0; i < currentPlayer.getWorkers().size(); i++) {
            for (Position p : GameMap.getNeighboringPositions(currentPlayer.getWorkers().get(i).getPosition()))
                if (gameState.getMap().cellAt(p).isReachable())
                    break;
            if(i == currentPlayer.getWorkers().size() - 1) endTurn();
        }
    }

    /**
     * Brief ends the execution of the turn
     * @return Boolean true if the player is victorious, false otherwise
     */
    private Boolean victory() {
        return win != null ? win : false;
    }

    /**
     * Brief Asking the player for undo or confirmation of the turn
     */
    private void requestUndoOrConfirm() {
        delegate.requestUndoOrConfirm(undo -> {
            if (undo) undo();
            else endTurn();
        });
    }

    /**
     * Brief ends officially the turn
     */
    private void endTurn() {
        delegate.onTurnOver();
        deployResult();
    }

    /**
     * Brief deploys the result after flushing the expansions
     * @param win the win report of the current player
     */
    private void deployResult(Boolean win) {
        flushExpansions();
        if (onCompletion != null) onCompletion.accept(win);
    }

    /**
     * Brief deploys the result
     */
    private void deployResult() {
        deployResult(win);
    }

    /**
     * Brief Undo feature: it restores the initial state and starts a new clean turn execution
     */
    private void undo() {
        restore();
        executeLoop();
    }

    /**
     * Brief restores the initial state (at the beginning of the turn)
     */
    private void restore() {
        resetVariables();
        restoreBackup();
    }

    /**
     * Brief restores the initial game state and the initial expansions
     */
    private void restoreBackup() {
        restoreGameState();
        restoreExpansions();
        delegate.onBoardChanged(GameMap.getBoard(), null);
        delegate.onTurnOver();
    }

    /**
     * Brief restores the initial expansions
     */
    private void restoreExpansions() {
        List<Expansion<?,GameMarker>> expansions = lastResult.expansionRules().getAllExpansions();
        if (expansions != null) expansions.removeIf(expansion -> {
            List<GameMarker> markers = expansion.getMarkers();
            if (markers != null)
                for (GameMarker marker : markers)
                    if (marker.getMark()) return false;
            return true;
        });
    }

    /**
     * Brief restores the initial game state
     */
    private void restoreGameState() {
        gameState = stateBackup.getMutable();
        delegate.onBoardChanged(GameMap.getBoard(), null);
    }

    /**
     * Brief generates the backup at the start of the turn
     */
    private void generateBackup() {
        this.stateBackup = new ImmutableGameState(gameState);
        markEarlyExpansions();
    }

    /**
     * Brief resets the main parameters at the start of the turn
     * @param operations are the operation to execute
     * @param currentPlayer is the player who is currently playing the turn
     * @param gameState is the state of the game
     * @param onCompletion called when completed with the win report of current player
     */
    private void reset(Player currentPlayer, GameState gameState, List<GameOperation<?,?>> operations, Consumer<Boolean> onCompletion) {
        this.currentPlayer = currentPlayer;
        this.gameState = gameState;
        setupGameStateCurrentPlayer();
        this.operations = operations;
        this.onCompletion = onCompletion;
        this.delegate = gameState.getDelegate();
        generateLastResult();
        resetVariables();
    }

    /**
     * Brief resets the main variables at the start of the turn
     */
    private void resetVariables() {
        win = false;
        abortNextOperations = false;
        skippedWorkerSelection = false;
        lastResult.setWinReport(null);
        lastResult.setWasSkipped(null);
        lastResult.setAbortNextOperations(false);
        lastResult.setActiveWorker(null);
        lastResult.setActivePosition(null);
    }

    /**
     * Brief generates a fresh last result storing
     */
    private void generateLastResult() {
        if (lastResult == null) lastResult = new GameOperationResult();
    }

    /**
     * Brief marks the early expansions to allow expansions recovery afterwards
     */
    private void markEarlyExpansions() {
        List<Expansion<?,GameMarker>> expansions = lastResult.expansionRules().getAllExpansions();
        if (expansions != null) expansions.forEach(expansion -> expansion.getMarkers().add(earlyMarker()));
    }

    /**
     * Brief the marker used to mark early expansions
     * @return GameMarker(true)
     */
    private GameMarker earlyMarker() {
        return new GameMarker(true);
    }

    /**
     * Sets up the parameters needed for the execution of the operation
     * @param operation is the gameOperation to be executed
     */
    private void setupExecution(GameOperation<?,?> operation) {
        applyLastResult(operation);
        if (skippedWorkerSelection) skippedWorkerSelectionCorrection(operation);
    }

    /**
     * Brief Applies the last result to the current operation
     * @param operation is the operation to be executed
     */
    private void applyLastResult(GameOperation<?,?> operation) {
        setupGameState();
        setupGameOperation(operation);
    }

    /**
     * Brief Updates abortNextOperation in order to eventually stop the operation's execution flow
     */
    private void updateAbortNextOperations() {
        if (!abortNextOperations) abortNextOperations = lastResult.getAbortNextOperations();
    }

    /**
     * Brief Sets the current player in the state of the game
     */
    private void setupGameStateCurrentPlayer() {
        gameState.setCurrentPlayer(currentPlayer);
    }

    /**
     * Brief Sets the active worker and the active position basing on the result of the last operation
     */
    private void setupGameState() {
        gameState.setActiveWorker(lastResult.getActiveWorker());
        gameState.setActivePosition(lastResult.getActivePosition());
    }

    /**
     * Brief Sets up the rules for the next operation basing on the results of the last one executed
     * @param operation is the next operation to be executed
     */
    private void setupGameOperation(GameOperation<?,?> operation) {
        operation.expandRules(lastResult.expansionRules());
    }

    /**
     * Brief Checks if the current players is a winner or a loser
     */
    private void handleWinReport() {
        win = lastResult.getWinReport();
        if (win != null) {
            if (!win) removeCurrentPlayerExpansions();
            abortNextOperations();
        }
    }

    /**
     * Brief Removes all current player's expansions (used to disable its effects from next turn)
     */
    private void removeCurrentPlayerExpansions() {
        removeExpansions(filterByOwner(currentPlayer));
        removeExpansions(filterByTarget(currentPlayer));
    }

    /**
     * Brief Executes the operation passed to the function
     * @param operation is the operation to be executed
     */
    private void executeOperation(GameOperation<?,?> operation) {
        operation.execute(gameState, (result) -> {
            skippedWorkerSelectionCheck(operation, result);
            afterExecution(result);
        });
    }

    /**
     * Updates the executor state based on the last execution
     * @param result is the result of the GameOperation
     */
    private void afterExecution(GameOperationResult result) {
        lastResult = result;
        handleWinReport();
        updateAbortNextOperations();
    }

    /**
     * Brief Requires worker selection if it was previously skipped
     * @param operation is the operation to be executed
     */
    private void skippedWorkerSelectionCorrection(GameOperation<?,?> operation) {
        operation.requiresWorkerSelection();
        skippedWorkerSelection = false;
    }

    /**
     * Brief Checks if worker selection was previously skipped and updates accordingly skippedWorkerSelection
     * @param operation is the executed operation
     * @param result the last result
     */
    private void skippedWorkerSelectionCheck(GameOperation<?,?> operation, GameOperationResult result) {
        if (operation.getRequiresWorkerSelection() && result.getWasSkipped()) skippedWorkerSelection = true;
    }

    /**
     * Brief Removes the undesired expansions from the last result's rules
     * @param undesired is the condition used to filter the undesired expansions
     */
    private void removeExpansions(Predicate<GameMarker> undesired) {
        lastResult.expansionRules().removeExpansionsByFilter(undesired);
    }
}
