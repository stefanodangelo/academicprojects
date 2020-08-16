package it.polimi.ingsw.santorini.view.gui.scenes;

import it.polimi.ingsw.santorini.communication.ImmutableCard;
import it.polimi.ingsw.santorini.view.gui.scenes.delegates.SetupSceneDelegate;
import it.polimi.ingsw.santorini.view.gui.scenes.utils.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Brief the setup scene
 * @see it.polimi.ingsw.santorini.view.gui.controllers.SetupSceneController
 *  * @see GUIScene
 * @see it.polimi.ingsw.santorini.view.gui.scenes.delegates.GUISceneDelegate
 */
public class SetupScene extends GUIScene<SetupSceneDelegate> {

    /**
     * Brief the default scene width
     */
    private final static Double defaultWidth = 1422.2222222222222222222222222222d;

    /**
     * Brief the default scene height
     */
    private final static Double defaultHeight = 800d;

    /**
     * Brief the default scene title
     */
    private final static String title = "Santorini setup";

    /**
     * Brief the default scene frame ratio
     */
    private final static Double frameRatio = 1.7777777777777777777777777777778;

    /**
     * Brief the default scene console frame ratio
     */
    private final static Double consoleFrameHRatio = 0.9;

    /**
     * Brief the default scene frame opacity
     */
    private final static Double consoleFrameOpacity = 0.5;

    /**
     * Brief the default scene upper pane ratio
     */
    private final static Double upRatio = 0.2;

    /**
     * Brief the default scene fake upper pane height ratio
     */
    private final static Double fakeUpHRatio = 0.19;

    /**
     * Brief the default scene fake bottom pane height ratio
     */
    private final static Double bottomHRatio = 0.81;


    /**
     * Brief the default scene background radius ratio
     */
    private final static Double consoleFrameBGRadiusRatio = 0.009d;

    /**
     * Brief the default scene border radius ratio
     */
    private final static Double consoleFrameBRadiusRatio = 0.007d;

    /**
     * Brief the default scene console frame border ratio
     */
    private final static Double consoleFrameBorderRatio = 0.003d;

    /**
     * Brief the scene frame
     */
    private StackPane frame;

    /**
     * the scene background
     */
    private ImageView background;

    /**
     * Brief the scene upper and bottom panes
     */
    private StackPane up, bottom;

    /**
     * Brief the scene main and waiting texts
     */
    private Text mainText, waitingText;

    /**
     * Brief the confirmation layer
     */
    private StackPane confirmLayer;

    /**
     * Brief the selection buffer for cards
     */
    private final Map<Integer, Boolean> selectedMap = new HashMap<>();

    /**
     * Brief the current number of selected cards
     */
    private Integer numberOfSelections;

    /**
     * Brief the waiting (for other players'choices) timer
     */
    private Timer waitingTimer;

    /**
     * Brief the waiting dots text counter
     */
    private Integer waitingDots = 0;

    /**
     * Brief force stops the timer
     */
    private Boolean stopTimer = false;

    /**
     * Brief main scene constructor: it loads the scene
     */
    public SetupScene() {
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
        background = getNewBackground();
        // console
        Pane consoleFrame = getNewConsoleFrame();
        Pane consoleBorder = getNewConsoleBorder(consoleFrame);
        VBox consoleDivider = getNewConsoleDivider();
        VBox decorationConsoleDivider = getNewDecorationConsoleDivider();
        // up and bottom
        up = getNewUp(consoleDivider);
        bottom = getNewBottom(decorationConsoleDivider);
        StackPane fakeUp = getNewFakeUp(decorationConsoleDivider);
        StackPane fakeBottom = getNewFakeBottom(consoleDivider);
        // load up and leave bottom free
        loadUp();
        // add
        Refiner.addTo(consoleDivider, up, fakeBottom);
        Refiner.addTo(decorationConsoleDivider, fakeUp, bottom);
        Refiner.addTo(frame, background, consoleFrame, consoleBorder, consoleDivider, decorationConsoleDivider);
        Refiner.addTo(root(), frame);
        // radius and border resize
        Refiner.addWidthListener(bottom, (ObservableValue<? extends Number> observableValue, Number nOld, Number nNew) -> {
            double bgRadius = nNew.doubleValue() * consoleFrameBGRadiusRatio;
            double bRadius = nNew.doubleValue() * consoleFrameBRadiusRatio;
            double border = nNew.doubleValue() * consoleFrameBorderRatio;
            Refiner.radiusBorder(bgRadius, bRadius, border, consoleFrame, consoleBorder);
        });
    }

    /**
     * Brief the selection variants enum
     */
    public enum Selection {
        MODE("Select the mode you want to play"),
        COLOR("Select your color"),
        FIRST("Choose who will be the first player"),
        CARDS("");

        /**
         * Brief the title of the selection
         */
        private String title;

        /**
         * Brief getter for the title
         * @return the title
         */
        public String getTitle() {
            return title;
        }

        /**
         * Brief the main constructor: sets the title
         * @param title
         */
        Selection(String title) {
            this.title = title;
        }
    }

    /**
     * Brief loads the scene choice selection
     * @param selection the required selection
     * @param options the list of selection options
     */
    public void loadChoiceSelection(Selection selection, List<String> options) {
        setMainText(selection.title);
        Refiner.removeFrom(bottom);
        loadPollSelection(options);
    }

    /**
     * Brief stops the text timer
     */
    public void stopTextTimer() {
        stopTimer = true;
    }

    /**
     * Brief loads the cards selection
     * @param cards the available cards
     * @param numberOfSelections the number of allowed selections
     */
    public void loadCardsSelection(List<ImmutableCard> cards, Integer numberOfSelections) {
        setMainText(Selection.CARDS.getTitle());
        this.numberOfSelections = numberOfSelections;
        Refiner.removeFrom(bottom);
        loadCardSelection(cards);
        loadConfirmButton();
    }

    /**
     * Brief loads the choice selection
     * @param title the title of the selection
     * @param options the available options for the selection
     */
    public void loadChoiceSelection(String title, List<String> options) {
        Refiner.removeFrom(bottom);
        setMainText(title);
        loadPollSelection(options);
    }

    /**
     * Brief loads the color selection
     * @param workersColors the available colors for selection
     */
    public void loadColorSelection(List<it.polimi.ingsw.santorini.view.Color> workersColors) {
        setMainText(Selection.COLOR.getTitle());
        Refiner.removeFrom(bottom);
        getLoadColorSelection(workersColors);
    }

    /**
     * Brief getter for the scene's frame pane
     * @return the frame pane
     */
    public StackPane getFrame() {
        return frame;
    }

    /**
     * Brief getter for the scene's up pane
     * @return the up pane
     */
    public StackPane getUp() {
        return up;
    }

    /**
     * Brief getter for the scene's bottom pane
     * @return the bottom pane
     */
    public StackPane getBottom() {
        return bottom;
    }

    /**
     * Brief produces a new frame pane
     * @return the frame pane
     */
    public StackPane getNewFrame() {
        return new ProportionalFrame(root(), frameRatio);
    }

    /**
     * Brief produces a new background image view
     * @return the background
     */
    public ImageView getNewBackground() {
        return new SimpleImageView("/images/Game/board", frame.maxWidthProperty(), frame.maxHeightProperty(), Effects.blur());
    }

    /**
     * Brief produces a new console frame pane
     * @return the console frame pane
     */
    public Pane getNewConsoleFrame() {
        Pane consoleFrame = new Pane();
        Refiner.color(consoleFrame, "black", "white");
        Refiner.sizeOpacity(consoleFrame, consoleFrameOpacity, SizeType.MAX,
                consoleFrame.maxHeightProperty().multiply(frameRatio),
                background.fitHeightProperty().multiply(consoleFrameHRatio));
        return consoleFrame;
    }

    /**
     * Brief produces a new console border pane
     * @param consoleFrame the container pane
     * @return the console border
     */
    public Pane getNewConsoleBorder(Pane consoleFrame) {
        Pane consoleBorder = new Pane();
        Refiner.borderColor(consoleBorder, "white");
        Refiner.size(consoleBorder, SizeType.MAX,
                consoleFrame.maxHeightProperty().multiply(frameRatio),
                background.fitHeightProperty().multiply(consoleFrameHRatio));
        return consoleBorder;
    }

    /**
     * Brief produces a new console divider pane
     * @return the divider pane
     */
    public VBox getNewConsoleDivider() {
        VBox consoleDivider = new VBox();
        Refiner.size(consoleDivider, SizeType.MAX,
                consoleDivider.maxHeightProperty().multiply(frameRatio),
                background.fitHeightProperty().multiply(consoleFrameHRatio));
        return consoleDivider;
    }

    /**
     * Brief produces a new upper pane
     * @param consoleDivider the container console divider
     * @return the upper pane
     */
    public StackPane getNewUp(VBox consoleDivider) {
        StackPane up = new StackPane();
        Refiner.size(up, SizeType.PREF, null, consoleDivider.heightProperty().multiply(upRatio));
        return up;
    }

    /**
     * Brief produces a new fake bottom pane
     * @param consoleDivider the container pane
     * @return the bottom fake
     */
    public StackPane getNewFakeBottom(VBox consoleDivider) {
        StackPane fakeBottom = new StackPane();
        Refiner.size(fakeBottom, SizeType.PREF, null, consoleDivider.heightProperty().multiply(1 - upRatio));
        return fakeBottom;
    }

    /**
     * Brief produces a new decoration console divider
     * @return the console divider
     */
    public VBox getNewDecorationConsoleDivider() {
        VBox decorationConsoleDivider = new VBox();
        Refiner.size(decorationConsoleDivider, SizeType.MAX,
                decorationConsoleDivider.maxHeightProperty().multiply(frameRatio),
                background.fitHeightProperty().multiply(consoleFrameHRatio));
        return decorationConsoleDivider;
    }

    /**
     * Brief produces a new fake up pane
     * @param decorationConsoleDivider the container pane
     * @return the fake up pane
     */
    public StackPane getNewFakeUp(VBox decorationConsoleDivider) {
        StackPane fakeUp = new StackPane();
        Refiner.size(fakeUp, SizeType.PREF, null, decorationConsoleDivider.heightProperty().multiply(fakeUpHRatio));
        return fakeUp;
    }

    /**
     * Brief prodices a new bottom pane
     * @param decorationConsoleDivider the container pane
     * @return the bottom pane
     */
    public StackPane getNewBottom(VBox decorationConsoleDivider) {
        bottom = new StackPane();
        Refiner.size(bottom, SizeType.PREF, null, decorationConsoleDivider.heightProperty().multiply(bottomHRatio));
        return bottom;
    }

    /**
     * Brief loads the upper frame
     */
    private void loadUp() {
        Pane decorativeUp = new StackPane();
        decorativeUp.setStyle("-fx-background-color: white;");
        decorativeUp.maxWidthProperty().bind(up.widthProperty().multiply(0.9957));
        decorativeUp.maxHeightProperty().bind(up.heightProperty().multiply(0.955));

        Pane decorativeRectUp = new Pane();
        decorativeRectUp.setStyle("-fx-background-color: white");
        decorativeRectUp.maxHeightProperty().bind(up.heightProperty().multiply(0.5));
        decorativeRectUp.translateYProperty().bind(up.heightProperty().multiply(0.25));

        Pane decorativeRectUp2 = new Pane();
        decorativeRectUp2.setStyle("-fx-background-color: white");
        decorativeRectUp2.maxHeightProperty().bind(up.heightProperty().multiply(0.8));

        Pane decorativeRectUp3 = new Pane();
        decorativeRectUp3.setStyle("-fx-background-color: white");
        decorativeRectUp3.maxWidthProperty().bind(up.widthProperty().multiply(0.98));

        mainText = new Text();
        setMainText("Ciao");

        bottom.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Function<Double, Integer> newFontSize = (ratio) -> Double.valueOf(t1.doubleValue() * ratio).intValue();
                Function<Double, Font> newFont = (ratio) -> new Font(newFontSize.apply(ratio));
                if (mainText != null) mainText.setFont(newFont.apply(0.025d));
            }
        });

        up.getChildren().addAll(decorativeUp, decorativeRectUp, decorativeRectUp2, decorativeRectUp3, mainText);
    }

    /**
     * Brief loads the waiting mode
     */
    public void loadWaiting() {
        Refiner.removeFrom(bottom);
        setMainText("Waiting");
        waitingText = new Text("Waiting for the other players");
        waitingText.setFont(new CustomFont().font(0));
        waitingText.setFill(Color.WHITE);

        bottom.getChildren().addAll(waitingText);

        ChangeListener<Number> listener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Function<Double, Integer> newFontSize = (ratio) -> Double.valueOf(t1.doubleValue() * ratio).intValue();
                Function<Double, Font> newFont = (ratio) -> new Font(newFontSize.apply(ratio));
                if (waitingText != null)
                    waitingText.setFont(new CustomFont().font(newFontSize.apply(0.03d)));
            }
        };
        bottom.widthProperty().addListener(listener);
        listener.changed(null, null, bottom.getWidth());

        waitingDots = 0;
        if (waitingTimer != null) waitingTimer.cancel();
        waitingTimer = new Timer();
        waitingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (stopTimer) cancel();
                waitingDots = (waitingDots != 3) ? waitingDots + 1 : 0;
                String current = waitingText.getText();
                if (waitingDots == 0) waitingText.setText(current.substring(0, current.indexOf('.')));
                else waitingText.setText(current + '.');
            }
        }, 0, 850);
    }

    /**
     * brief loads the confirm button
     */
    private void loadConfirmButton() {
        confirmLayer = new StackPane();
        confirmLayer.maxWidthProperty().bind(bottom.widthProperty().multiply(0.4));
        confirmLayer.maxHeightProperty().bind(bottom.heightProperty().multiply(0.2));
        confirmLayer.translateYProperty().bind(bottom.heightProperty().multiply(0.2));
        confirmLayer.setVisible(false);

        StackPane layerDecoration = new StackPane();
        layerDecoration.maxWidthProperty().bind(bottom.widthProperty().multiply(0.4));
        layerDecoration.maxHeightProperty().bind(bottom.heightProperty().multiply(0.2));
        layerDecoration.setOpacity(0.7);

        StackPane layer = new StackPane();
        layer.maxWidthProperty().bind(bottom.widthProperty().multiply(0.12));
        layer.maxHeightProperty().bind(bottom.heightProperty().multiply(0.14));

        StackPane confirm = new StackPane();
        confirm.maxWidthProperty().bind(bottom.widthProperty().multiply(0.12));
        confirm.maxHeightProperty().bind(bottom.heightProperty().multiply(0.12));
        confirm.setStyle("-fx-background-color: springgreen; \n");

        confirm.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String confirmStyle = confirm.getStyle();
                confirm.setStyle("-fx-background-color: white;" + confirmStyle.substring(confirmStyle.indexOf('\n')));
            }
        });

        confirm.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String confirmStyle = confirm.getStyle();
                confirm.setStyle("-fx-background-color: springgreen;" + confirmStyle.substring(confirmStyle.indexOf('\n')));
            }
        });

        confirm.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                runAsync(() -> delegate().onCardsConfirmed());
            }
        });

        Text ok = new Text("OK");

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.WHITE);
        confirm.setEffect(dropShadow);

        ChangeListener<Number> listener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Function<Double, Integer> newFontSize = (ratio) -> Double.valueOf(t1.doubleValue() * ratio).intValue();
                ok.setFont(new CustomFont().font(newFontSize.apply(0.02d)));

                int newRadius = Double.valueOf(t1.doubleValue() * 0.009d).intValue();
                int newRadius2 = Double.valueOf(t1.doubleValue() * 0.007d).intValue();
                int newBorder = Double.valueOf(t1.doubleValue() * 0.003d).intValue();
                layerDecoration.setStyle(
                        "-fx-background-color: black;" +
                                "-fx-background-radius: " + newRadius + ";"
                );
                String confirmStyle = confirm.getStyle();
                confirm.setStyle(
                        confirmStyle.substring(0, confirmStyle.indexOf('\n') + 1) +
                                "-fx-background-radius: " + newRadius + ";" +
                                "-fx-border-radius: " + newRadius2 + ";" +
                                "-fx-border-color: white;" +
                                "-fx-border-width: " + newBorder + ";"
                );
            }
        };
        bottom.widthProperty().addListener(listener);
        listener.changed(null, null, bottom.getWidth());

        confirm.getChildren().addAll(ok);
        layer.getChildren().addAll(confirm);
        confirmLayer.getChildren().addAll(layerDecoration, layer);
        bottom.getChildren().addAll(confirmLayer);
    }

    /**
     * Brief the player colors enum
     */
    public enum PlayerColor {
        YELLOW("yellow"),
        RED("red"),
        GREEN("green");

        /**
         * Brief the name of the color
         */
        private final String name;

        /**
         * Brief getter for the color name
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Constructor: sets the color
         * @param color the color name
         */
        PlayerColor(String color) {
            name = color;
        }

        /**
         * Brief static converter between CLI colors and GUI colors
         * @param cliColor the CLI colors
         * @return the GUI color
         */
        public static PlayerColor getGUIColor(it.polimi.ingsw.santorini.view.Color cliColor) {
            String colorName = cliColor.getName().toUpperCase();
            return PlayerColor.valueOf(colorName.equals("CYAN") ? "GREEN" : colorName);
        }
    }

    /**
     * Brief loads the color selection
     * @param workersColors the available colors to choose from
     */
    public void getLoadColorSelection(List<it.polimi.ingsw.santorini.view.Color> workersColors) {
        mainText.setText("Pick your color");
        HBox hBox = new HBox();
        //hBox.setStyle("-fx-background-color: purple");
        hBox.maxWidthProperty().bind(bottom.widthProperty().multiply(0.9));
        hBox.maxHeightProperty().bind(bottom.heightProperty().multiply(0.3));
        hBox.spacingProperty().bind(hBox.maxWidthProperty().multiply(0.1));
        hBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < workersColors.size(); i++) hBox.getChildren().addAll(
                colorSelector(hBox, PlayerColor.getGUIColor(workersColors.get(i)).getName(), i, workersColors));
        bottom.getChildren().addAll(hBox);
    }

    /**
     * Brief produces a color selector cell
     * @param hBox the container pane
     * @param color the color
     * @param id the id of the pane
     * @param workersColors the available colors to choose from
     * @return the color selector pane
     */
    private StackPane colorSelector(HBox hBox, String color, Integer id, List<it.polimi.ingsw.santorini.view.Color> workersColors) {
        StackPane colorSelector = new StackPane();
        colorSelector.prefWidthProperty().bind(hBox.heightProperty());

        StackPane selector = new StackPane();
        selector.setStyle("-fx-background-color: white;");
        selector.setOpacity(0.64);
        selector.setVisible(false);
        colorSelector.getChildren().addAll(selector);

        colorSelector.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selector.setVisible(true);
            }
        });

        colorSelector.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selector.setVisible(false);
            }
        });

        colorSelector.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                runAsync(() -> delegate().onColorSelected(id, workersColors));
            }
        });

        ChangeListener<Number> listener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                int newRadius = Double.valueOf(t1.doubleValue() * 0.0145d).intValue();
                int newRadius2 = Double.valueOf(t1.doubleValue() * 0.011d).intValue();
                int newBorder = Double.valueOf(t1.doubleValue() * 0.005d).intValue();
                colorSelector.setStyle(
                        "-fx-background-color: " + color + ";" +
                                "-fx-background-radius: " + newRadius + ";" +
                                "-fx-border-radius: " + newRadius2 + ";" +
                                "-fx-border-color: white;" +
                                "-fx-border-width: " + newBorder + ";"
                );
            }
        };
        bottom.widthProperty().addListener(listener);
        listener.changed(null, null, bottom.getWidth());

        return colorSelector;
    }

    /**
     * Brief loads the poll selection
     * @param incomingOptions the available incoming options to choose from
     */
    public void loadPollSelection(List<String> incomingOptions) {
        List<String> options = incomingOptions.stream().map(String::toUpperCase).collect(Collectors.toList());
        //mainText.setText("Who is the most god-like player?");
        HBox hBox = new HBox();
        //hBox.setStyle("-fx-background-color: purple");
        hBox.maxWidthProperty().bind(bottom.widthProperty().multiply(0.9));
        hBox.maxHeightProperty().bind(bottom.heightProperty().multiply(0.2));
        hBox.spacingProperty().bind(hBox.maxWidthProperty().multiply(0.1));
        hBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < options.size(); i++) hBox.getChildren().addAll(choiceSelector(hBox, i, options.get(i)));
        bottom.getChildren().addAll(hBox);
    }

    /**
     * Brief produces a choice selector
     * @param hBox the container pane
     * @param id the id of the selector
     * @param option the available option
     * @return the selector
     */
    private StackPane choiceSelector(HBox hBox, int id, String option) {
        StackPane playerSelector = new StackPane();
        playerSelector.prefWidthProperty().bind(hBox.widthProperty().multiply(0.4));

        Text text = new Text(option);

        StackPane selector = new StackPane();
        selector.setStyle("-fx-background-color: white;");
        selector.maxWidthProperty().bind(playerSelector.widthProperty().multiply(1));
        selector.maxHeightProperty().bind(playerSelector.heightProperty().multiply(0.7));
        selector.setOpacity(0.6);
        selector.setVisible(false);

        ChangeListener<Number> listener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Function<Double, Integer> newFontSize = (ratio) -> Double.valueOf(t1.doubleValue() * ratio).intValue();
                text.setFont(new CustomFont().font(newFontSize.apply(0.017d)));

                int newRadius = Double.valueOf(t1.doubleValue() * 0.018d).intValue();
                playerSelector.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-background-radius: " + newRadius + ";"
                );
            }
        };
        bottom.widthProperty().addListener(listener);
        listener.changed(null, null, frame.getWidth());

        playerSelector.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selector.setVisible(true);
            }
        });

        playerSelector.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selector.setVisible(false);
            }
        });

        playerSelector.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                delegate().onChoiceSelected(id);
            }
        });

        playerSelector.getChildren().addAll(text, selector);
        return playerSelector;
    }

    /**
     * Brief loads the cards selection
     * @param cards the available cards
     */
    private void loadCardSelection(List<ImmutableCard> cards) {
        //setMainText("Pick three cards");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.maxWidthProperty().bind(bottom.widthProperty().multiply(1));
        scrollPane.maxHeightProperty().bind(bottom.heightProperty().multiply(1));

        final int nCards = cards.size();

        StackPane sp = new StackPane();
        sp.prefWidthProperty().bind(scrollPane.maxWidthProperty().multiply(0.25).multiply(nCards).add(
                scrollPane.maxWidthProperty().multiply(0.01).multiply(nCards+1)
        ));
        sp.prefHeightProperty().bind(scrollPane.heightProperty());
        sp.styleProperty().bind(Bindings.concat(
                "-fx-background-radius: 10;"
        ));

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.maxWidthProperty().bind(sp.prefWidthProperty().subtract(hBox.spacingProperty().multiply(2)));
        hBox.maxHeightProperty().bind(sp.prefHeightProperty());
        hBox.spacingProperty().bind(scrollPane.maxWidthProperty().multiply(0.01));
        hBox.setFillHeight(false);
        //hBox.setStyle("-fx-background-color: white");

        for (int i = 0; i < nCards; i++) hBox.getChildren().addAll(getCard(scrollPane, i, cards.get(i).getId()));
        //hBox.getChildren().addAll(background);
        sp.getChildren().addAll(hBox);
        scrollPane.setContent(sp);
        bottom.getChildren().addAll(scrollPane);

        ChangeListener<Number> listener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                int newRadius = Double.valueOf(t1.doubleValue() * 0.009d).intValue();
                int newRadius2 = Double.valueOf(t1.doubleValue() * 0.007d).intValue();
                int newBorder = Double.valueOf(t1.doubleValue() * 0.003d).intValue();
                scrollPane.setStyle(
                        "-fx-background: transparent; -fx-background-color: transparent; " +
                                "-fx-background-radius: " + newRadius + ";" +
                                "-fx-border-radius: " + newRadius2 + ";" +
                                "-fx-border-color: white;" +
                                "-fx-border-width: " + newBorder + ";"
                );
            }
        };
        bottom.widthProperty().addListener(listener);
        listener.changed(null, null, bottom.getWidth());
    }

    /**
     * Brief produces a card pane
     * @param sp the container pane
     * @param i the id of the pane
     * @param cardId the card id
     * @return the card selector pane
     */
    private StackPane getCard(ScrollPane sp, int i, int cardId) {
        StackPane imageContainer = new StackPane();
        imageContainer.prefWidthProperty().bind(sp.maxWidthProperty().multiply(0.25));
        imageContainer.prefHeightProperty().bind(imageContainer.prefWidthProperty().multiply(1.616883116883117));

        StackPane decorationContainer = new StackPane();
        decorationContainer.prefWidthProperty().bind(imageContainer.prefWidthProperty());
        decorationContainer.prefHeightProperty().bind(imageContainer.prefHeightProperty());
        decorationContainer.setStyle(
                "-fx-background: transparent; -fx-background-color: transparent; \n"
        );
        decorationContainer.setOpacity(0.97);
        decorationContainer.setVisible(false);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.WHITE);
        decorationContainer.setEffect(dropShadow);

        Image backgroundImage = null;
        try {
            backgroundImage = new Image(Objects.requireNonNull(GUIScene.class.getResource(
                    "/images/godCards/" + (cardId < 10 ? 0 : "") + cardId + ".png"))
                    .toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ImageView backgroundView = new ImageView();
        backgroundView.setImage(backgroundImage);
        backgroundView.setSmooth(true);
        backgroundView.setCache(true);
        backgroundView.setPreserveRatio(true);
        backgroundView.fitWidthProperty().bind(imageContainer.prefWidthProperty().multiply(0.9));

        backgroundView.setId(Integer.toString(i));
        selectedMap.put(i, Boolean.FALSE);

        backgroundView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ImageView image = (ImageView) mouseEvent.getSource();
                System.out.println(image.getId());
                if (!selectedMap.get(i) && selectedCount() < numberOfSelections) {
                    String currentStyle = decorationContainer.getStyle();
                    decorationContainer.setStyle(
                            "-fx-background: transparent; -fx-background-color: white; " +
                                    currentStyle.substring(currentStyle.indexOf('\n'))
                    );
                    backgroundView.setEffect(new Bloom(0.75));
                    selectedMap.put(i, true);
                    if (selectedCount().equals(numberOfSelections)) {
                        confirmLayer.setVisible(true);
                    }
                    runAsync(() -> delegate().onCardSelected(i));
                } else if (selectedMap.get(i)) {
                    String currentStyle = decorationContainer.getStyle();
                    decorationContainer.setStyle(
                            "-fx-background: transparent; -fx-background-color: transparent; " +
                                    currentStyle.substring(currentStyle.indexOf('\n'))
                    );
                    backgroundView.setEffect(null);
                    if (selectedCount().equals(numberOfSelections)) {
                        confirmLayer.setVisible(false);
                    }
                    selectedMap.put(i, false);
                    confirmLayer.setVisible(false);
                    runAsync(() -> delegate().onCardUnselected(i));
                }
            }
        });

        imageContainer.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!selectedMap.get(i) && selectedCount() < numberOfSelections) decorationContainer.setVisible(true);
            }
        });

        imageContainer.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!selectedMap.get(i) && selectedCount() < numberOfSelections) decorationContainer.setVisible(false);
            }
        });

        bottom.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                int newRadius = Double.valueOf(t1.doubleValue() * 0.009d).intValue();
                int newRadius2 = Double.valueOf(t1.doubleValue() * 0.007d).intValue();
                int newBorder = Double.valueOf(t1.doubleValue() * 0.003d).intValue();
                decorationContainer.setStyle(
                        decorationContainer.getStyle().substring(0, decorationContainer.getStyle().indexOf('\n') + 1) +
                                "-fx-background-radius: " + newRadius + ";" +
                                "-fx-border-radius: " + newRadius2 + ";" +
                                "-fx-border-color: white;" +
                                "-fx-border-width: " + newBorder + ";"
                );
            }
        });

        imageContainer.getChildren().addAll(decorationContainer, backgroundView);
        return imageContainer;
    }

    /**
     * Brief sets the main text of the scene
     * @param text the setting text
     */
    public void setMainText(String text) {
        mainText.setText(text);
    }

    /**
     * Brief returns the count of selected currently cards
     * @return the number of the cards being selected currently
     */
    private Integer selectedCount() {
        return Long.valueOf(selectedMap.values().stream().filter((bool) -> bool).count()).intValue();
    }
}
