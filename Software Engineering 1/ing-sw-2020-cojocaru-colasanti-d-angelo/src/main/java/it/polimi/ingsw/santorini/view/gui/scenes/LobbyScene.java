package it.polimi.ingsw.santorini.view.gui.scenes;

import it.polimi.ingsw.santorini.view.gui.scenes.delegates.LobbySceneDelegate;
import it.polimi.ingsw.santorini.view.gui.scenes.utils.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.function.Function;

/**
 * Brief Lobby Scene (where the match is being formed)
 * @see LobbySceneDelegate
 * @see GUIScene
 */
public class LobbyScene extends GUIScene<LobbySceneDelegate> {

    /**
     * Brief the default width of the scene
     */
    private final static Double defaultWidth = 1280d;

    /**
     * Brief the default height of the scene
     */
    private final static Double defaultHeight = 720d;

    /**
     * Brief the title of the scene
     */
    private final static String title = "Santorini lobby";

    /**
     * Brief the ratio of frame's sizes
     */
    private final static Double frameRatio = 1.7777777777777777777777777777778;

    /**
     * Brief the scene frame
     */
    private StackPane frame;

    /**
     * Brief the scene players box
     */
    private VBox playersBox;

    /**
     * Brief the scene control pane
     */
    private StackPane controlPane;

    /**
     * Brief the scene over layer
     */
    private StackPane overLayer;

    /**
     * Brief the scene name input text
     */
    private Text nameInputText;

    /**
     * Brief the scene name input text field
     */
    private TextField nameInput;

    /**
     * Brief decoration panes for two players and three players
     */
    private Pane decorationPanePlayers2, decorationPanePlayers3;

    /**
     * Brief selection panes for two players and three players
     */
    private StackPane playerNumberButton2, playerNumberButton3;

    /**
     * Brief the scene info pane (for waiting)
     */
    private StackPane infoPane;

    /**
     * Brief the scene info text (for waiting)
     */
    private Text infoText;

    /**
     * Brief the scene host indicator
     */
    private Pane hostIndicator;

    /**
     * Brief the scene start button
     */
    private StackPane startButton;

    /**
     * Brief boolean that tells if the name was invalid
     */
    private Boolean invalidName = false;

    /**
     * Brief the current lobby's number of players
     */
    private Integer numberOfPlayers = 0;

    /**
     * Brief tells if two players mode is the current mode
     */
    private Boolean twoPlayers = true;

    /**
     * Brief main scene constructor: it loads the scene
     */
    public LobbyScene() {
        super(defaultWidth,defaultHeight,title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void customLoadScene() {
        // black root color
        Refiner.backgroundColor(root(), "black");
        // frame and background
        frame = getNewFrame();
        ImageView background = getNewBackground();
        // playersBox, decorationBox and decorationBorder
        playersBox = getNewPlayerBox();
        Pane decorationBox = getNewDecorationBox();
        Pane decorationBorder = getNewDecorationBorder();
        // exit button
        StackPane leaveButton = getNewLeaveButton();
        // control pane
        controlPane = getNewControlPane();
        // over layer and name input
        overLayer = getNewOverLayer();
        HBox inputPanel = getNewNameInputPanel();
        nameInput = getNewNameInput(inputPanel);
        StackPane confirmButton = getNewConfirmButton(inputPanel, nameInput);
        // add
        Refiner.addTo(inputPanel, nameInput, confirmButton);
        Refiner.addTo(overLayer, inputPanel);
        Refiner.addTo(frame, background, decorationBox, decorationBorder, playersBox, controlPane, overLayer, leaveButton);
        Refiner.addTo(root(), frame);
    }

    /**
     * Brief loads the control pane
     * @param host if to show host
     */
    public void loadControlPane(Boolean host) {
        Refiner.removeFrom(controlPane);
        Refiner.addTo(controlPane, getNewInfoPane(host));
        this.startButton = getNewStartButton();
        startButton.setVisible(false);
        if (host) Refiner.addTo(controlPane, getNewPlayersNumberPanel(), startButton);
    }

    /**
     * Brief shows the start button
     */
    public void showStartButton() {
        startButton.setVisible(true);
    }

    /**
     * Brief hides the start button
     */
    public void hideStartButton() {
        if (startButton != null) startButton.setVisible(false);
    }

    /**
     * Brief loads the name input
     */
    public void loadNameInput() {
        nameInput.setText("");
        setNameInputText();
        overLayer.setVisible(true);
        if (!invalidName) invalidName = true;
    }

    public void hideOverLayer() {
        overLayer.setVisible(false);
    }

    /**
     * Brief loads the name input text
     */
    private void setNameInputText() {
        nameInputText.setFill(Paint.valueOf(invalidName ? "white" : "white"));
        nameInputText.setText(invalidName ? "Name was invalid or already taken" : "Type your name");
    }

    /**
     * Brief loads a new frame pane
     * @return the frame
     */
    private StackPane getNewFrame() {
        return new ProportionalFrame(root(), frameRatio);
    }

    /**
     * Brief loads a new background
     * @return the background
     */
    private ImageView getNewBackground() {
        return new SimpleImageView("/images/Backgrounds/lobbyBackground", frame.maxWidthProperty(), frame.maxHeightProperty());
    }

    /**
     * Brief loads a new player box
     * @return the player box
     */
    private VBox getNewPlayerBox() {
        playersBox = new VBox();
        playersBox.maxWidthProperty().bind(frame.widthProperty().multiply(0.47));
        playersBox.maxHeightProperty().bind(frame.heightProperty().multiply(0.74));
        playersBox.translateXProperty().bind(frame.widthProperty().multiply(-0.193));
        playersBox.translateYProperty().bind(frame.heightProperty().multiply(0.07));
        playersBox.setFillWidth(false);
        playersBox.setAlignment(Pos.CENTER);
        playersBox.spacingProperty().bind(playersBox.maxHeightProperty().multiply(0.1));
        return playersBox;
    }

    /**
     * Brief loads a new decoration box
     * @return the decoration box
     */
    private Pane getNewDecorationBox() {
        Pane decorationBox = new Pane();
        Refiner.newStyle(decorationBox, CSS.background("white", 7d));
        decorationBox.setOpacity(0.5);
        decorationBox.maxWidthProperty().bind(frame.widthProperty().multiply(0.47));
        decorationBox.maxHeightProperty().bind(frame.heightProperty().multiply(0.74));
        decorationBox.translateXProperty().bind(frame.widthProperty().multiply(-0.193));
        decorationBox.translateYProperty().bind(frame.heightProperty().multiply(0.07));
        return decorationBox;
    }

    /**
     * Brief loads a new decoration border
     * @return the decoration border
     */
    private Pane getNewDecorationBorder() {
        Pane decorationBorder = new Pane();
        Refiner.newStyle(decorationBorder, CSS.border("white", 5d, 4.5d));
        decorationBorder.maxWidthProperty().bind(frame.widthProperty().multiply(0.47));
        decorationBorder.maxHeightProperty().bind(frame.heightProperty().multiply(0.74));
        decorationBorder.translateXProperty().bind(frame.widthProperty().multiply(-0.193));
        decorationBorder.translateYProperty().bind(frame.heightProperty().multiply(0.07));
        return decorationBorder;
    }

    /**
     * Brief loads a new exit button
     * @return the exit (leave) button
     */
    private StackPane getNewLeaveButton() {
        StackPane leaveButton = new StackPane();
        leaveButton.maxWidthProperty().bind(frame.widthProperty().multiply(0.05));
        leaveButton.maxHeightProperty().bind(leaveButton.maxWidthProperty());

        leaveButton.translateXProperty().bind(frame.widthProperty().divide(2)
                .subtract(leaveButton.maxWidthProperty().divide(2))
                .subtract(frame.widthProperty().multiply(0.02)));

        leaveButton.translateYProperty().bind(frame.heightProperty().divide(2)
                .subtract(leaveButton.maxWidthProperty().divide(2))
                .subtract(frame.widthProperty().multiply(0.02)).multiply(-1));

        Refiner.newStyle(leaveButton, CSS.background("red", 200d));

        Pane leaveHover = new Pane();
        Refiner.newStyle(leaveHover, CSS.background("white", 200d));
        leaveHover.setOpacity(0.4);
        leaveHover.setVisible(false);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.WHITE);
        leaveButton.setEffect(dropShadow);

        Text leaveText = new Text("X");
        leaveText.setFill(Paint.valueOf("white"));

        frame.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Function<Double, Integer> newFontSize = (ratio) -> Double.valueOf(t1.doubleValue() * ratio).intValue();
                Function<Double, Font> newFont = (ratio) -> new Font(newFontSize.apply(ratio));
                leaveText.setFont(newFont.apply(0.025d));
            }
        });

        leaveButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                leaveHover.setVisible(true);
            }
        });

        leaveButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                leaveHover.setVisible(false);
            }
        });

        leaveHover.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                delegate().onExit();
            }
        });

        leaveButton.getChildren().addAll(leaveText, leaveHover);
        return leaveButton;
    }

    /**
     * Brief loads a new control pane
     * @return the control pane
     */
    public StackPane getNewControlPane() {
        StackPane controlPane = new StackPane();
        controlPane.maxWidthProperty().bind(frame.widthProperty().multiply(0.43));
        controlPane.maxHeightProperty().bind(playersBox.heightProperty());
        controlPane.translateXProperty().bind(frame.widthProperty().multiply(0.279));
        controlPane.translateYProperty().bind(playersBox.translateYProperty());
        return controlPane;
    }

    /**
     * Brief loads a new info pane
     * @param host true if host indicator must be shown
     * @return the info pane
     */
    public StackPane getNewInfoPane(Boolean host) {
        StackPane infoPane = new StackPane();
        infoPane.maxWidthProperty().bind(controlPane.widthProperty().multiply(host ? 0.9 : 1));
        //infoPane.maxWidthProperty().bind(controlPane.widthProperty().multiply(0.9));
        infoPane.maxHeightProperty().bind(playersBox.heightProperty().multiply(host ? 0.2 : 1));
        //infoPane.maxHeightProperty().bind(controlPane.heightProperty().multiply(0.2));
        infoPane.translateYProperty().bind(host ? controlPane.widthProperty().multiply(-0.38) :
                playersBox.translateYProperty().subtract(controlPane.translateYProperty()));
        //infoPane.translateYProperty().bind(controlPane.widthProperty().multiply(-0.38));

        StackPane decorationInfoPane = new StackPane();
        decorationInfoPane.setOpacity(host ? 1 : 0.5);
        //decorationInfoPane.setOpacity(1);
        Refiner.backgroundColor(decorationInfoPane, host ? "white" : "black");
        //Refiner.backgroundColor(decorationInfoPane, "white");
        Refiner.radiusBorder(decorationInfoPane, host ? 2d : 30d, null, null);
        //Refiner.radiusBorder(decorationInfoPane, 2d, null, null);

        if (host) {
            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(5.0);
            dropShadow.setOffsetX(3.0);
            dropShadow.setOffsetY(3.0);
            dropShadow.setColor(Color.LIMEGREEN);
            decorationInfoPane.setEffect(dropShadow);
        }

        //infoText = new Text("You are the host");
        infoText = new Text(host ? "You are the host" :
                "Wait for the host to\n start the game...");
        infoText.setTextAlignment(TextAlignment.CENTER);
        infoText.setFont(new CustomFont().font(0));
        infoText.setFill(Paint.valueOf(host ? "black" : "white"));
        //infoText.setFill(Paint.valueOf("black"));

        ChangeListener<Number> listener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number nNew) {
                Function<Double, Integer> newFontSize = (ratio) -> Double.valueOf(nNew.doubleValue() * ratio).intValue();
                Function<Double, Font> newFont = (ratio) -> new Font(newFontSize.apply(ratio));
                infoText.setFont(new CustomFont().font(newFontSize.apply(0.025d)));

                double bgRadius = nNew.doubleValue() * (host ? 0.007d : 0.2d);
                Refiner.radiusBorder(bgRadius, null, null, decorationInfoPane);
            }
        };
        frame.widthProperty().addListener(listener);
        listener.changed(null, null, frame.getWidth());

        Refiner.addTo(infoPane, decorationInfoPane, infoText);
        return infoPane;
    }

    /**
     * Brief loads a new start button
     * @return the start button
     */
    public StackPane getNewStartButton() {
        StackPane startButton = new StackPane();
        startButton.maxWidthProperty().bind(controlPane.widthProperty().multiply(0.5));
        startButton.maxHeightProperty().bind(controlPane.heightProperty().multiply(0.17));
        startButton.translateYProperty().bind(controlPane.heightProperty().multiply(0.35));

        Pane startDecorationPane = new Pane();
        Refiner.backgroundColor(startDecorationPane, "white");
        Refiner.radiusBorder(startDecorationPane, 10d, null, null);

        Pane startBorderPane = new Pane();
        Refiner.borderColor(startBorderPane, "white");
        Refiner.radiusBorder(startBorderPane, 10d, 8d, 2d);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.WHITE);
        startButton.setEffect(dropShadow);

        Text startText = new Text("Start");
        startText.setFont(new CustomFont().font(0));

        frame.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Function<Double, Integer> newFontSize = (ratio) -> Double.valueOf(t1.doubleValue() * ratio).intValue();
                Function<Double, Font> newFont = (ratio) -> new Font(newFontSize.apply(ratio));
                startText.setFont(new CustomFont().font(newFontSize.apply(0.025d)));
            }
        });

        startButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                startDecorationPane.setOpacity(0.3);
            }
        });

        startButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                startDecorationPane.setOpacity(1);
            }
        });

        startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                delegate().onStart();
            }
        });

        startButton.getChildren().addAll(startDecorationPane, startBorderPane, startText);
        return startButton;
    }

    /**
     * Brief loads a new players number panel
     * @return the players number panel
     */
    private HBox getNewPlayersNumberPanel() {
        HBox playersNumberPanel = new HBox();
        playersNumberPanel.setAlignment(Pos.CENTER);
        playersNumberPanel.setFillHeight(false);
        playersNumberPanel.maxHeightProperty().bind(controlPane.heightProperty().multiply(0.3));
        playersNumberPanel.translateYProperty().bind(controlPane.heightProperty().multiply(0));
        //Refiner.newStyle(playersNumberPanel, CSS.background("purple", null));
        playersNumberPanel.spacingProperty()
                .bind(playersNumberPanel.widthProperty().multiply(1 - (0.3 * 2)).divide(2 + 1));

        playersNumberPanel.getChildren().addAll(getNewPlayerNumberButton(
                playersNumberPanel, 2),
                getNewPlayerNumberButton(playersNumberPanel, 3));

        return playersNumberPanel;
    }

    /**
     * Brief Updates the number of players (visually and logically)
     */
    private void updatedNumberOfPlayers() {
        twoPlayers = !twoPlayers;
        decorationPanePlayers2.maxWidthProperty().bind(playerNumberButton2.widthProperty()
                .multiply(!twoPlayers ? 0.75 : 1));
        decorationPanePlayers3.maxWidthProperty().bind(playerNumberButton3.widthProperty()
                .multiply(twoPlayers ? 0.75 : 1));
        delegate().onNumberOfPlayerChanged(twoPlayers ? 2 : 3);
    }

    /**
     * Brief loads a new player number button
     * @param playersNumberPanel the container panel
     * @param playerNumber the number of players represented
     * @return the new player button
     */
    private StackPane getNewPlayerNumberButton(HBox playersNumberPanel, Integer playerNumber) {
        StackPane playerNumberButton = new StackPane();
        playerNumberButton.prefWidthProperty().bind(playersNumberPanel.widthProperty().multiply(0.27));
        playerNumberButton.prefHeightProperty().bind(playerNumberButton.prefWidthProperty());

        StackPane decoration = new StackPane();
        decoration.maxWidthProperty().bind(playerNumberButton.widthProperty()
                .multiply((playerNumber == 2) ? ((!twoPlayers) ? 0.75 : 1) : ((twoPlayers) ? 0.75 : 1)));
        decoration.maxHeightProperty().bind(decoration.maxWidthProperty());
        Refiner.newStyle(decoration, CSS.background("orange", 10d));
        decoration.setOpacity(0.95);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.WHITE);
        decoration.setEffect(dropShadow);

        Pane playerNumberButtonHover = new Pane();
        playerNumberButtonHover.maxWidthProperty().bind(decoration.maxWidthProperty());
        playerNumberButtonHover.maxHeightProperty().bind(playerNumberButtonHover.maxWidthProperty());
        Refiner.newStyle(playerNumberButtonHover, CSS.background(null, 10d));
        playerNumberButtonHover.setOpacity(0.5);

        if (playerNumber == 2) {
            playerNumberButton2 = playerNumberButton;
            decorationPanePlayers2 = decoration;
        }
        else {
            playerNumberButton3 = playerNumberButton;
            decorationPanePlayers3 = decoration;
        }

        ImageView playersNumberImage = new SimpleImageView("/images/Other/" + playerNumber + "pl",
                playerNumberButton.widthProperty().multiply(0.2),
                playerNumberButton.heightProperty().multiply(0.2));

        playerNumberButtonHover.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (numberOfPlayers.equals(3) && !twoPlayers) return;
                if (playerNumber == 2 && !twoPlayers) {
                    //decoration.setOpacity(0.32);
                    Refiner.backgroundColor(playerNumberButtonHover, "white");
                } else if (playerNumber == 3 && twoPlayers) {
                    //decoration.setOpacity(0.32);
                    Refiner.backgroundColor(playerNumberButtonHover, "white");
                }
            }
        });

        playerNumberButtonHover.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (numberOfPlayers.equals(3) && !twoPlayers) return;
                if (playerNumber == 2 && !twoPlayers) {
                    //decoration.setOpacity(0.95);
                    Refiner.backgroundColor(playerNumberButtonHover, "transparent");
                } else if (playerNumber == 3 && twoPlayers) {
                    //decoration.setOpacity(0.95);
                    Refiner.backgroundColor(playerNumberButtonHover, "transparent");
                }
            }
        });

        playerNumberButtonHover.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (numberOfPlayers.equals(3) && !twoPlayers) return;
                if (playerNumber == 2 && !twoPlayers) {
                    updatedNumberOfPlayers();
                    //decoration.setOpacity(0.95);
                    Refiner.backgroundColor(playerNumberButtonHover, "transparent");
                } else if (playerNumber == 3 && twoPlayers) {
                    updatedNumberOfPlayers();
                    //decoration.setOpacity(0.95);
                    Refiner.backgroundColor(playerNumberButtonHover, "transparent");
                }
            }
        });

        playerNumberButton.getChildren().addAll(decoration, playersNumberImage, playerNumberButtonHover);
        return playerNumberButton;
    }

    /**
     * Brief loads a new over layer
     * @return the over layer pane
     */
    private StackPane getNewOverLayer() {
        StackPane overLayer = new StackPane();
        overLayer.setVisible(false);

        StackPane decorationOverLayer = new StackPane();
        Refiner.backgroundColor(decorationOverLayer, "black");
        decorationOverLayer.setOpacity(0.85);

        nameInputText = new Text("Type your name");
        nameInputText.setFill(Paint.valueOf("white"));
        nameInputText.translateYProperty().bind(frame.heightProperty().multiply(-0.15));
        nameInputText.setFont(new CustomFont().font(0));

        frame.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Function<Double, Integer> newFontSize = (ratio) -> Double.valueOf(t1.doubleValue() * ratio).intValue();
                Function<Double, Font> newFont = (ratio) -> new Font(newFontSize.apply(ratio));
                nameInputText.setFont(new CustomFont().font(newFontSize.apply(0.04d)));
            }
        });

        Refiner.addTo(overLayer, decorationOverLayer, nameInputText);
        return overLayer;
    }

    /**
     * Brief loads a new name input panel
     * @return the name input panel
     */
    private HBox getNewNameInputPanel() {
        HBox nameInputPanel = new HBox();
        nameInputPanel.setAlignment(Pos.CENTER);
        //Refiner.backgroundColor(nameInputPanel, "purple");
        nameInputPanel.setFillHeight(false);
        nameInputPanel.spacingProperty().bind(nameInputPanel.widthProperty().multiply(0.07));
        nameInputPanel.maxWidthProperty().bind(overLayer.widthProperty().multiply(0.5));
        nameInputPanel.maxHeightProperty().bind(overLayer.widthProperty().multiply(0.07));
        nameInputPanel.translateYProperty().bind(overLayer.heightProperty().multiply(0.05));
        return nameInputPanel;
    }

    /**
     * Brief loads a new name input
     * @param inputPanel the container panel
     * @return the name input
     */
    private TextField getNewNameInput(HBox inputPanel) {
        TextField nameInput = new TextField();
        nameInput.setAlignment(Pos.CENTER);
        nameInput.prefWidthProperty().bind(inputPanel.widthProperty().multiply(0.6));

        Refiner.styleUpdate(nameInput, "-fx-background-color: #ffffff;");

        nameInput.setFont(new CustomFont().font(0));

        frame.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Function<Double, Integer> newFontSize = (ratio) -> Double.valueOf(t1.doubleValue() * ratio).intValue();
                Function<Double, Font> newFont = (ratio) -> new Font(newFontSize.apply(ratio));
                nameInput.setFont(new CustomFont().font(newFontSize.apply(0.025d)));
            }
        });

        return nameInput;
    }

    /**
     * Brief loads a new name confirmation button
     * @param inputPanel the container panel
     * @param nameInput the name input
     * @return the confirm button
     */
    private StackPane getNewConfirmButton(HBox inputPanel, TextField nameInput) {
        StackPane confirmButton = new StackPane();
        Refiner.backgroundColor(confirmButton, "white");
        Refiner.styleUpdate(confirmButton, CSS.background(null, 10d));
        confirmButton.prefWidthProperty().bind(inputPanel.widthProperty().multiply(0.3));
        confirmButton.prefHeightProperty().bind(nameInput.heightProperty());

        Text confirmText = new Text("OK");
        //confirmText.setFill(Paint.valueOf("black"));
        confirmText.setFont(new CustomFont().font(0));

        frame.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Function<Double, Integer> newFontSize = (ratio) -> Double.valueOf(t1.doubleValue() * ratio).intValue();
                Function<Double, Font> newFont = (ratio) -> new Font(newFontSize.apply(ratio));
                confirmText.setFont(new CustomFont().font(newFontSize.apply(0.025d)));
            }
        });

        confirmButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Refiner.backgroundColor(confirmButton, "limegreen");
            }
        });

        confirmButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Refiner.backgroundColor(confirmButton, "white");
            }
        });

        confirmButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                delegate().onNameConfirmed(nameInput.getText());
            }
        });

        Refiner.addTo(confirmButton, confirmText);
        return confirmButton;
    }

    /**
     * Brief updates the player box
     * @param players the list of current players
     * @param hostIndicator true if must show the host infdicator
     */
    public void updatePlayersBox(List<String> players, Boolean hostIndicator) {
        numberOfPlayers = players.size();
        playersBox.spacingProperty().bind(playersBox.heightProperty().multiply(1 - (0.2 * players.size())).divide(players.size() + 1));
        playersBox.getChildren().removeIf((node -> true));
        for (int i = 0; i < players.size(); i++) addPlayerCell(players.get(i), hostIndicator && i == 0);
    }

    /**
     * Brief adds a new player cell to the players box
     * @param player the player being added
     * @param host host indicator visibility
     */
    private void addPlayerCell(String player, Boolean host) {
        StackPane playerCell = new StackPane();
        playerCell.prefWidthProperty().bind(playersBox.widthProperty().multiply(0.8));
        playerCell.prefHeightProperty().bind(playersBox.heightProperty().multiply(0.2));
        Refiner.newStyle(playerCell, CSS.background("white", 30d));

        Pane hostIndicator = new Pane();
        hostIndicator.maxWidthProperty().bind(playerCell.widthProperty().multiply(0.05));
        hostIndicator.maxHeightProperty().bind(hostIndicator.maxWidthProperty());
        hostIndicator.translateXProperty().bind(playerCell.widthProperty().multiply(-0.4));
        Refiner.backgroundColor(hostIndicator, "limegreen");
        Refiner.radiusBorder(hostIndicator, 50d, null, null);
        hostIndicator.setVisible(host);

        Text playerName = new Text(player);
        playerName.setFont(new CustomFont().font(0));

        ChangeListener<Number> listener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number nNew) {
                Function<Double, Integer> newFontSize = (ratio) -> Double.valueOf(nNew.doubleValue() * ratio).intValue();
                Function<Double, Font> newFont = (ratio) -> new Font(newFontSize.apply(ratio));
                playerName.setFont(new CustomFont().font(newFontSize.apply(0.025d)));

                double bgRadius = nNew.doubleValue() * 0.02;
                Refiner.radiusBorder(bgRadius, null, null, playerCell);

                double bgRadius2 = nNew.doubleValue() * 0.04;
                Refiner.radiusBorder(bgRadius2, null, null, hostIndicator);
            }
        };
        frame.widthProperty().addListener(listener);
        listener.changed(null, null, frame.getWidth());

        Refiner.addTo(playerCell, playerName, hostIndicator);
        playersBox.getChildren().addAll(playerCell);
    }
}
