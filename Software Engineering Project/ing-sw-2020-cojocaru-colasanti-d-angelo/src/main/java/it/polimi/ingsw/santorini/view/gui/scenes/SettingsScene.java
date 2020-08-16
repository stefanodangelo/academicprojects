package it.polimi.ingsw.santorini.view.gui.scenes;

import it.polimi.ingsw.santorini.view.gui.scenes.delegates.SettingsSceneDelegate;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

/**
 * Brief This class contains dedicated fields to allow the modifications of the standard ip and port to use during the connection.
 *       It is accessed from the Settings button in the Home Scene
 */
public class SettingsScene extends GUIScene<SettingsSceneDelegate>{
    private final static Double defaultWidth = 500d;
    private final static Double defaultHeight = 500d;
    private final static String title = "SettingsScene";

    private static final String C = "#000000";

    final double ratio = 1;

    StackPane frame;

    public SettingsScene() {
        super(defaultWidth,defaultHeight,title);
    }

    /**
     * Brief This method is tasked with adding all necessary elements to the scene, the communication of modified parameters
     *       and the transition back to the HomeScene
     */
    @Override
    public void customLoadScene() {
        root().setStyle("-fx-background-color: #000000");

        frame = new StackPane();
        frame.setStyle("-fx-background-color: #1c9bdf");
        frame.maxHeightProperty().bind(Bindings.min(root().heightProperty(), root().widthProperty().divide(ratio)));
        frame.maxWidthProperty().bind(frame.maxHeightProperty().multiply(ratio));

        GridPane upperGridPane = new GridPane();
        upperGridPane.setAlignment(Pos.TOP_CENTER);
        upperGridPane.setVgap(24.5);

        Text panelTitle = new Text("Settings Panel");
        panelTitle.setFont(Font.loadFont("file:resources/fonts/HERAKLES.TTF", 40));
        panelTitle.setFill(Color.valueOf(C));
        upperGridPane.add(panelTitle, 0, 2, 2, 4);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(40);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Text IP = new Text("IP:");
        IP.setFont(Font.loadFont("file:resources/fonts/HERAKLES.TTF", 15));
        IP.setFill(Color.valueOf(C));
        gridPane.add(IP, 0, 1);

        TextField IPField = new TextField();
        gridPane.add(IPField, 1, 1);

        Text port = new Text("Port:");
        port.setFont(Font.loadFont("file:resources/fonts/HERAKLES.TTF", 15));
        port.setFill(Color.valueOf(C));
        gridPane.add(port, 0, 2);

        TextField portField = new TextField();
        gridPane.add(portField, 1, 2);

        GridPane lowerGridPane = new GridPane();
        lowerGridPane.setAlignment(Pos.BOTTOM_CENTER);
        lowerGridPane.setVgap(70);

        final Text notificationMessage = new Text();
        lowerGridPane.add(notificationMessage, 0, 0, 1, 3);

        Button apply = new Button();
        apply.setText("Apply");
        apply.setFont(Font.loadFont("file:resources/fonts/HERAKLES.TTF", 15));
        apply.setOnAction(actionEvent -> {
            notificationMessage.setFill(Color.valueOf(C));
            notificationMessage.setFont(Font.loadFont("file:resources/fonts/HERAKLES.TTF", 12));
            if( checkInputs(IPField.getText(), portField.getText()) ){
                delegate().onApplyPressed(IPField.getText(), Integer.valueOf(portField.getText()));
                notificationMessage.setText("Settings successfully changed. Please press 'Done' to go back to the main menu.");
            }
            else notificationMessage.setText("Please fill all available text fields above correctly to modify settings.");
        });

        gridPane.add(apply, 2, 4);

        Button done = new Button();
        done.setText("Done");
        done.setFont(Font.loadFont("file:resources/fonts/HERAKLES.TTF", 15));
        done.setOnAction(e -> {
            delegate().onDonePressed();
        });

        gridPane.add(done, 3, 4);

        root().getChildren().addAll(frame, upperGridPane, lowerGridPane, gridPane);
    }

    /**
     * Brief Checks if user inputs are correct
     * @param IP is the IP chosen by the user
     * @param port is the port chosen by the user
     * @return Boolean.TRUE if the inputs are acceptable
     */
    private Boolean checkInputs(String IP, String port) {
        return ( !IP.isEmpty() && !port.isEmpty() && Integer.valueOf(port) <= 65535);
    }
}

