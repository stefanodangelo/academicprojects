package it.polimi.ingsw.santorini.view.gui.controllers;

import it.polimi.ingsw.santorini.communication.ImmutableCard;
import it.polimi.ingsw.santorini.view.Color;
import it.polimi.ingsw.santorini.view.gui.controllers.delegates.SetupSceneControllerDelegate;
import it.polimi.ingsw.santorini.view.gui.scenes.SetupScene;
import it.polimi.ingsw.santorini.view.gui.scenes.delegates.SetupSceneDelegate;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Brief Handles the setup phase setting repeatedly every needed parameter whenever requested
 * @see SetupScene
 * @see SetupSceneControllerDelegate
 * @see GUISceneController
 */
public class SetupSceneController extends GUISceneController<SetupScene, SetupSceneControllerDelegate> implements SetupSceneDelegate {

    /**
     * Brief the generic choice request
     */
    private Consumer<Integer> choiceRequest;

    /**
     * Brief the color request
     */
    private Consumer<String> colorRequest;

    /**
     * Brief the card request
     */
    private Consumer<List<Integer>> cardsRequest;

    /**
     * Brief the currently selected cards
     */
    private final Set<Integer> selectedCards = new HashSet<>();

    /**
     * Brief the current io map: masks the local output into an external one as required
     */
    private Map<Integer, Integer> ioMap = new HashMap<>();

    /**
     * Brief main constructor: it sets the delegate and loads the scene
     * @param delegate
     */
    public SetupSceneController(SetupSceneControllerDelegate delegate) {
        super(new SetupScene(), delegate);
        scene().setDelegate(this);
        runSafely(() -> {
            loadScene();
            waitOtherPlayers();
            loadingCompleted();
        });
    }

    /**
     * Brief produces a standard io map (numerically ordered)
     * @param length the length of the io map
     * @param inverted if true the map will be inverted
     * @param incremented if true the map will be incremented by one
     * @return the io map
     */
    private Map<Integer, Integer> getNewStandardIoMap(Integer length, Boolean inverted, Boolean incremented) {
        HashMap<Integer, Integer> ioMap = new HashMap<>();
        for (int i = 0; i < length; i++) ioMap.put(!inverted ? i : (length - i - 1), !incremented ? i : i + 1);
        return ioMap;
    }

    /**
     * Brief produces a color request io map
     * @param workersColors the list of the available colors
     * @return the io map
     */
    private Map<Integer, Integer> getColorIoMap(List<Color> workersColors) {
        return getNewStandardIoMap(workersColors.size(), false, false);
    }

    /**
     * Brief produces a mode request io map
     * @return the io map
     */
    private Map<Integer, Integer> getModeIoMap() {
        return getNewStandardIoMap(3, false, false);
    }

    /**
     * Brief produces a poll request io map
     * @return the io map
     */
    private Map<Integer, Integer> getPollIoMap() {
        return getNewStandardIoMap(3, false, true);
    }

    /**
     * Brief produces a first player io map
     * @return the io map
     */
    private Map<Integer, Integer> getFirstPlayerIoMap() {
        return getNewStandardIoMap(3, false, true);
    }

    /**
     * Brief produces a cards selection request io map
     * @param cards the available cards
     * @return the io map
     */
    private Map<Integer, Integer> getCardsIoMap(List<ImmutableCard> cards) {
        Map<Integer, Integer> ioMap = new HashMap<>();
        for (int i = 0; i < cards.size(); i++) ioMap.put(i, cards.get(i).getId());
        return ioMap;
    }

    /**
     * Brief sets the current io map
     * @param ioMap the setting io map
     */
    private void setIoMap(Map<Integer, Integer> ioMap) {
        this.ioMap = ioMap;
    }

    /**
     * Brief ports a local output in to the external one through the current io map
     * @param input the local output (input to be converted)
     * @return the external output
     */
    private Integer getOutput(Integer input) {
        return ioMap.get(input);
    }

    /**
     * Brief ports a local output list in to the external one through the current io map
     * @param input the local output list (input list to be converted)
     * @return the external output list
     */
    private List<Integer> getOutput(List<Integer> input) {
        return input.stream().map(this::getOutput).collect(Collectors.toList());
    }

    /**
     * Brief asks the scene to go waiting mode
     */
    public void waitOtherPlayers() {
        runSafely(() -> scene().loadWaiting());
    }

    /**
     * Brief asks the scene to load game mode selection
     * @param availableModes the available modes
     * @param correctInputs the correct io map (known convention)
     * @param onCompletion the on completion lambda
     */
    public void chooseGameMode(List<String> availableModes, List<Integer> correctInputs, Consumer<Integer> onCompletion) {
        choiceRequest = onCompletion;
        setIoMap(getModeIoMap());
        runSafely(() -> scene().loadChoiceSelection(SetupScene.Selection.MODE, availableModes));
    }

    /**
     * Brief asks the scene to load color selection
     * @param workersColors the available colors
     * @param onCompletion the on completion lambda
     */
    public void selectColor(List<Color> workersColors, Consumer<String> onCompletion) {
        colorRequest = onCompletion;
        setIoMap(getColorIoMap(workersColors));
        runSafely(() -> scene().loadColorSelection(workersColors));
    }

    /**
     * Brief asks the scene to load vote selection mode
     * @param players the available players to vote
     * @param question the question
     * @param onCompletion the on completion lambda
     */
    public void getVote(Map<Integer, String> players, String question, Consumer<Integer> onCompletion) {
        choiceRequest = onCompletion;
        setIoMap(getPollIoMap());
        runSafely(() -> scene().loadChoiceSelection(question, new ArrayList<>(players.values())));
    }

    /**
     * Brief asks the scene to load the first player selection mode
     * @param names the available selectable names
     * @param onCompletion the on completion lambda
     */
    public void chooseFirstPlayer(List<String> names, Consumer<Integer> onCompletion) {
        choiceRequest = onCompletion;
        setIoMap(getFirstPlayerIoMap());
        runSafely(() -> scene().loadChoiceSelection(SetupScene.Selection.FIRST, names));
    }

    /**
     * Brief asks the scene to load the cards selection mode
     * @param cards the available cards for selection
     * @param numberOfSelections the number of selections to perform
     * @param onCompletion the on completion lambda
     */
    public void chooseCards(List<ImmutableCard> cards, Integer numberOfSelections, Consumer<List<Integer>> onCompletion) {
        cardsRequest = onCompletion;
        setIoMap(getCardsIoMap(cards));
        runSafely(() -> scene().loadCardsSelection(cards, numberOfSelections));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChoiceSelected(Integer choice) {
        System.out.println(getOutput(choice));
        runAsync(() -> choiceRequest.accept(getOutput(choice)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onColorSelected(Integer choice, List<it.polimi.ingsw.santorini.view.Color> workersColors) {
        System.out.println(choice);
        Integer output = getOutput(choice);
        String outputString = workersColors.get(output).toString();
        runAsync(() -> colorRequest.accept(outputString));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCardSelected(Integer card) {
        selectedCards.add(card);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCardUnselected(Integer card) {
        selectedCards.remove(card);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCardsConfirmed() {
        runAsync(() -> cardsRequest.accept(getOutput(new ArrayList<>(selectedCards))));
    }
}
