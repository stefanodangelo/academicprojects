package it.polimi.ingsw.santorini.view.gui.scenes;

import it.polimi.ingsw.santorini.view.gui.scenes.delegates.HomeSceneDelegate;
import it.polimi.ingsw.santorini.view.gui.scenes.utils.CustomFont;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * Brief Class responsible for populating the Home window, which is opened as soon as the game is launched in GUI mode.
 *       It allows the user to access the Settings Scene through the button "Settings" and to start a connection towards
 *       the server using "Start"
 */

public class HomeScene extends GUIScene<HomeSceneDelegate> {
    /**
     * Brief The standard dimensions for the HomeScene window
     */
    private final static Double defaultWidth = 1422.2222222222222222222222222222d;
    private final static Double defaultHeight = 800d;
    private final static String title = "Santorini Home";

    final double ratio = 1.7777777777777777777777777777778;
    StackPane frame;
    ImageView backgroundView;

    public HomeScene() {
        super(defaultWidth,defaultHeight,title);
    }

    /**
     * Brief This method is tasked with adding all necessary elements to the scene and the transitions towards other scenes
     */
    @Override
    public void customLoadScene() {
        root().setStyle("-fx-background-color: #000000");

        frame = new StackPane();
        frame.maxHeightProperty().bind(Bindings.min(root().heightProperty(), root().widthProperty().divide(ratio)));
        frame.maxWidthProperty().bind(frame.maxHeightProperty().multiply(ratio));

        Image backgroundImage = null;
        try {
            backgroundImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("title_background_without_buttons.jpg"))
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

        GridPane gridPane = new GridPane();

        Button start = new Button();
        start.setText("Start Game");
        start.setFont(new CustomFont().font(15));
        start.setOnAction(e -> {
            delegate().onStart();
        });

        Button settings = new Button();
        settings.setText("Settings");
        settings.setFont(new CustomFont().font(15));
        settings.setOnAction(e -> {
            delegate().onSettingsPressed();
        });

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(100);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_CENTER);
        hbBtn.setSpacing(980);
        hbBtn.getChildren().addAll(start, settings);
        gridPane.add(hbBtn, 0, 2, 2, 4);

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.BASELINE_CENTER);
        Text credits = new Text("Developed by Cristian Cojocaru, Luca Colasanti, Stefano D'Angelo");
        credits.setFont(new CustomFont().font(10));
        pane.getChildren().add(credits);

        frame.getChildren().add(backgroundView);
        frame.getChildren().add(pane);
        frame.getChildren().add(gridPane);

        root().getChildren().add(frame);
    }
}

