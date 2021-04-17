package it.polimi.ingsw.santorini.view.gui.controllers;

import it.polimi.ingsw.santorini.communication.ImmutableCard;
import it.polimi.ingsw.santorini.communication.ImmutablePosition;
import it.polimi.ingsw.santorini.view.Color;
import it.polimi.ingsw.santorini.view.gui.controllers.delegates.GameSceneControllerDelegate;
import it.polimi.ingsw.santorini.view.gui.scenes.GameScene;
import it.polimi.ingsw.santorini.view.gui.scenes.delegates.GameSceneDelegate;
import it.polimi.ingsw.santorini.view.gui.scenes.utils.MapWorker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Brief the game scene controller (the map and all the game action takes part here)
 * @see GameScene
 * @see GameSceneControllerDelegate
 * @see GUISceneController
 */
public class GameSceneController extends GUISceneController<GameScene, GameSceneControllerDelegate> implements GameSceneDelegate {

    /**
     * Brief the position selection request
     */
    private Consumer<ImmutablePosition> positionRequest;

    /**
     * Brief the block type selection request
     */
    private Consumer<Integer> blockTypeRequest;

    /**
     * Brief the skip request
     */
    private Consumer<Boolean> skipRequest;

    /**
     * Brief the undo request
     */
    private Consumer<Boolean> undoRequest;

    /**
     * Brief the worker selection request
     */
    private Consumer<Integer> workerRequest;

    /**
     * Brief the workers selection io map
     */
    private Map<ImmutablePosition, Integer> workersIoMap;

    /**
     * Brief indicates whether the current position selection is a worker selection one or not
     */
    private Boolean workerSelection = false;

    /**
     * Brief the size of the map
     */
    private final static int mapSize = 5;

    /**
     * Brief cards in Game
     */
    private List<ImmutableCard> cardsInGame = new ArrayList<>();

    /**
     * Brief players in Game
     */
    private List<String> playersInGame = new ArrayList<>();

    /**
     * Brief main constructor: sets the delegate and loads the scene
     * @param delegate the delegate
     */
    public GameSceneController(GameSceneControllerDelegate delegate, List<ImmutableCard> cardsInGame, List<String> playersInGame) {
        super(new GameScene(), delegate);
        this.cardsInGame = cardsInGame;
        this.playersInGame = playersInGame;
        scene().setDelegate(this);
        scene().loadScene();
        loadingCompleted();
    }

    /**
     * Brief asks the scene to load the position selection
     * @param positions the available positions
     * @param selectionTypeMessage the selection type
     * @param currentPosition the current worker position (optional)
     * @param onCompletion the on completion lambda
     */
    public void choosePosition(List<ImmutablePosition> positions, String selectionTypeMessage, ImmutablePosition currentPosition,
                   Consumer<ImmutablePosition> onCompletion) {
        positionRequest = onCompletion;
        workerSelection = false;
        runSafely(() -> {
            scene().getMapViewWrapper().getMapView().selectablePositions(invertPositions(positions));
            scene().executingOperation(selectionTypeMessage);
            if (currentPosition != null) scene().getMapViewWrapper().getMapView().currentPosition(invertPosition(currentPosition));
        });
    }

    /**
     * Brief asks the scene to perform the block type selection
     * @param blockTypes the io map available block types for selection
     * @param blockNames the available block types for selection
     * @param onCompletion the on completion lambda
     */
    public void chooseBlockType(List<Integer> blockTypes, List<String> blockNames, Consumer<Integer> onCompletion) {
        blockTypeRequest = onCompletion;
        runSafely(() -> scene().blockTypeSelection(blockNames));
    }

    /**
     * Brief asks the scene to perform a skip operation question
     * @param operationType the operation under question
     * @param onCompletion the on completion lambda
     */
    public void skipOperation(String operationType, Consumer<Boolean> onCompletion) {
        skipRequest = onCompletion;
        runSafely(() -> scene().skipOrDo(operationType));
    }

    /**
     * Brief asks the scene to perform an undo request
     * @param onCompletion the on completion lambda
     */
    public void onUndoRequest(Consumer<Boolean> onCompletion) {
        undoRequest = onCompletion;
        runSafely(() -> scene().undoRequest());
    }

    /**
     * Brief updates the local scene map
     * @param board the new incoming board
     * @param validPositions the valid positions (selectable)
     */
    public void updateScreen(Object[][] board, List<ImmutablePosition> validPositions) {
        scene().getMapViewWrapper().updateScreen(board, validPositions);
    }

    /**
     * Brief asks the scene to load worker selection
     * @param correctInputs the available correct io map
     * @param genders the ordered genders of the selectable workers
     * @param playerColor the current player's color
     * @param onCompletion the on completion lambda
     */
    public void chooseWorker(List<Integer> correctInputs, List<String> genders, Color playerColor, Consumer<Integer> onCompletion) {
        workerRequest = onCompletion;
        workerSelection = true;
        List<String> convertedGenders = genders.stream().map(MapWorker::getGenderConversion).collect(Collectors.toList());
        Integer color = playerColor.ordinal();
        List<List<MapWorker>> workers = scene().getMapViewWrapper().getMapView().getWorkers();
        List<ImmutablePosition> workersPositions = new ArrayList<>();
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                MapWorker worker = workers.get(x).get(y);
                if (worker != null && worker.getColor().equals(color)) workersPositions.add(new ImmutablePosition(x, y));
            }
        }
        workersIoMap = new HashMap<>();
        for (ImmutablePosition workerPosition : workersPositions) {
            MapWorker worker = workers.get(workerPosition.getX()).get(workerPosition.getY());
            workersIoMap.put(workerPosition, convertedGenders.indexOf(worker.getGender()));
        }
        runSafely(() -> scene().getMapViewWrapper().getMapView().selectablePositions(workersPositions));
    }

    /**
     * Brief inverts a single position
     * @param position the incoming position
     * @return the inverted position
     */
    private static ImmutablePosition invertPosition(ImmutablePosition position) {
        return new ImmutablePosition(position.getY(), position.getX());
    }

    /**
     * Brief inverts a list of positions
     * @param positions teh incoming list of positions
     * @return the inverted list
     */
    private static List<ImmutablePosition> invertPositions(List<ImmutablePosition> positions) {
        return positions.stream().map(GameSceneController::invertPosition).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPositionSelected(ImmutablePosition position) {
        if (!workerSelection) runAsync(() -> positionRequest.accept(invertPosition(position)));
        else runAsync(() -> workerRequest.accept(workersIoMap.get(position)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSkipOrDoAnswer(Boolean skip) {
        runAsync(() -> skipRequest.accept(skip));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUndoAnswer(Boolean undo) {
        runAsync(() -> undoRequest.accept(undo));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBlockSelected(Integer choice) {
        runAsync(() -> blockTypeRequest.accept(choice));
    }
}
