package it.polimi.ingsw.santorini.testing.view;

import it.polimi.ingsw.santorini.model.*;
import it.polimi.ingsw.santorini.view.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestingView {
    private final List<Color> availableColors = new ArrayList<>(Arrays.asList(Color.YELLOW, Color.RED, Color.CYAN));
    private Worker chosenWorker;
    private List <BlockType> chosenBlockTypes;
    private List<Integer> chosenCardsIds;
    private List <Boolean> skipOperations;
    private Integer gameMode;
    private List<Position> chosenPositions;
    private int numOfPlayers;

    public Integer chooseGameMode(){
        return gameMode;
    }

    public List<String> getPlayersNames(){
        if(numOfPlayers == 2)
            return Arrays.asList("Ciccio", "Turi");
        return Arrays.asList("Ciccio", "Turi", "Iaffiu");
    }

    public List<Integer> getVote(Integer playerId, String question){
        return Arrays.asList(2,2,1);
    }

    public Integer chooseFirstPlayer(ArrayList<Player> players){
        Player chosenPlayer = players.get(1);
        return players.indexOf(chosenPlayer);
    }

    public List<Integer> chooseCards(ArrayList<Card> cards){
        List<Integer> selectedCards = new ArrayList<>();
        selectedCards.add(cards.get(0).getId());
        return selectedCards;
    }

    public Color selectColor(){
        Color chosenColor = availableColors.get(0);
        availableColors.remove(chosenColor);
        return chosenColor;
    }

    public Worker getChosenWorker() {
        return chosenWorker;
    }

    public List<BlockType> getChosenBlockTypes() {
        return chosenBlockTypes;
    }

    public List<Boolean> getSkipOperations() {
        return skipOperations;
    }

    public List<Integer> getChosenCardsIds() {
        return chosenCardsIds;
    }

    public List<Position> getChosenPositions() {
        return chosenPositions;
    }

    public void setChosenCardsIds(List<Integer> chosenCardsIds) {
        this.chosenCardsIds = new ArrayList<>(chosenCardsIds);
    }

    public void setChosenWorker(Worker chosenWorker) {
        this.chosenWorker = chosenWorker;
    }

    public void setChosenBlockTypes(List<BlockType> chosenBlockTypes) {
        this.chosenBlockTypes = new ArrayList<>(chosenBlockTypes);
    }

    public void setSkipOperations(List<Boolean> skipOperations) {
        this.skipOperations = new ArrayList<>(skipOperations);
    }

    public void setGameMode(Integer gameMode) {
        this.gameMode = gameMode;
    }

    public void setChosenPositions(List<Position> chosenPositions){
        this.chosenPositions = new ArrayList<>(chosenPositions);
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }
}
