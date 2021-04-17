package it.polimi.ingsw.santorini.view.gui.scenes.utils;

import it.polimi.ingsw.santorini.communication.ImmutablePosition;
import it.polimi.ingsw.santorini.view.gui.scenes.GUIScene;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MapView extends GridPane {

    /**
     * Brief the map size
     */
    private final static int mapSize = 5;

    /**
     * Brief the 2D list of building levels across the entire map
     */
    private final List<List<Integer>> buildingLevels = new ArrayList<>();

    /**
     * Brief the 2D list of workers across the entire map
     */
    private List<List<MapWorker>> workers = new ArrayList<>();

    /**
     * Brief the list of the current selectables pushed across the map
     */
    private List<Pane> selectables = new ArrayList<>();

    /**
     * Brief the current position of the active worker
     */
    private Pane currentPosition;

    private final MapViewDelegate delegate;

    private final StackPane frame;

    public MapView(StackPane frame, MapViewDelegate delegate) {
        this.frame = frame;
        this.delegate = delegate;
        load();
        initialize2DList(buildingLevels);
        initialize2DList(workers);
    }

    public void load() {
        for (int i = 0; i < 5; i++) {
            ColumnConstraints c = new ColumnConstraints();
            c.prefWidthProperty().bind(frame.widthProperty().multiply(0.074803141));
            getColumnConstraints().add(c);

            RowConstraints r = new RowConstraints();
            r.prefHeightProperty().bind(getColumnConstraints().get(0).prefWidthProperty());
            getRowConstraints().add(r);
        }
        vgapProperty().bind(maxWidthProperty().subtract(frame.widthProperty().multiply(0.075).multiply(5)).divide(4));
        hgapProperty().bind(vgapProperty());

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                StackPane p1 = new StackPane();
                //p1.setStyle("-fx-background-color: #420fff");
                p1.prefWidthProperty().bind(getColumnConstraints().get(0).prefWidthProperty());
                p1.prefHeightProperty().bind(p1.prefWidthProperty());
                GridPane.setConstraints(p1, i, j);
                getChildren().add(p1);
            }
        }
    }

    /**
     * Brief getter for the workers 2D list
     * @return the workers 2D list
     */
    public List<List<MapWorker>> getWorkers() {
        return workers;
    }

    /**
     * Brief Resets state of map updates receiving
     */
    public void reset() {
        resetWorkers();
    }

    /**
     * Brief Resets the 2D list of workers
     */
    private void resetWorkers() {
        workers = new ArrayList<>();
        initialize2DList(workers);
    }

    /**
     * Brief static method to fully initialize a given by reference 2D list
     * @param list the incoming list
     * @param <T> the type of the list's elements
     */
    private static <T> void initialize2DList(List<List<T>> list) {
        for (int x = 0; x < mapSize; x++) {
            list.add(new ArrayList<>());
            for (int y = 0; y < mapSize; y++)
                list.get(x).add(null);
        }
    }

    /**
     * Brief sets a worker in a given position in the 2D workers list
     * @param color the color of the worker
     * @param gender the gender of the worker
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void setWorker(String color, String gender, int x, int y) {
        workers.get(x).set(y, new MapWorker(color, gender));
    }

    /**
     * Brief setter for the building level
     * @param level the building level of the cell
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void setBuildingLevel(Integer level, int x, int y) {
        buildingLevels.get(x).set(y, level);
    }

    /**
     * Brief pushes a block in a given map position
     * @param level the level of the block
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void pushBlock(Integer level, Integer x, Integer y) {
        StackPane stack = getStackFromGrid(x, y);

        Pane p = new Pane();
        //p.setStyle("-fx-background-color: #ffffff");
        p.maxWidthProperty().bind(stack.widthProperty());
        p.maxHeightProperty().bind(stack.heightProperty());
        p.prefWidthProperty().bind(p.maxWidthProperty());
        p.prefHeightProperty().bind(p.maxHeightProperty());
        stack.getChildren().add(p);

        ImageView pro = new ImageView();
        Image prova = null;
        try {
            prova = new Image(Objects.requireNonNull(GUIScene.class.getResource(
                    "/images/Blocks/" + (level < 4 ? "BuildingBlock0" + level : "Dome") + ".png"))
                    .toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        pro.setImage(prova);
        pro.setViewport(new Rectangle2D(150, 70, 340, 340));
        pro.setSmooth(true);
        pro.setCache(true);
        pro.fitWidthProperty().bind(p.widthProperty());
        pro.fitHeightProperty().bind(p.heightProperty());
        p.getChildren().add(pro);
    }

    /**
     * Brief pushes a worker in a given position on the map
     * @param worker the worker
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void pushWorker(MapWorker worker, Integer x, Integer y) {
        StackPane stack = getStackFromGrid(x, y);

        boolean female = worker.getGender().equals("female");
        Integer color = worker.getColor();

        Pane p = new Pane();
        //p.setStyle("-fx-background-color: #ffffff");
        p.maxWidthProperty().bind(stack.widthProperty());
        p.maxHeightProperty().bind(stack.heightProperty());
        p.prefWidthProperty().bind(p.maxWidthProperty());
        p.prefHeightProperty().bind(p.maxHeightProperty());
        stack.getChildren().add(p);

        ImageView pro = new ImageView();
        Image prova = null;
        try {
            prova = new Image(Objects.requireNonNull(GUIScene.class
                    .getResource( "/images/Workers/" + (female ? "Female" : "Male") + "Builder" + color + ".png"))
                    .toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        pro.setImage(prova);
        pro.setViewport(new Rectangle2D(150, 70, 340, 340));
        pro.setSmooth(true);
        pro.setCache(true);
        pro.fitWidthProperty().bind(p.widthProperty());
        pro.fitHeightProperty().bind(p.heightProperty());
        p.getChildren().add(pro);
    }

    /**
     * Brief Gets the stack from the cell in the provided position
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the stack pane in the given position
     */
    public StackPane getStackFromGrid(int x, int y) {
        return (StackPane) getChildren().stream().filter((node) ->
                GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y
        ).collect(Collectors.toList()).get(0);
    }

    /**
     * Brief clears the map from all its pushed elements
     */
    public void clearMap() {
        for (int i = 0; i < mapSize; i++)
            for (int j = 0; j < mapSize; j++)
                getStackFromGrid(i, j).getChildren().removeIf(stack -> true);
    }

    /**
     * Brief updates the map (first clearing it)
     */
    public void updateMap() {
        clearMap();
        for (int x = 0; x < mapSize; x++)
            for (int y = 0; y < mapSize; y++)
                push(x, y);
    }

    /**
     * Brief pushes all the incoming selectable positions on the map
     * @param positions the selectable positions
     */
    public void selectablePositions(List<ImmutablePosition> positions) {
        for (ImmutablePosition position : positions) {
            int x = position.getX(), y = position.getY();
            selectables.add(pushSelectable(x, y));
        }
    }

    /**
     * Brief sets and pushes on the map the current active worker's position
     * @param position the position of the active worker
     */
    public void currentPosition(ImmutablePosition position) {
        currentPosition = pushCurrentPosition(position.getX(), position.getY());
    }

    /**
     * Brief pushes basing on current state the objects available for the given position
     * @param x the x coordinate
     * @param y the y coordinate
     */
    private void push(int x, int y) {
        Integer block = buildingLevels.get(x).get(y);
        MapWorker worker = workers.get(x).get(y);
        if (block > 0) pushBlock(block, x, y);
        if (worker != null) pushWorker(worker, x, y);
    }

    /**
     * Brief removes all the selectables from the map
     */
    private void removeSelectables() {
        for (Pane selectable : selectables)
            ((StackPane) selectable.getParent()).getChildren().remove(selectable);
        selectables = new ArrayList<>();
    }

    /**
     * Brief pushes the current position indicator in the given position
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the pushed current position indicator
     */
    private Pane pushCurrentPosition(int x, int y) {
        StackPane stack = getStackFromGrid(x, y);
        Pane p = new Pane();
        p.setStyle("-fx-background-color: springgreen");
        p.setOpacity(0.6);
        stack.getChildren().add(stack.getChildren().size() - 1, p);
        return p;
    }

    /**
     * Brief removes the current position
     */
    private void removeCurrentPosition() {
        ((StackPane) currentPosition.getParent()).getChildren().remove(currentPosition);
    }

    /**
     * Brief pushes a selectable in the given position
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the pushed selectable indicator
     */
    private Pane pushSelectable(int x, int y) {
        StackPane stack = getStackFromGrid(x, y);
        Pane p = new Pane();
        p.setStyle("-fx-background-color: white");
        p.setOpacity(0.6);
        Pane worker = null;
        if (workers.get(x).get(y) != null)
            worker = (Pane) stack.getChildren().remove(stack.getChildren().size() - 1);
        stack.getChildren().add(p);
        if (worker != null) stack.getChildren().addAll(worker);

        EventHandler<MouseEvent> click = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                removeSelectables();
                delegate.onPositionSelected(new ImmutablePosition(x, y));
            }
        };
        EventHandler<MouseEvent> mouseIn = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                p.setOpacity(0.8);
            }
        };
        EventHandler<MouseEvent> mouseOut = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                p.setOpacity(0.6);
            }
        };

        if (worker == null) {
            p.setOnMouseClicked(click);
            p.setOnMouseEntered(mouseIn);
            p.setOnMouseExited(mouseOut);
        } else {
            worker.setOnMouseClicked(click);
            worker.setOnMouseEntered(mouseIn);
            worker.setOnMouseExited(mouseOut);
        }
        return p;
    }
}
