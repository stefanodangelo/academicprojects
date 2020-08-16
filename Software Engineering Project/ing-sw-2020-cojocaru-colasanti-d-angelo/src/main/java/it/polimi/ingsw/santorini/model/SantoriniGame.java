package it.polimi.ingsw.santorini.model;

import it.polimi.ingsw.santorini.controller.CardLoader;
import it.polimi.ingsw.santorini.model.gameoperations.GameOperation;
import it.polimi.ingsw.santorini.model.gameoperations.GameOperationsExecutor;
import it.polimi.ingsw.santorini.model.gameoperations.factory.BuildFactory;
import it.polimi.ingsw.santorini.model.gameoperations.factory.MoveFactory;
import it.polimi.ingsw.santorini.model.gameoperations.state.GameState;
import it.polimi.ingsw.santorini.model.utils.TurnMessage;

import java.util.*;
import java.util.function.Consumer;

/**
 * Brief Main Singleton class of the whole game, storing the informations about the map, the players and other parameters and communicating with the controller through an interface
 */
public class SantoriniGame implements TurnBasedGame {
    private static final Integer TWOPLAYERSMODE = 2;
    private static final Integer STANDARDNUMOFWORKERS = 2;
    private static GameMap map;
    private final GameDelegate delegate;
    private final PlayersHandler playersHandler = new PlayersHandler();
    private final Integer numberOfPlayers;
    private static GameMode mode;
    private final GameOperationsExecutor executor = new GameOperationsExecutor();

    /**
     * Brief Creates a new instance of SantoriniGame
     * @param delegate is the GameDelegate who communicates with the controller
     * @param numberOfPlayers is the number of players in game
     */
    public SantoriniGame(GameDelegate delegate, Integer numberOfPlayers) {
        this.delegate = delegate;
        this.numberOfPlayers = numberOfPlayers;
    }

    /**
     * Brief This method is divided in steps, also based on the rulebook's Setup section:
     *       1) Map creation (rulebook's steps 1 and 2)
     *       2) Players log in
     *       3) Game mode request: if BASIC, loads the basic rules and randomly chooses the first player then goes to step 4)
     *          3.1) Poll for the most god-like player election (rulebook's step 3)
     *          3.2) God powers selection and "reading" (rulebook's steps 3, 4 and 5)
     *          3.3) Cards assignment in clockwise order (rulebook's step 6)
     *          3.4) First player choice (rulebook's step 7)
     *       4) Workers selection and positioning (rulebook's step 7)
     */
    @Override
    public void setupGame() {
        Player godLikePlayer;

        //Players acceptance via server
        logIn();

        //Game mod choice
        mode = delegate.requestGameMode(PlayersHandler.getPlayers().get(0).getId());

        if(mode.equals(GameMode.BASIC)) {
            loadBasicRules(PlayersHandler.getPlayers());
            PlayersHandler.setCurrentPlayerIndex((int) (Math.random() * 10) % numberOfPlayers);
        }
        else {
            //Most god-like Player choice via Poll
            Poll poll = new Poll(numberOfPlayers, "Choose the most \"god-like\" player");
            godLikePlayer = godLikePlayerChoice(PlayersHandler.getPlayers(), poll);

            //Cards loading and selection
            cardsSelection(godLikePlayer);

            //Cards' assignment
            assignCards(CardLoader.getCards(), godLikePlayer);

            //First Player choice
            delegate.requestFirstPlayerChoice(godLikePlayer.getId(), PlayersHandler.getPlayers(), id -> PlayersHandler.setCurrentPlayerIndex(PlayersHandler.getPlayers().indexOf(PlayersHandler.getPlayerById(id))));
        }

        //Workers choices and positioning
        selectWorker(getUnassignedPositions());
    }

    Player currentPlayer;

    /**
     * Brief Calls the current player's turn handler, then chooses the next player and ends the game if there's a winner
     */
    @Override
    public void startGame() {
        currentPlayer = PlayersHandler.getCurrentPlayer();
        gameCycle();
    }

    private void gameCycle() {
        currentPlayer = PlayersHandler.getCurrentPlayer();
        handleTurn(currentPlayer, thereIsAWinner -> {
            currentPlayer = playersHandler.next();
            if (!thereIsAWinner) gameCycle();
        });
    }

    /**
     * Brief Handles the current turn
     * @param player is the player who plays the current turn
     * @param onCompletion true if there's a winner, otherwise false
     */
    @Override
    public void handleTurn(Player player, Consumer<Boolean> onCompletion) {
        executor.executeOperations(player.getCard().getPower(), player, new GameState(map, PlayersHandler.getPlayers(), delegate), (win) -> {
            if(win != null) checkWin(player, win, onCompletion);
            else if (onCompletion != null) onCompletion.accept(false);
        });
    }

    /**
     * Brief checks the victory condition for the current turn
     * @param player is the player who plays the current turn
     * @param win is true in case of win, false in case of loss
     * @param onCompletion true if there's a winner, otherwise false
     */
    private void checkWin(Player player, Boolean win, Consumer<Boolean> onCompletion) {
        if(!win) {
            if(PlayersHandler.getPlayers().size() == TWOPLAYERSMODE){
                Player winner = playersHandler.next();
                String notificationMessage = winner.getName() + TurnMessage.winMotivationMessage + player.getName() + TurnMessage.loseMotivationMessage;
                informPlayers(player, notificationMessage, TurnMessage.loseMessage, false);
                informPlayers(winner, winner.getName() + TurnMessage.winNotificationMessage, TurnMessage.winMessage, true);
                onCompletion.accept(true);
            }
            else {
                removeWorkers(player);
                delegate.onBoardChanged(GameMap.getBoard(), null);
                informPlayers(player, player.getName() + TurnMessage.loseMotivationMessage + ", so he" + TurnMessage.loseNotificationMessage, TurnMessage.loseMessage, false);
                PlayersHandler.setCurrentPlayerIndex(player.getId() - 1);
            }
        }
        else
            informPlayers(player, player.getName() + TurnMessage.winNotificationMessage, TurnMessage.winMessage, true);
        if (onCompletion != null) onCompletion.accept(win);
    }

    /**
     * Brief Accepts a player asking him to insert a username
     */
    private void logIn(){
        delegate.requestPlayersNames(names -> {
            for(String name : names)
                playersHandler.addPlayer(new Player(names.indexOf(name) + 1, name));
        });
    }

    /**
     * Brief Loads basic rules to each player, giving them a special card with id equals to 0
     * @param players are the players in game
     */
    private void loadBasicRules(ArrayList<Player> players){
        List<GameOperation<?, ?>> operations = Arrays.asList(MoveFactory.getDefaultMove(), BuildFactory.getDefaultBuild());
        Card card = new Card(0);
        card.setPower(operations);
        for(Player player : players)
            player.setCard(card);
    }

    /**
     * Brief Sends a Poll to all the players to let them choose the most "god-like" player
     * @param players contains a reference to every player
     * @param poll is an instance containing a question and a voting method
     * @return the most god-like player
     */
    private Player godLikePlayerChoice(ArrayList<Player> players, Poll poll){
        List<Integer> ids = new ArrayList<>();
        for(int i = 0; i < numberOfPlayers; i++)
            ids.add(players.get(i).getId());
        delegate.sendPoll(ids, poll, votedIds -> {
            for(Integer votedId : votedIds)
                poll.vote(votedId, players);
        });
        PlayersHandler.setCurrentPlayerIndex(players.indexOf(poll.getLeader(players)));

        return PlayersHandler.getCurrentPlayer();
    }

    /**
     * Brief Asks the god-like player to select a number of cards equal to the number of players in game
     * @param godLikePlayer is the player who must choose the cards
     */
    private void cardsSelection(Player godLikePlayer){
        ArrayList<Card> cards = CardLoader.getCards();
        if(mode.equals(GameMode.COMPLETE))
            cards.removeIf(Card::isAvanced);
        delegate.requestCardsSelection(godLikePlayer.getId(), cards, numberOfPlayers, CardLoader::selectCards);
    }

    /**
     * Brief Assigns one of the selected cards in clockwise order to each player
     * @param cardsToAssign are the cards to assign
     * @param godLikePlayer is the last player who receives the card
     */
    private void assignCards(ArrayList<Card> cardsToAssign, Player godLikePlayer){
        Integer NUMOFSELECTIONS = 1;
        List<Card> assignedCards = new ArrayList<>();

        for(int i = 0; i < numberOfPlayers - 1; i++){
            Player p = playersHandler.next();

            delegate.requestCardsSelection(p.getId(), cardsToAssign, NUMOFSELECTIONS, selectedCard -> p.setCard(CardLoader.getCardById(selectedCard.get(0))));
            cardsToAssign.remove(p.getCard());
        }
        godLikePlayer.setCard(cardsToAssign.get(0));
        for(Player p : PlayersHandler.getPlayers())
            assignedCards.add(p.getCard());
        delegate.onCardsSelected(assignedCards);
    }

    /**
     * Brief This method is called at the very beginning of the game, when players have to place their workers on the map
     * @return all the positions in the map, because initially they're all free
     */
    private ArrayList<Position> getUnassignedPositions(){
        ArrayList<Position> unassignedPositions = new ArrayList<>();
        for(int i = 0; i < GameMap.getDefaultWidth(); i++)
            for(int j = 0; j < GameMap.getDefaultHeight(); j++)
                unassignedPositions.add(new Position(i, j));
        return unassignedPositions;
    }

    /**
     * Brief Requests to choose a color to the player and then to place his two workers on the board
     * @param freePositions are the available positions on the board
     */
    private void selectWorker(ArrayList<Position> freePositions){
        for(int i = 0; i < numberOfPlayers; i++){
            Integer currentPlayerId = PlayersHandler.getCurrentPlayer().getId();
            delegate.requestPlayerColor(currentPlayerId, color -> assignWorkers(currentPlayerId, color, freePositions));
            playersHandler.next();
        }
    }

    /**
     * Brief Assigns the workers to a player
     * @param playerId is the id of the player who chose to control these workers
     * @param color is the color of the chosen workers
     * @param freePositions contains all positions not yet occupied by a worker
     */
    private void assignWorkers(Integer playerId, String color, ArrayList<Position> freePositions) {
        placeWorkers(new ArrayList<>(), color, playerId, freePositions);
        delegate.onBoardChanged(GameMap.getBoard(), freePositions);
    }

    /**
     * Brief Places one by one all the workers on the board and deletes their position from the allowed ones
     * @param workers are the workers to be placed, then assigned
     * @param color is the available chosen color
     * @param playerId is the id of the player who needs the assignment
     * @param freePositions contains all the positions not yet occupied by a worker
     */
    private void placeWorkers(ArrayList<Worker> workers, String color, Integer playerId, ArrayList<Position> freePositions){
        for(int i = 0; i < STANDARDNUMOFWORKERS; i++)
            workers.add(i, new Worker(color, Gender.values()[i]));
        PlayersHandler.getPlayerById(playerId).setWorkers(workers);
        for(Worker w : workers) {
            delegate.onBoardChanged(GameMap.getBoard(), freePositions);
            delegate.positionSelection(playerId, freePositions, TurnMessage.workerPlacementMessage, null, position -> { map.addGameObject(w, position); freePositions.remove(position); w.setPlayerId(playerId);});
        }
    }

    public PlayersHandler getPlayersHandler() {
        return playersHandler;
    }

    public static GameMode getMode() {
        return mode;
    }

    /**
     * Brief Creates a new GameMap
     */
    public static void createMap(){
        map = new GameMap();
    }

    /**
     * Brief Informs the players if there is a winner or a loser, removing him from the list of players currently in game
     * @param player is the winner or the loser
     * @param notificationMessage is the message to send to all the players except the winner/loser
     * @param victoryMessage is the message to send to the winner/loser
     * @param gameOver is true if the game will end after this notification process
     */
    private void informPlayers(Player player, String notificationMessage, String victoryMessage, boolean gameOver) {
        for(Player p : PlayersHandler.getPlayers())
            if(!p.equals(player))
                delegate.onVictoryCondition(p.getId(), notificationMessage, null);
        delegate.onVictoryCondition(player.getId(), victoryMessage, gameOver);
        PlayersHandler.removePlayer(player);
    }

    /**
     * Brief Removes from the map the workers of a certain player
     * @param player is the player who generally lost a game
     */
    public static void removeWorkers(Player player) {
        try{
            List<Worker> workers = player.getWorkers();
            for (Worker worker : workers)
                map.cellAt(worker.getPosition()).popGameObject();
        } catch (NullPointerException ignored){
        }
    }

    @Override
    public String toString() {
        return "SantoriniGame{" +
                "map='" + map.toString() +
                ", playersHandler=" + playersHandler.toString() +
                ", numberOfPlayers=" + numberOfPlayers +
                '}';
    }
}
