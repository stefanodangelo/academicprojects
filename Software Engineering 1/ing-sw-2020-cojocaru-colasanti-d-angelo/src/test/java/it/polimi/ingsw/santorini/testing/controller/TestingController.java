package it.polimi.ingsw.santorini.testing.controller;

import it.polimi.ingsw.santorini.controller.CardLoader;
import it.polimi.ingsw.santorini.model.*;
import it.polimi.ingsw.santorini.testing.view.TestingView;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class TestingController implements GameDelegate {
    static TestingView testingView = new TestingView();

    public TestingController(){
        String cardsFileExtension = "xml";
        CardLoader.getInstance();
        CardLoader.loadCards();
    }

    @Override
    public GameMode requestGameMode(Integer playerId) {
        Integer choice = testingView.chooseGameMode();
        switch (choice){
            case 0: return GameMode.BASIC;
            case 1: return GameMode.COMPLETE;
            case 2: return GameMode.ADVANCED;
            default: throw new IllegalStateException("Unexpected intValueOf: " + choice);
        }
    }

    @Override
    public void requestPlayersNames(Consumer<List<String>> onCompletion) {
        onCompletion.accept(testingView.getPlayersNames());
    }


    @Override
    public void requestPlayerColor(Integer playerId, Consumer<String> onCompletion) {
        onCompletion.accept(testingView.selectColor().getName());
    }

    @Override
    public void sendPoll(List<Integer> playersIds, Poll poll, Consumer<List<Integer>> onCompletion) {
        for(Integer id : playersIds)
            onCompletion.accept(testingView.getVote(id, poll.getQuestion()));
    }

    @Override
    public void requestFirstPlayerChoice(Integer playerId, ArrayList<Player> players, Consumer<Integer> onCompletion) {
        onCompletion.accept(testingView.chooseFirstPlayer(players));
    }

    @Override
    public void requestCardsSelection(Integer playerId, ArrayList<Card> cards, Integer numberOfSelections, Consumer<List<Integer>> onCompletion) {
        if(numberOfSelections.equals(1))
            onCompletion.accept(testingView.chooseCards(cards));
        else
            onCompletion.accept(testingView.getChosenCardsIds());
    }

    @Override
    public void onBoardChanged(Cell[][] board, List<Position> validPositions) {

    }

    @Override
    public void workerSelection(Integer currentPlayerId, List<Worker> workers, Consumer<Worker> onCompletion) {
        onCompletion.accept(testingView.getChosenWorker());
    }

    @Override
    public void positionSelection(Integer currentPlayerId, List<Position> allowedPositions, String turnPhase, Position currentPosition, Consumer<Position> onCompletion)
    throws IncorrectInputs {
        checkInputs(allowedPositions, testingView.getChosenPositions());
        Position chosenPosition;
        do chosenPosition = testingView.getChosenPositions().remove(0);
        while (!allowedPositions.contains(chosenPosition));
        onCompletion.accept(chosenPosition);
    }

    @Override
    public void blockTypeSelection(Integer currentPlayerId, List<BlockType> blockTypes, Consumer<BlockType> onCompletion)
    throws IncorrectInputs {
        checkInputs(blockTypes, testingView.getChosenBlockTypes());
        BlockType chosenBlockType;
        List<BlockType> remainingBlockTypes = new ArrayList<>((testingView.getChosenBlockTypes()));
        chosenBlockType = testingView.getChosenBlockTypes().get(0);
        remainingBlockTypes.remove(0);
        testingView.setChosenBlockTypes(remainingBlockTypes);
        onCompletion.accept(chosenBlockType);
    }

    @Override
    public void executingGameOperation(String gameOperation) {

    }

    @Override
    public void areOptionalOperationsSkipped(Integer currentPlayerId, String operationType, Consumer<Boolean> onCompletion)
    throws IncorrectInputs {
        checkInputs(new ArrayList<>(Arrays.asList(true, false)), testingView.getSkipOperations());
        Boolean skipOperation = true;
        List <Boolean> remainingSkipOperations = new ArrayList<>((testingView.getSkipOperations()));
        if(!remainingSkipOperations.isEmpty()){
            skipOperation = remainingSkipOperations.get(0);
            remainingSkipOperations.remove(0);
            testingView.setSkipOperations(remainingSkipOperations);
        }
        onCompletion.accept(skipOperation);
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
        onCompletion.accept(false);
    }

    public static TestingView getTestingView() {
        return testingView;
    }

    private <T> void checkInputs(List<T> validInputs, List<T> inputs) throws IncorrectInputs {
        if (inputs.stream().noneMatch(validInputs::contains)) throw new IncorrectInputs();
    }
}
