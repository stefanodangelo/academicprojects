package it.polimi.ingsw.santorini.view.gui.scenes;

import it.polimi.ingsw.santorini.communication.ImmutableCard;
import it.polimi.ingsw.santorini.communication.ImmutablePosition;
import it.polimi.ingsw.santorini.view.gui.scenes.delegates.GameSceneDelegate;
import it.polimi.ingsw.santorini.view.gui.scenes.utils.CustomFont;
import it.polimi.ingsw.santorini.view.gui.scenes.utils.MapView;
import it.polimi.ingsw.santorini.view.gui.scenes.utils.MapViewDelegate;
import it.polimi.ingsw.santorini.view.gui.scenes.utils.MapViewWrapper;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URISyntaxException;
import java.util.*;

/**
 * Brief the game scene (handles the game map and all the player's in-game choices
 * @see it.polimi.ingsw.santorini.view.gui.controllers.GameSceneController
 * @see GameSceneDelegate
 * @see GUIScene
 */
public class GameScene extends GUIScene<GameSceneDelegate> implements MapViewDelegate {

    /**
     * Brief the scene default width
     */
    private final static Double defaultWidth = 1422.2222222222222222222222222222d;

    /**
     * Brief the scene default height
     */
    private final static Double defaultHeight = 800d;

    /**
     * Brief the scene's title
     */
    private final static String title = "Santorini";

    /**
     * Brief the scene frame's sizes ratio
     */
    final double ratio = 1.7777777777777777777777777777778;

    /**
     * Brief the map view
     */
    private MapView mapView;

    private MapViewWrapper mapViewWrapper;

    /**
     * Brief the scene's frame
     */
    private StackPane frame;

    /**
     * Brief the border pane
     */
    private BorderPane borderPane;

    /**
     * Brief the background image view
     */
    private ImageView backgroundView;

    /**
     * Brief the scene's panel
     */
    private Pane panel;

    /**
     * Brief the selection text (on top of the map during a position selection)
     */
    private Text selectionText;

    /**
     * Brief clip used for dynamic blur effect
     */
    private final Rectangle clip = new Rectangle();

    /**
     * Brief another clip used for dynamic blur effect
     */
    private final Rectangle _clip = new Rectangle();

    private final IntegerProperty fontSize = new SimpleIntegerProperty(100);
    private final IntegerProperty border = new SimpleIntegerProperty(5);
    private final ObjectProperty<Node> clip1 = new SimpleObjectProperty<>(clip);
    private final ObjectProperty<Node> clip2 = new SimpleObjectProperty<>(_clip);

    private Timer timer;

    private Boolean activation = true;

    Pane leftPane;

    public MapViewWrapper getMapViewWrapper() {
        return mapViewWrapper;
    }

    /**
     * Brief the time counter for undo requests
     */
    private Integer time;

    @Override
    public void onPositionSelected(ImmutablePosition position) {
        selectionText.setVisible(false);
        runAsync(() -> delegate().onPositionSelected(position));
    }

    /**
     * Brief main constructor: loads the scene and initializes 2D lists
     */
    public GameScene() {
        super(defaultWidth,defaultHeight,title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void customLoadScene() {
        root().setStyle("-fx-background-color: #000000");

        frame = new StackPane();
        //frame.setStyle("-fx-background-color: #669900");
        frame.maxHeightProperty().bind(Bindings.min(root().heightProperty(), root().widthProperty().divide(ratio)));
        frame.maxWidthProperty().bind(frame.maxHeightProperty().multiply(ratio));

        Image backgroundImage = null;
        try {
            backgroundImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("board.png"))
                    .toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        backgroundView = new ImageView();
        backgroundView.setImage(backgroundImage);
        backgroundView.setSmooth(true);
        backgroundView.setCache(true);
        backgroundView.fitWidthProperty().bind(frame.maxWidthProperty());
        backgroundView.fitHeightProperty().bind(frame.maxHeightProperty());

        borderPane = new BorderPane();
        //borderPane.setStyle("-fx-background-color: #6d00f7");
        borderPane.maxWidthProperty().bind(frame.widthProperty());
        borderPane.maxHeightProperty().bind(frame.heightProperty());

        mapViewWrapper = new MapViewWrapper(frame, this);
        mapView = mapViewWrapper.getMapView();
        //gridPane.setStyle("-fx-background-color: #420000");
        StackPane centerPane = new StackPane();
        //centerPane.setStyle("-fx-background-color: purple");
        centerPane.prefWidthProperty().bind(frame.widthProperty().multiply(0.40885416666666666666666666666667));
        centerPane.prefHeightProperty().bind(frame.heightProperty());
        mapView.maxWidthProperty().bind(centerPane.widthProperty());
        mapView.maxHeightProperty().bind(mapView.maxWidthProperty());
        mapView.translateYProperty().bind(frame.heightProperty().multiply(0.007));

        selectionText = new Text();
        selectionText.setFont(new CustomFont().font(25));
        selectionText.setText("Select a place for your worker");
        selectionText.setFill(Paint.valueOf("white"));
        selectionText.translateYProperty().bind(centerPane.heightProperty().multiply(-0.42));
        selectionText.setVisible(false);

        centerPane.getChildren().addAll(mapView, selectionText);

        leftPane = new Pane();
        //leftPane.setStyle("-fx-background-color: #420000");
        leftPane.prefWidthProperty().bind(frame.widthProperty().subtract(centerPane.prefWidthProperty()).divide(2));
        leftPane.prefHeightProperty().bind(frame.heightProperty());
        Pane rightPane = new Pane();
        //rightPane.setStyle("-fx-background-color: #82000f");
        rightPane.prefHeightProperty().bind(frame.heightProperty());
        rightPane.prefWidthProperty().bind(frame.widthProperty().subtract(centerPane.prefWidthProperty()).divide(2));
        borderPane.setCenter(centerPane);
        borderPane.setLeft(leftPane);
        borderPane.setRight(rightPane);

        frame.getChildren().addAll(backgroundView);
        frame.getChildren().add(borderPane);
        loadLeft(leftPane, null, null);
        loadRight(rightPane);
        root().getChildren().add(frame);
    }

    /**
     * Brief loads the left pane
     * @param left the left pane to load
     */
    public void loadLeft(Pane left, List<ImmutableCard> cardsInGame, List<String> playersInGame) {
        Pane activeLeft = new Pane();
        activeLeft.setStyle("-fx-background-color: #ffffff");
        activeLeft.setOpacity(1);
        activeLeft.prefWidthProperty().bind(left.widthProperty().multiply(0.6));
        activeLeft.prefHeightProperty().bind(left.heightProperty());

        clip.widthProperty().bind(activeLeft.widthProperty());
        clip.heightProperty().bind(activeLeft.heightProperty());

        Image backgroundImage = null;
        try {
            backgroundImage = new Image(Objects.requireNonNull(GUIScene.class.getResource(
                    "/images/Game/board.png"))
                    .toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ImageView backgroundView = new ImageView();
        backgroundView.setImage(backgroundImage);
        backgroundView.setSmooth(true);
        backgroundView.setCache(true);
        backgroundView.fitWidthProperty().bind(frame.maxWidthProperty());
        backgroundView.fitHeightProperty().bind(frame.maxHeightProperty());

        backgroundView.clipProperty().bind(clip1);

        BoxBlur blur = new BoxBlur();
        blur.setIterations(3);
        blur.widthProperty().bind(activeLeft.widthProperty());
        blur.heightProperty().bind(activeLeft.heightProperty());

        backgroundView.setEffect(blur);

        StackPane activeStack = new StackPane();
        activeStack.prefWidthProperty().bind(activeLeft.widthProperty());
        activeStack.prefHeightProperty().bind(activeLeft.heightProperty());

        VBox vBox = new VBox();
        vBox.alignmentProperty().setValue(Pos.CENTER);
        //vBox.setStyle("-fx-background-color: #ff0fff");
        vBox.maxWidthProperty().bind(activeStack.widthProperty().multiply(0.9));
        vBox.maxHeightProperty().bind(activeLeft.heightProperty().subtract(activeLeft.heightProperty().multiply(0.01).multiply(2)));
        vBox.spacingProperty().bind(activeLeft.heightProperty().multiply(0.01));

        int a = 0;
        for (int i = 0; i < 3; i++) {
            StackPane cellWrapper = new StackPane();
            cellWrapper.prefHeightProperty().bind(vBox.heightProperty().subtract(vBox.spacingProperty().multiply(2)).divide(3));

            Pane cell = new Pane();
            cell.setOpacity(1);

            String player = "";
            switch (i) {
                case 0:
                    player = "Luca";
                    break;
                case 1:
                    player = "Stefano";
                    break;
                case 2:
                    player = "Cristian";
            }

            if (a == i) {
                Bloom bloom = new Bloom(0.95);
                cell.setEffect(bloom);

                activation = true;

                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        double t = bloom.getThreshold();
                        if (activation) {
                            t += 0.005;
                            if (t >= 0.95) {
                                t = 0.95;
                                activation = false;
                            }
                        } else {
                            t -= 0.005;
                            if (t <= 0.7) {
                                t = 0.7;
                                activation = true;
                            }
                        }
                        bloom.setThreshold(t);
                        cell.setEffect(bloom);
                    }
                }, 0, 40);
            }

            //col = "white";
            cell.styleProperty().bind(Bindings.concat(
                    "-fx-background-color: " + "white" + ";",
                    "-fx-background-radius: 30;",
                    "-fx-border-radius: 30;"
            ));
            cell.maxWidthProperty().bind(cellWrapper.widthProperty().multiply(i == a ? 0.87 : 0.77));
            cell.maxHeightProperty().bind(cellWrapper.heightProperty().multiply(i == a ? 0.95 : 0.85));

            VBox vBox1 = new VBox();
            vBox1.setSpacing(0);
            vBox1.prefWidthProperty().bind(cell.widthProperty());
            vBox1.prefHeightProperty().bind(cell.heightProperty());

            StackPane su = new StackPane();
            su.prefHeightProperty().bind(vBox1.heightProperty().multiply(0.2));

            Text playerName = new Text(player);
            Font font = new Font(20);
            playerName.setFont(font);
            //playerName.setFill(col);

            su.getChildren().addAll(playerName);

            StackPane sd = new StackPane();
            sd.prefWidthProperty().bind(vBox1.prefWidthProperty());
            sd.prefHeightProperty().bind(vBox1.prefHeightProperty().multiply(0.8));

            Image sd_backgroundImage = null;
            try {
                sd_backgroundImage = backgroundImage = new Image(Objects.requireNonNull(GUIScene.class.getResource(
                        "/images/godCards/" + 0 + (i + 1) + ".png"))
                        .toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            ImageView sd_backgroundView = new ImageView();
            sd_backgroundView.setImage(sd_backgroundImage);
            sd_backgroundView.setSmooth(true);
            sd_backgroundView.setCache(true);
            sd_backgroundView.setPreserveRatio(true);
            //sd_backgroundView.fitHeightProperty().bind(sd.prefHeightProperty().multiply(0.88));
            sd_backgroundView.fitWidthProperty().bind(sd.prefWidthProperty().multiply(0.52));
            //sd_backgroundView.fitWidthProperty().bind(sd.widthProperty().multiply(0.1));

            Image l_backgroundImage = null;
            try {
                l_backgroundImage = new Image(Objects.requireNonNull(GUIScene.class.getResource("/images/Workers/FemaleBuilder" + i + ".png"))
                        .toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            ImageView l_backgroundView = new ImageView();
            l_backgroundView.setImage(l_backgroundImage);
            l_backgroundView.setSmooth(true);
            l_backgroundView.setCache(true);
            l_backgroundView.setPreserveRatio(true);
            l_backgroundView.fitHeightProperty().bind(sd.prefWidthProperty().multiply(0.4));
            l_backgroundView.translateXProperty().bind(sd.prefWidthProperty().multiply(-1).multiply(0.38));

            //l.getChildren().addAll(l_backgroundView);

            Image r_backgroundImage = null;
            try {
                r_backgroundImage = new Image(Objects.requireNonNull(GUIScene.class.getResource("/images/Workers/MaleBuilder" + i + ".png"))
                        .toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            ImageView r_backgroundView = new ImageView();
            r_backgroundView.setImage(r_backgroundImage);
            r_backgroundView.setSmooth(true);
            r_backgroundView.setCache(true);
            r_backgroundView.setPreserveRatio(true);
            r_backgroundView.fitHeightProperty().bind(sd.prefWidthProperty().multiply(0.4));
            r_backgroundView.translateXProperty().bind(sd.prefWidthProperty().multiply(1).multiply(0.38));

            //r.getChildren().addAll(r_backgroundView);

            sd.getChildren().addAll(sd_backgroundView, l_backgroundView, r_backgroundView);
            vBox1.getChildren().addAll(su,sd);
            cell.getChildren().addAll(vBox1);
            cellWrapper.getChildren().addAll(cell);
            vBox.getChildren().addAll(cellWrapper);
        }

        activeStack.getChildren().addAll(vBox);
        activeLeft.getChildren().addAll(backgroundView, activeStack);
        left.getChildren().addAll(activeLeft);
    }

    /**
     * Brief loads the right pane
     * @param right the right pane
     */
    public void loadRight(Pane right) {
        Pane activeRight = new Pane();
        //activeRight.setStyle("-fx-background-color: #420000");
        //activeRight.setOpacity(0.6);
        activeRight.prefWidthProperty().bind(right.prefWidthProperty().multiply(0.7));
        activeRight.prefHeightProperty().bind(right.prefHeightProperty());
        activeRight.translateXProperty().bind(right.widthProperty().multiply(0.3));

        panel = new Pane();
        panel.setVisible(false);
        panel.prefWidthProperty().bind(activeRight.widthProperty().multiply(0.65));
        panel.prefHeightProperty().bind(activeRight.heightProperty().multiply(0.49));
        panel.translateXProperty().bind(activeRight.widthProperty().multiply(0.235));
        panel.translateYProperty().bind(activeRight.heightProperty().multiply(0.47));

        _clip.widthProperty().bind(panel.widthProperty());
        _clip.heightProperty().bind(panel.heightProperty());

        panel.styleProperty().bind(Bindings.concat(
                /*"-fx-background-color: #ffffff;",*/
                "-fx-background-radius: 10;",
                "-fx-border-radius: 10;",
                "-fx-border-color: white;",
                "-fx-border-width: 3;"
        ));

        Image backgroundImage = null;
        try {
            backgroundImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("board.png"))
                    .toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ImageView backgroundView = new ImageView();
        backgroundView.setImage(backgroundImage);
        backgroundView.setSmooth(true);
        backgroundView.setCache(true);
        backgroundView.fitWidthProperty().bind(frame.maxWidthProperty());
        backgroundView.fitHeightProperty().bind(frame.maxHeightProperty());
        backgroundView.viewportProperty().bind(new ObjectBinding<>() {
            @Override
            protected Rectangle2D computeValue() {
                {
                    super.bind(frame.maxWidthProperty());
                }

                double x, y;
                x = (frame.sceneToLocal(panel.localToScene(0,0)).getX() / frame.getMaxWidth()) * 1920.0d;
                y = (frame.sceneToLocal(panel.localToScene(0,0)).getY() / frame.getMaxHeight()) * 1080.0d;
                return new Rectangle2D(x, y, panel.getPrefWidth(), panel.getPrefHeight());
            }
        });

        backgroundView.clipProperty().bind(clip2);

        BoxBlur blur = new BoxBlur();
        blur.setIterations(3);
        blur.widthProperty().bind(panel.prefWidthProperty());
        blur.heightProperty().bind(panel.prefHeightProperty());

        backgroundView.setEffect(blur);

        DropShadow ds = new DropShadow();
        ds.offsetXProperty().bind(panel.prefWidthProperty().multiply(0.01));
        ds.offsetXProperty().bind(panel.prefHeightProperty().multiply(0.01));
        ds.setColor(Color.WHITE);

        panel.setEffect(ds);

        panel.getChildren().addAll(backgroundView);
        activeRight.getChildren().addAll(panel);
        right.getChildren().addAll(activeRight);
    }



    /**
     * Brief loads the right pane adding a hourglass with timing handling (for undo)
     */
    public void loadRightHourglass() {
        if (panel.getChildren().size() > 1) panel.getChildren().remove(panel.getChildren().size() - 1);
        VBox divider = new VBox();
        //divider.setStyle("-fx-background-color: #220000");
        divider.prefWidthProperty().bind(panel.widthProperty());
        divider.prefHeightProperty().bind(panel.heightProperty());

        StackPane u = new StackPane();
        //u.setStyle("-fx-background-color: #420000");
        u.prefWidthProperty().bind(panel.widthProperty());
        u.prefHeightProperty().bind(panel.heightProperty().multiply(0.48));

        Text hourglassText = new Text("5");
        Font font = new Font(30);
        hourglassText.setFont(font);

        timer = new Timer();
        time = 5;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time--;
                hourglassText.setText(time.toString());
                if (time.equals(0)) {
                    panel.setVisible(false);
                    delegate().onUndoAnswer(false);
                    cancel();
                }
            }
        },1000, 1000);

        Image ui = null;
        try {
            ui = new Image(Objects.requireNonNull(GUIScene.class.getResource("/images/Hourglass/hourglass1.png"))
                    .toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ImageView bui = new ImageView();
        bui.setImage(ui);
        bui.setSmooth(true);
        bui.setCache(true);
        bui.setPreserveRatio(true);
        bui.fitHeightProperty().bind(u.prefHeightProperty().multiply(0.6));
        //bui.translateYProperty().bind(u.heightProperty().multiply(-1).multiply(0.1));

        Pane d = new Pane();
        //d.setStyle("-fx-background-color: #420000");
        d.maxWidthProperty().bind(panel.widthProperty());
        d.maxHeightProperty().bind(panel.heightProperty().multiply(0.5));
        d.prefWidthProperty().bind(d.maxWidthProperty());
        d.prefHeightProperty().bind(d.maxHeightProperty());

        Pane dd = new Pane();
        //dd.setStyle("-fx-background-color: #420000");
        dd.prefWidthProperty().bind(panel.widthProperty());
        dd.prefHeightProperty().bind(panel.heightProperty().multiply(0.02));

        VBox buttDivider = new VBox();
        buttDivider.setFillWidth(false);
        //buttDivider.setStyle("-fx-background-color: #4200f0");
        buttDivider.spacingProperty().bind(d.heightProperty().multiply(0.05));
        buttDivider.prefWidthProperty().bind(d.widthProperty());
        buttDivider.prefHeightProperty().bind(d.heightProperty());
        buttDivider.setAlignment(Pos.CENTER);

        StackPane customButton = new StackPane();
        customButton.setStyle("-fx-background-color: red");
        customButton.prefWidthProperty().bind(buttDivider.widthProperty().multiply(0.8));
        customButton.prefHeightProperty().bind(buttDivider.heightProperty().multiply(0.45));

        Text undoText = new Text();
        undoText.setFont(new CustomFont().font(20));
        undoText.setText("UNDO");

        customButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                panel.setVisible(false);
                timer.cancel();
                delegate().onUndoAnswer(true);
            }
        });

        customButton.getChildren().add(undoText);
        buttDivider.getChildren().add(customButton);

        u.getChildren().addAll(bui,hourglassText);
        d.getChildren().addAll(buttDivider);
        divider.getChildren().addAll(u,d,dd);
        panel.getChildren().addAll(divider);
        panel.setVisible(true);
    }

    /**
     * Brief loads the right pane adding a binary choice to it
     * @param white true if the requested color is white or green and red
     * @param s1 the first button text
     * @param s2 the second button text
     * @param e1 the click mouse event for the first button
     * @param e2 the click mouse event for the second button
     */
    public void loadRightBinary(Boolean white, String s1, String s2, EventHandler<MouseEvent> e1, EventHandler<MouseEvent> e2) {
        if (panel.getChildren().size() > 1) panel.getChildren().remove(panel.getChildren().size() - 1);
        VBox vBox = new VBox();
        vBox.setFillWidth(false);
        //buttDivider.setStyle("-fx-background-color: #4200f0");
        vBox.spacingProperty().bind(panel.heightProperty().multiply(0.16));
        vBox.prefWidthProperty().bind(panel.widthProperty());
        vBox.prefHeightProperty().bind(panel.heightProperty());
        vBox.setAlignment(Pos.CENTER);

        StackPane customButton1 = new StackPane();
        if (white) customButton1.setStyle("-fx-background-color: white");
        else customButton1.setStyle("-fx-background-color: limegreen");
        customButton1.prefWidthProperty().bind(vBox.widthProperty().multiply(0.8));
        customButton1.prefHeightProperty().bind(vBox.heightProperty().multiply(0.22));

        Text text1 = new Text();
        text1.setFont(new CustomFont().font(20));
        text1.setText(s1);

        customButton1.setOnMouseClicked(e1);

        StackPane customButton2 = new StackPane();
        if (white) customButton2.setStyle("-fx-background-color: white");
        else customButton2.setStyle("-fx-background-color: red");
        customButton2.prefWidthProperty().bind(vBox.widthProperty().multiply(0.8));
        customButton2.prefHeightProperty().bind(vBox.heightProperty().multiply(0.22));

        Text text2 = new Text();
        text2.setFont(new CustomFont().font(20));
        text2.setText(s2);

        customButton2.setOnMouseClicked(e2);

        customButton1.getChildren().add(text1);
        customButton2.getChildren().add(text2);
        vBox.getChildren().addAll(customButton1, customButton2);

        panel.getChildren().addAll(vBox);
        panel.setVisible(true);
    }

    /**
     * Brief asks the scene to perform a skip or do question
     * @param operationType the operation under question
     */
    public void skipOrDo(String operationType) {
        EventHandler<MouseEvent> e1 = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                panel.setVisible(false);
                runAsync(() -> delegate().onSkipOrDoAnswer(false));
            }
        };
        EventHandler<MouseEvent> e2 = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                panel.setVisible(false);
                runAsync(() -> delegate().onSkipOrDoAnswer(true));
            }
        };
        loadRightBinary(false, operationType, "skip", e1, e2);
    }

    /**
     * Brief asks the scene to perform a block type selection
     * @param blockTypes the available block types for selection
     */
    public void blockTypeSelection(List<String> blockTypes) {
        EventHandler<MouseEvent> e1 = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                panel.setVisible(false);
                runAsync(() -> delegate().onBlockSelected(0));
            }
        };
        EventHandler<MouseEvent> e2 = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                panel.setVisible(false);
                runAsync(() -> delegate().onBlockSelected(1));
            }
        };
        loadRightBinary(true, blockTypes.get(0), blockTypes.get(1).substring(0, 3), e1, e2);
    }

    /**
     * Brief asks the scene to perform an undo request
     */
    public void undoRequest() {
        loadRightHourglass();
    }

    /**
     * Brief asks the scene to inform the player upon the currently executing operation
     * @param operationType the operation type
     */
    public void executingOperation(String operationType) {
        selectionText.setText(operationType);
        selectionText.setVisible(true);
    }

    /**
     * Brief loads loss report
     */
    public void loss() {
        StackPane sp = new StackPane();
        sp.setStyle("-fx-background-color: black");
        sp.setOpacity(0.7);
        sp.maxWidthProperty().bind(frame.maxWidthProperty());
        sp.maxHeightProperty().bind(frame.maxHeightProperty());

        StackPane sp2 = new StackPane();
        sp2.maxWidthProperty().bind(frame.maxWidthProperty());
        sp2.maxHeightProperty().bind(frame.maxHeightProperty());

        Pane view = new Pane();
        //view.setStyle("-fx-background-color: orangered");
        view.maxWidthProperty().bind(sp2.maxWidthProperty().multiply(0.29));
        view.maxHeightProperty().bind(sp2.maxHeightProperty().multiply(0.4));

        StackPane activeView = new StackPane();
        //activeView.setStyle("-fx-background-color: lime");
        activeView.prefWidthProperty().bind(view.maxWidthProperty().multiply(0.95));
        activeView.prefHeightProperty().bind(view.maxHeightProperty().multiply(1));
        activeView.translateYProperty().bind(view.maxHeightProperty().multiply(0));

        VBox vBox = new VBox();
        //vBox.setStyle("-fx-background-color: purple");
        vBox.maxWidthProperty().bind(activeView.prefWidthProperty());
        vBox.maxHeightProperty().bind(activeView.prefHeightProperty());

        StackPane textPane = new StackPane();
        //textPane.setStyle("-fx-background-color: yellow");
        textPane.prefHeightProperty().bind(vBox.maxHeightProperty().multiply(0.11));

        Text playerName = new Text("Player");
        Font font = new Font(28);
        playerName.setFont(font);
        playerName.setFill(Color.WHITE);

        Pane middlePane = new Pane();
        //middlePane.setStyle("-fx-background-color: black");
        middlePane.prefHeightProperty().bind(vBox.maxHeightProperty().multiply(0.27));

        StackPane cardPane = new StackPane();
        //cardPane.setStyle("-fx-background-color: green");
        cardPane.prefHeightProperty().bind(vBox.maxHeightProperty().multiply(0.62));

        Image backgroundImage = null;
        try {
            backgroundImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("loss.png"))
                    .toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ImageView backgroundView = new ImageView();
        backgroundView.setImage(backgroundImage);
        backgroundView.setSmooth(true);
        backgroundView.setCache(true);
        backgroundView.setPreserveRatio(true);
        backgroundView.fitWidthProperty().bind(sp2.maxWidthProperty().multiply(0.7));

        Image card_backgroundImage = null;
        try {
            card_backgroundImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("1.png"))
                    .toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ImageView card_backgroundView = new ImageView();
        card_backgroundView.setImage(card_backgroundImage);
        card_backgroundView.setSmooth(true);
        card_backgroundView.setCache(true);
        card_backgroundView.setPreserveRatio(true);
        card_backgroundView.fitHeightProperty().bind(cardPane.prefHeightProperty().multiply(0.9));

        textPane.getChildren().addAll(playerName);
        cardPane.getChildren().addAll(card_backgroundView);
        vBox.getChildren().addAll(textPane,middlePane,cardPane);
        activeView.getChildren().addAll(vBox);
        view.getChildren().addAll(activeView);
        sp2.getChildren().addAll(backgroundView, view);
        frame.getChildren().addAll(sp, sp2);
    }

    /**
     * Brief loads win report
     */
    public void win() {
        StackPane sp = new StackPane();
        sp.setStyle("-fx-background-color: black");
        sp.setOpacity(0.7);
        sp.maxWidthProperty().bind(frame.maxWidthProperty());
        sp.maxHeightProperty().bind(frame.maxHeightProperty());

        StackPane sp2 = new StackPane();
        sp2.maxWidthProperty().bind(frame.maxWidthProperty());
        sp2.maxHeightProperty().bind(frame.maxHeightProperty());

        Pane view = new Pane();
        //view.setStyle("-fx-background-color: orangered");
        view.maxWidthProperty().bind(sp2.maxWidthProperty().multiply(0.29));
        view.maxHeightProperty().bind(sp2.maxHeightProperty().multiply(0.4));

        StackPane activeView = new StackPane();
        //activeView.setStyle("-fx-background-color: lime");
        activeView.prefWidthProperty().bind(view.maxWidthProperty().multiply(0.95));
        activeView.prefHeightProperty().bind(view.maxHeightProperty().multiply(1));
        activeView.translateYProperty().bind(view.maxHeightProperty().multiply(0));

        VBox vBox = new VBox();
        //vBox.setStyle("-fx-background-color: purple");
        vBox.maxWidthProperty().bind(activeView.prefWidthProperty());
        vBox.maxHeightProperty().bind(activeView.prefHeightProperty());

        StackPane textPane = new StackPane();
        //textPane.setStyle("-fx-background-color: yellow");
        textPane.prefHeightProperty().bind(vBox.maxHeightProperty().multiply(0.11));

        Text playerName = new Text("Luca");
        Font font = new Font(28);
        playerName.setFont(font);
        playerName.setFill(Color.WHITE);

        Pane middlePane = new Pane();
        //middlePane.setStyle("-fx-background-color: black");
        middlePane.prefHeightProperty().bind(vBox.maxHeightProperty().multiply(0.27));

        StackPane cardPane = new StackPane();
        //cardPane.setStyle("-fx-background-color: green");
        cardPane.prefHeightProperty().bind(vBox.maxHeightProperty().multiply(0.62));

        Image backgroundImage = null;
        try {
            backgroundImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("victory.png"))
                    .toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ImageView backgroundView = new ImageView();
        backgroundView.setImage(backgroundImage);
        backgroundView.setSmooth(true);
        backgroundView.setCache(true);
        backgroundView.setPreserveRatio(true);
        backgroundView.fitWidthProperty().bind(sp2.maxWidthProperty().multiply(0.7));

        Image card_backgroundImage = null;
        try {
            card_backgroundImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("1.png"))
                    .toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ImageView card_backgroundView = new ImageView();
        card_backgroundView.setImage(card_backgroundImage);
        card_backgroundView.setSmooth(true);
        card_backgroundView.setCache(true);
        card_backgroundView.setPreserveRatio(true);
        card_backgroundView.fitHeightProperty().bind(cardPane.prefHeightProperty().multiply(0.9));
        card_backgroundView.setEffect(new Bloom(0.9));

        DropShadow ds = new DropShadow();
        ds.setOffsetX(5);
        ds.setOffsetY(5);
        ds.setColor(Color.WHITE);

        ds.setInput(new Bloom(0.8));
        card_backgroundView.setEffect(ds);

        textPane.getChildren().addAll(playerName);
        cardPane.getChildren().addAll(card_backgroundView);
        vBox.getChildren().addAll(textPane,middlePane,cardPane);
        activeView.getChildren().addAll(vBox);
        view.getChildren().addAll(activeView);
        sp2.getChildren().addAll(backgroundView, view);
        frame.getChildren().addAll(sp, sp2);
    }
}
