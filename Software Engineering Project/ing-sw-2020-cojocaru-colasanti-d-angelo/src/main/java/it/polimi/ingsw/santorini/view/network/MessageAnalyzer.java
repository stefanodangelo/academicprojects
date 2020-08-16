package it.polimi.ingsw.santorini.view.network;

import it.polimi.ingsw.santorini.communication.*;
import it.polimi.ingsw.santorini.view.modelimport.BlockLevel;
import it.polimi.ingsw.santorini.view.View;
import it.polimi.ingsw.santorini.view.utils.ClientSocketMessage;

import java.util.*;
import java.util.function.Consumer;

/**
 * Brief Parser for the message received from the server
 */
public class MessageAnalyzer {
    private final View view;

    public MessageAnalyzer(View view) {
        this.view = view;
    }

    /**
     * Brief Analyzes a message received during the lobby phase
     * @param message is the message to analyze
     */
    public void analyzeMessage(LobbyMessage message){
        switch (MethodHeading.values()[message.getId()]){
            case LOBBYLIST: printList(message.getData()); break;
            case LOBBYCOMMANDS: printCommands(message.getData()); break;
        }
    }

    /**
     * Brief Analyzes a certain message by extracting its fields and checking which View's method has to be called
     * @param message is the message to analyze
     * @param onCompletion called when completed
     */
    public void analyzeMessage(GameMessage message, Consumer<GameMessage> onCompletion){
        List<Object> data = message.getData();
        try{
            browseMethod(MethodHeading.values()[message.getId()], data, onCompletion);
        } catch (ArrayIndexOutOfBoundsException e){
            if(data != null)
                setAttributes(data);
            else
                View.resetActivePosition();
            onCompletion.accept(null);
        }
    }

    /**
     * Brief Looks for the method to call corresponding to the heading passed as a parameter
     * @param methodHeading is a useful value to find the correct method
     * @param data are the sensible information contained in the original message
     * @param onCompletion called when completed
     */
    private void browseMethod(MethodHeading methodHeading, List<Object> data, Consumer<GameMessage> onCompletion) {
        switch (methodHeading) {
            case PARAMS: extractParameters(data);
                onCompletion.accept(null);
                break;
            case BOARD: unwrapBoard(data);
                onCompletion.accept(null);
                break;
            case MODE:
                getMode(data, (mode) ->
                        onCompletion.accept(new GameMessage(View.getPlayerId(), false, Collections.singletonList(mode))));
                break;
            case USERNAME:
                getUsername(data, (username) ->
                        onCompletion.accept(new GameMessage(View.getPlayerId(), false, Collections.singletonList(username))));
                break;
            case COLOR:
                view.selectColor((color) ->
                        onCompletion.accept(new GameMessage(View.getPlayerId(), true, Arrays.asList(color, View.getPlayerName()))));
                break;
            case VOTE:
                getVote(data, (vote) ->
                        onCompletion.accept(new GameMessage(View.getPlayerId(), false, Collections.singletonList(vote))));
                break;
            case FIRST:
                getFirst(data, (first) ->
                        onCompletion.accept(new GameMessage(View.getPlayerId(), false, Collections.singletonList(first))));
                break;
            case CARDS:
                getCards(data, (cards) ->
                        onCompletion.accept(new GameMessage(View.getPlayerId(), false, Collections.singletonList(cards))));
                break;
            case WORKER:
                getWorker(data, (worker) ->
                        onCompletion.accept(new GameMessage(View.getPlayerId(), false, Collections.singletonList(worker))));
                break;
            case POSITION:
                View.ViewStatus.endSetup();
                getPosition(data, (position) ->
                        onCompletion.accept(new GameMessage(View.getPlayerId(), false, Collections.singletonList(position))));
                break;
            case BLOCK:
                getBlock(data, (block) ->
                        onCompletion.accept(new GameMessage(View.getPlayerId(), false, Collections.singletonList(block))));
                break;
            case SKIP:
                view.skipOperation((String) data.get(0), (skipped) ->
                        onCompletion.accept(new GameMessage(View.getPlayerId(), false, Collections.singletonList(skipped))));
                break;
            case ID: View.setPlayerId((Integer) data.get(0));
                onCompletion.accept(null);
                break;
            case UNDO:
                view.onUndoRequest((undone) ->
                        onCompletion.accept(new GameMessage(View.getPlayerId(), false, Collections.singletonList(undone))));
                break;
        }
    }

    /**
     * Brief Extracts the list of players during the lobby
     * @param data are the data contained in the original message
     */
    public void parseMessageId(Integer id){
        if(view.requestsImmediateRun())
            view.printMessage(ClientSocketMessage.serverAnswerMessage + NetworkMessage.byId(id).getMessage());
        switch (NetworkMessage.byId(id)) {
            case host:
                view.thisIsHost(true);
                break;
            case waitStart:
                view.thisIsHost(false);
                break;
            case startConfirmation:
                view.gameIsStarting();
                break;
            case waitChoice: case waitVote:
                view.waitOtherPlayers();
                break;
            default:
                break;
        }
    }

    /**
     * Brief Extracts the list of players during the lobby
     * @param data are the data contained in the original message
     */
    @SuppressWarnings("unchecked")
    private void printList(List<Object> data) {
        for(Object extractedData : data){
            if(extractedData instanceof List) {
                if(((List<?>) extractedData).get(0) instanceof String)
                    view.printLobbyList((List<String>) extractedData);
            }
        }
    }

    /**
     * Brief Extracts the commands available during the lobby phase
     * @param data contains the commands to print
     */
    @SuppressWarnings("unchecked")
    private void printCommands(List<Object> data) {
        try {
            view.printLobbyCommands((List<String>) data.get(0), (List<String>) data.get(1));
        } catch (ClassCastException e){
            view.printLobbyCommands(new ArrayList<>(Collections.singleton((String) data.get(0))), new ArrayList<>(Collections.singleton((String) data.get(1))));
        }
    }

    /**
     * Brief Sets the View's attributes basing on the instance of the data passed as a parameter
     * @param data contains the attributes to be set or eventually updated
     */
    @SuppressWarnings("unchecked")
    private void setAttributes(List<Object> data) {
        Object extractedData = data.get(0);
        if(extractedData instanceof String)
            view.removeColor((String) extractedData, (String) data.get(1));
        else if(extractedData instanceof List)
            View.setCardsInGame((List<ImmutableCard>) extractedData);
        else if(extractedData instanceof ImmutablePosition)
            View.setActivePosition((ImmutablePosition) extractedData);
    }

    /**
     * Brief Extracts the parameters contained in the list of sensible information originally contained in the message received from the server
     * @param data is the list to analyze
     */
    @SuppressWarnings("unchecked")
    private void extractParameters(List<Object> data) {
        for(Object extractedData : data){
            if(extractedData instanceof Integer){
                if(View.getDefaultHeight() == null) //if height hasn't been set yet
                    View.setDefaultHeight((Integer) extractedData);
                else
                    View.setDefaultWidth((Integer) extractedData);
            }
            else if(extractedData instanceof List)
                if(((List<?>) extractedData).get(0) instanceof String) {
                    List<String> values = (List<String>) extractedData;
                    for (String value : values)
                        view.addName(value);
                }
        }
    }

    /**
     * Brief Unwraps the board from the data originally contained in the message received from the server, then sends it to the View
     * @param data is the list to analyze
     */
    @SuppressWarnings("unchecked")
    private void unwrapBoard(List<Object> data){
        Object[][] board = null;
        List<ImmutablePosition> positions = null;
        for (Object extractedData : data) {
            if(extractedData instanceof Object[][])
                board = (Object[][]) extractedData;
            else if(extractedData instanceof List)
                positions = (List<ImmutablePosition>) extractedData;
        }
        view.updateScreen(board, positions);
    }

    /**
     * Brief Extracts the data from the list of sensible information originally contained in the message received from the server, then queries the View
     * @param data is the list to analyze
     * @param onCompletion called when completed
     */
    @SuppressWarnings("unchecked")
    private void getMode(List<Object> data, Consumer<Integer> onCompletion){
        List<String> availableModes = null;
        List<Integer> correctInputs = null;
        for (Object extractedData : data) {
            if (extractedData instanceof List)
                if (((List<?>) extractedData).get(0) instanceof String)
                    availableModes = (List<String>) extractedData;
                else if (((List<?>) extractedData).get(0) instanceof Integer)
                    correctInputs = (List<Integer>) extractedData;
        }
        view.chooseGameMode(availableModes, correctInputs, onCompletion);
    }

    /**
     * Brief Extracts the data from the list of sensible information originally contained in the message received from the server, then queries the View
     * @param data is the list to analyze
     * @param onCompletion called when completed
     */
    private void getUsername(List<Object> data, Consumer<String> onCompletion){
        Integer playerId = null;
        for (Object extractedData : data) {
            if (extractedData instanceof Integer)
                playerId = (Integer) extractedData;
        }
        view.chooseUsername(playerId, onCompletion);
    }

    /**
     * Brief Extracts the data from the list of sensible information originally contained in the message received from the server, then queries the View
     * @param data is the list to analyze
     * @param onCompletion called when completed
     */
    @SuppressWarnings("unchecked")
    private void getVote(List<Object> data, Consumer<Integer> onCompletion){
        Map<Integer, String> players = null;
        String question = "";
        for (Object extractedData : data) {
            if (extractedData instanceof Map)
                players = (Map<Integer, String>) extractedData;
            else if(extractedData instanceof String)
                question = (String) extractedData;
        }
        view.getVote(players, question, onCompletion);
    }

    /**
     * Brief Extracts the data from the list of sensible information originally contained in the message received from the server, then queries the View
     * @param data is the list to analyze
     * @param onCompletion called when completed
     */
    @SuppressWarnings("unchecked")
    private void getFirst(List<Object> data, Consumer<Integer> onCompletion){
        List<String> names = null;
        for (Object extractedData : data) {
            if (extractedData instanceof List)
                names = (List<String>) extractedData;
        }
        view.chooseFirstPlayer(names, onCompletion);
    }

    /**
     * Brief Extracts the data from the list of sensible information originally contained in the message received from the server, then queries the View
     * @param data is the list to analyze
     * @param onCompletion is the List of Integers for the cards
     */
    @SuppressWarnings("unchecked")
    private void getCards(List<Object> data, Consumer<List<Integer>> onCompletion){
        List<ImmutableCard> cards = null;
        int numOfSelections = 0;
        for (Object extractedData : data) {
            if (extractedData instanceof List)
                cards = (List<ImmutableCard>) extractedData;
            else if(extractedData instanceof Integer)
                numOfSelections = (Integer) extractedData;
        }
        view.chooseCards(cards, numOfSelections, onCompletion);
    }

    /**
     * Brief Extracts the data from the list of sensible information originally contained in the message received from the server, then queries the View
     * @param data is the list to analyze
     * @param onCompletion called when completed
     */
    @SuppressWarnings("unchecked")
    private void getWorker(List<Object> data, Consumer<Integer> onCompletion){
        List<Integer> correctInputs = null;
        List<String> genders = null;
        for (Object extractedData : data) {
            if (extractedData instanceof List) {
                if (((List<?>) extractedData).get(0) instanceof Integer)
                    correctInputs = (List<Integer>) extractedData;
                else if (((List<?>) extractedData).get(0) instanceof String)
                    genders = (List<String>) extractedData;
            }
        }
        view.chooseWorker(correctInputs, genders, onCompletion);
    }

    /**
     * Brief Extracts the data from the list of sensible information originally contained in the message received from the server, then queries the View
     * @param data is the list to analyze
     * @param onCompletion called when completed
     */
    @SuppressWarnings("unchecked")
    private void getPosition(List<Object> data, Consumer<ImmutablePosition> onCompletion){
        List<ImmutablePosition> allowedPositions = null;
        ImmutablePosition initialPosition = null;
        String currentPhaseMessage = "";
        for (Object extractedData : data) {
            if (extractedData instanceof List)
                allowedPositions = (List<ImmutablePosition>) extractedData;
            else if (extractedData instanceof String)
                currentPhaseMessage = (String) extractedData;
            else if(extractedData instanceof ImmutablePosition)
                initialPosition = (ImmutablePosition) extractedData;
        }
        view.choosePosition(allowedPositions, currentPhaseMessage, initialPosition, onCompletion);
    }

    /**
     * Brief Extracts the data from the list of sensible information originally contained in the message received from the server, then queries the View
     * @param data is the list to analyze
     * @param onCompletion called when completed
     */
    @SuppressWarnings("unchecked")
    private void getBlock(List<Object> data, Consumer<Integer> onCompletion){
        List<Integer> correctInputs;
        List<String> blocksNames = new ArrayList<>();
        for (Object extractedData : data) {
            if (extractedData instanceof List) {
                correctInputs = (List<Integer>) extractedData;
                for (Integer correctInput : correctInputs) blocksNames.add(BlockLevel.values()[correctInput].getName());
            }
        }
        List<Integer> userInputs = new ArrayList<>();
        for (int i = 0; i < blocksNames.size(); i++) userInputs.add(i);
        view.chooseBlockType(userInputs, blocksNames, onCompletion);
    }

    public View getView() {
        return view;
    }
}

