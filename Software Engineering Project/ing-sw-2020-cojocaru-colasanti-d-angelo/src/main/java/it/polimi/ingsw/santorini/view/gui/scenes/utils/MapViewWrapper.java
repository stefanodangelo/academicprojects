package it.polimi.ingsw.santorini.view.gui.scenes.utils;

import it.polimi.ingsw.santorini.communication.ImmutablePosition;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;

import java.util.List;

public class MapViewWrapper {

    private final MapView mapView;

    /**
     * Brief the size of the map
     */
    private final static int mapSize = 5;

    public MapViewWrapper(StackPane frame, MapViewDelegate delegate) {
        mapView = new MapView(frame, delegate);
    }

    public MapView getMapView() {
        return mapView;
    }

    /**
     * Brief updates the local scene map
     * @param board the new incoming board
     * @param validPositions the valid positions (selectable)
     */
    public void updateScreen(Object[][] board, List<ImmutablePosition> validPositions) {
        mapView.reset();
        deserializeCells(board);
        runSafely(mapView::updateMap);
    }

    /**
     * Brief cells deserialization
     * @param board the incoming board
     */
    private void deserializeCells(Object[][] board) {
        Object[][] invertedBoard = invertBoard(board);
        for(int x = 0; x < mapSize; x++)
            for (int y = 0; y < mapSize; y++)
                deserializeCell(invertedBoard[x][y], x, y);
    }

    /**
     * Brief inverts a given board (matrix transpose)
     * @param board the incoming board
     * @return the inverted board
     */
    private Object[][] invertBoard(Object[][] board) {
        Object[][] invertedBoard = new Object[mapSize][mapSize];
        for (int x = 0; x < mapSize; x++){
            for (int y = 0; y < mapSize; y++)
                invertedBoard[y][x] = board[x][y];
        }
        return invertedBoard;
    }

    /**
     * Brief performs single cell deserialization
     * @param cell the cell
     * @param x the row
     * @param y the column
     */
    @SuppressWarnings({"unchecked"})
    private void deserializeCell(Object cell, int x, int y) {
        if (cell instanceof List)  //position is occupied
            extractCellContent((List<Object>) cell, x, y);
        else
            mapView.setBuildingLevel((Integer) cell, x, y); //level in position (i, j)
    }

    /**
     * Brief extracts the cell's contents
     * @param cell the cell
     * @param x the x position
     * @param y the y position
     */
    private void extractCellContent(List<Object> cell, int x, int y) {
        String color = "", gender = "";
        for (Object content : cell) {
            if (content instanceof String) { //the content is a worker
                try {
                    it.polimi.ingsw.santorini.view.Color.valueOf((String) content);
                    color = (String) content;
                } catch (IllegalArgumentException e) {
                    gender = (String) content;
                }
            } else //the content is a building
                mapView.setBuildingLevel((Integer) content, x, y);
        }
        mapView.setWorker(color, gender, x, y);
    }

    /**
     * Brief runs code safely on JavaFX Thread
     */
    private void runSafely(Runnable runnable) {
        Platform.runLater(runnable);
    }
}
