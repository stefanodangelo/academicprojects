package it.polimi.ingsw.santorini.model.gameoperations;

import it.polimi.ingsw.santorini.model.*;
import it.polimi.ingsw.santorini.model.gameoperations.state.GameState;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class GameOperationsController implements GameDelegate {

    private GameState gameState;

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public List<Boolean> getSkips() {
        return skips;
    }

    public void setSkips(List<Boolean> skips) {
        this.skips = new ArrayList<>(skips);
    }

    private List<Player> players  = new ArrayList<>();
    private int playerIndex = 0;

    private List<Position> positions = new ArrayList<>();
    private List<BlockType> blockTypes  = new ArrayList<>();
    private List<Integer> workers  = new ArrayList<>();
    private List<Boolean> skips = new ArrayList<>();

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = new ArrayList<>(players);
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = new ArrayList<>(positions);
    }

    public List<BlockType> getBlockTypes() {
        return blockTypes;
    }

    public void setBlockTypes(List<BlockType> blockTypes) {
        this.blockTypes = new ArrayList<>(blockTypes);
    }

    public List<Integer> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Integer> workers) {
        this.workers = new ArrayList<>(workers);
    }

    public Integer getCurrentWorker() {
        return workers.get(0);
    }

    public Position getCurrentPosition() {
        return positions.get(0);
    }

    public BlockType getCurrentBlockType() {
        return blockTypes.get(0);
    }

    public Player getCurrentPlayer() {
        return players.get(playerIndex);
    }

    public void nextPlayer() {
        playerIndex = playerIndex < players.size() - 1 ? playerIndex + 1 : 0;
        gameState.setCurrentPlayer(getCurrentPlayer());
    }

    public Position consumePosition() {
        return positions.remove(0);
    }

    public BlockType consumeBlockType() {
        return blockTypes.remove(0);
    }

    public Worker consumeWorker() {
        return getCurrentPlayer().getWorkers().get(workers.remove(0));
    }

    public Boolean consumeSkip() {
        return skips.remove(0);
    }

    public GameOperationsController(List<Player> players) {
        setPlayers(players);

        gameState = new GameState(
                        new GameMap(),
                        new ArrayList<>(players), this);

        players.forEach((player) -> player.getWorkers().forEach((worker) -> gameState.getMap().addGameObject(worker, worker.getPosition())));

        gameState.setCurrentPlayer(getCurrentPlayer());
    }

    @Override
    public GameMode requestGameMode(Integer playerId) {
        return null;
    }

    @Override
    public void requestPlayersNames(Consumer<List<String>> onCompletion) {

    }

    @Override
    public void requestPlayerColor(Integer playerId, Consumer<String> onCompletion) {

    }

    @Override
    public void sendPoll(List<Integer> playersIds, Poll poll, Consumer<List<Integer>> onCompletion) {

    }


    @Override
    public void requestFirstPlayerChoice(Integer playerId, ArrayList<Player> players, Consumer<Integer> onCompletion) {

    }

    @Override
    public void requestCardsSelection(Integer playerId, ArrayList<Card> cards, Integer numberOfSelections, Consumer<List<Integer>> onCompletion) {

    }

    @Override
    public void onBoardChanged(Cell[][] board, List<Position> validPositions) {

    }

    @Override
    public void workerSelection(Integer currentPlayerId, List<Worker> workers, Consumer<Worker> onCompletion) {
        onCompletion.accept(consumeWorker());
    }

    @Override
    public void positionSelection(Integer currentPlayerId, List<Position> allowedPositions, String turnPhase, Position currentPosition, Consumer<Position> onCompletion) {
        onCompletion.accept(consumePosition());
    }

    @Override
    public void blockTypeSelection(Integer currentPlayerId, List<BlockType> blockTypes, Consumer<BlockType> onCompletion) {
        onCompletion.accept(consumeBlockType());
    }

    @Override
    public void executingGameOperation(String gameOperation) {

    }

    @Override
    public void areOptionalOperationsSkipped(Integer currentPlayerId, String operationType, Consumer<Boolean> onCompletion) {
        onCompletion.accept(consumeSkip());
    }

    @Override
    public void onVictoryCondition(Integer playerId, String message, Boolean gameOver) {

    }

    @Override
    public void onActiveWorker(Worker worker) {

    }

    @Override
    public void onCardsSelected(List<Card> cards) {

    }

    @Override
    public void onTurnOver() {

    }

    @Override
    public void requestUndoOrConfirm(Consumer<Boolean> onCompletion) {

    }


}
