package it.polimi.ingsw;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JavaFX App
 */
public class App extends Application {

    StackPane root;
    GridPane gridPane;
    StackPane frame;

    final double ratio = 1.7777777777777777777777777777778;
    ImageView backgroundView = new ImageView();
    Stage stage;

    MediaPlayer player;

    final List<Animation> ongoingAnimations = new ArrayList<>();
    final List<Long> initials = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        root = new StackPane();
        root.setStyle("-fx-background-color: #000000");

        frame = new StackPane();
        //frame.setStyle("-fx-background-color: #669900");
        frame.maxHeightProperty().bind(Bindings.min(root.heightProperty(), root.widthProperty().divide(ratio)));
        frame.maxWidthProperty().bind(frame.maxHeightProperty().multiply(ratio));

        Image backgroundImage = null;
        try {
            backgroundImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("board.png"))
                    .toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        backgroundView.setImage(backgroundImage);
        backgroundView.setSmooth(true);
        backgroundView.setCache(true);
        backgroundView.fitWidthProperty().bind(frame.maxWidthProperty());
        backgroundView.fitHeightProperty().bind(frame.maxHeightProperty());

        BorderPane borderPane = new BorderPane();
        //borderPane.setStyle("-fx-background-color: #6d00f7");
        borderPane.maxWidthProperty().bind(frame.widthProperty());
        borderPane.maxHeightProperty().bind(frame.heightProperty());

        gridPane = new GridPane();
        //gridPane.setStyle("-fx-background-color: #420000");
        gridPane.prefWidthProperty().bind(frame.widthProperty().multiply(0.40885416666666666666666666666667));
        gridPane.prefHeightProperty().bind(gridPane.prefWidthProperty());
        Pane pl = new Pane();
        //pl.setStyle("-fx-background-color: #420000");
        pl.prefHeightProperty().bind(frame.heightProperty());
        pl.prefWidthProperty().bind(frame.widthProperty().subtract(gridPane.prefWidthProperty()).divide(2));
        Pane pr = new Pane();
        //pr.setStyle("-fx-background-color: #420000");
        pr.prefHeightProperty().bind(frame.heightProperty());
        pr.prefWidthProperty().bind(frame.widthProperty().subtract(gridPane.prefWidthProperty()).divide(2));
        Pane pu = new Pane();
        //pu.setStyle("-fx-background-color: #420000");
        pu.prefWidthProperty().bind(frame.widthProperty());
        pu.prefHeightProperty().bind(frame.heightProperty().subtract(gridPane.prefWidthProperty()).multiply(0.52434456928838951310861423220974));
        Pane pb = new Pane();
        //pb.setStyle("-fx-background-color: #420000");
        pb.prefWidthProperty().bind(frame.widthProperty());
        pb.prefHeightProperty().bind(frame.heightProperty().subtract(gridPane.prefWidthProperty()).subtract(pu.prefHeightProperty()));
        borderPane.setLeft(pl);
        borderPane.setRight(pr);
        borderPane.setTop(pu);
        borderPane.setBottom(pb);
        borderPane.setCenter(gridPane);

        for (int i = 0; i < 5; i++) {
            ColumnConstraints c = new ColumnConstraints();
            //c.setPercentWidth(23);
            //c.setMaxWidth(73);
            c.prefWidthProperty().bind(frame.widthProperty().multiply(0.074803141));
            gridPane.getColumnConstraints().add(c);

            RowConstraints r = new RowConstraints();
            //r.setPercentHeight(23);
            //r.setMaxHeight(73);
            r.prefHeightProperty().bind(gridPane.getColumnConstraints().get(0).prefWidthProperty());
            gridPane.getRowConstraints().add(r);
        }
        gridPane.vgapProperty().bind(gridPane.prefWidthProperty().subtract(frame.widthProperty().multiply(0.075).multiply(5)).divide(4));
        gridPane.hgapProperty().bind(gridPane.vgapProperty());

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                StackPane p1 = new StackPane();
                //p1.setStyle("-fx-background-color: #420fff");
                p1.prefWidthProperty().bind(gridPane.getColumnConstraints().get(0).prefWidthProperty());
                p1.prefHeightProperty().bind(p1.prefWidthProperty());
                GridPane.setConstraints(p1, i, j);
                gridPane.getChildren().add(p1);
            }
        }

        frame.getChildren().add(backgroundView);
        frame.getChildren().add(borderPane);
        root.getChildren().add(frame);
        //Scene scene = new Scene(root, 960, 540, Color.BLACK);
        Scene scene = new Scene(root,1422.2222222222222222222222222222,800,Color.BLACK);
        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.show();

        frame.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Platform.runLater(() -> {
                    ongoingAnimations.forEach((animation) -> {
                        animation.stop();
                        long delta = System.currentTimeMillis() - initials.get(ongoingAnimations.indexOf(animation));
                        Duration current = Duration.millis(Math.max(delta, animation.getCurrentTime().toMillis()));
                        animation.playFrom(current);
                    });
                });
            }
        });

        frame.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Platform.runLater(() -> {
                    ongoingAnimations.forEach((animation) -> {
                        animation.stop();
                        long delta = System.currentTimeMillis() - initials.get(ongoingAnimations.indexOf(animation));
                        Duration current = Duration.millis(Math.max(delta, animation.getCurrentTime().toMillis()));
                        animation.playFrom(current);
                    });
                });
            }
        });

        test();
    }

    public static void main(String[] args) {
        launch();
    }

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
            prova = new Image(Objects.requireNonNull(getClass().getClassLoader()
                    .getResource((level < 4 ? "BuildingBlock0" + level : "Dome") + ".png"))
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

    public void pushWorker(Boolean female, Integer color, Integer x, Integer y) {
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
            prova = new Image(Objects.requireNonNull(getClass().getClassLoader()
                    .getResource( (female ? "Female" : "Male") + "Builder" + color + ".png"))
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

    public StackPane getStackFromGrid(int i, int j) {
        return (StackPane) gridPane.getChildren().stream().filter((node) ->
                GridPane.getColumnIndex(node) == i && GridPane.getRowIndex(node) == j
        ).collect(Collectors.toList()).get(0);
    }

    public void pop(int x, int y) {
        StackPane stack = getStackFromGrid(x,y);
        stack.getChildren().remove(stack.getChildren().size() - 1);
    }

    public void selectable(Integer color, int x, int y) {
        StackPane stack = getStackFromGrid(x, y);
        Pane p = new Pane();
        String c = "#000000";
        switch (color) {
            case 1:
                c = "#2e775a";
                break;
            case 2:
                c = "#983537";
                break;
            case 3:
                c = "#a89800";
                break;
        }
        p.setStyle("-fx-background-color: " + c);
        stack.getChildren().add(p);
    }

    public void move(int x, int y, int x2, int y2) {
        StackPane stack = getStackFromGrid(1,1);
        Pane node = (Pane) stack.getChildren().get(stack.getChildren().size()-1);

        Path path = new Path();

        MoveTo moveTo = new MoveTo();
        moveTo.xProperty().bind(stack.widthProperty().divide(2));
        moveTo.yProperty().bind(stack.heightProperty().divide(2));

        LineTo lineTo = new LineTo();
        lineTo.xProperty().bind(stack.widthProperty().divide(2).add(gridPane.vgapProperty()).add(stack.widthProperty()));
        lineTo.yProperty().bind(stack.heightProperty().divide(2));

        path.getElements().add(moveTo);
        path.getElements().add(lineTo);
        final PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(5000));
        pathTransition.setNode(node);
        pathTransition.setPath(path);
        pathTransition.setCycleCount(1);
        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ongoingAnimations.remove(pathTransition);
                stack.getChildren().remove(stack.getChildren().size()-1);
                StackPane stackPane = getStackFromGrid(2,1);
                stackPane.getChildren().add(node);
                node.setTranslateX(0);
                node.setTranslateY(0);
            }
        });

        ongoingAnimations.add(pathTransition);
        initials.add(System.currentTimeMillis());
        pathTransition.play();
    }

    public void test() {
        Platform.runLater(() -> {
            //pushTest();
            //popTest();
            //selectableTest();
            moveTest();
            //soundTest();
        });
    }

    public void pushTest() {
        Random rnd = new Random();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (rnd.nextDouble() > 0.5) {
                    int level = rnd.nextInt(4) + 1;
                    int _k = Math.min(level, 3);
                    for (int k = 1; k < _k+1; k++) pushBlock(k,i,j);
                    if (level == 4) {
                        if (rnd.nextDouble() > 0.5) {
                            pushWorker(rnd.nextBoolean(), rnd.nextInt(3)+1,i,j);
                        } else pushBlock(4,i,j);
                    }
                }
            }
        }
    }

    public void popTest() {
        pushBlock(1,0,0);
        pushBlock(2,0,0);
        pushBlock(3,0,0);
        pushBlock(4,0,0);

        pop(0,0);
    }

    public void selectableTest() {
        pushWorker(true,2,0,0);
        selectable(2,1,0);
        selectable(2,0,1);
        selectable(2,1,1);
    }

    public void moveTest() {
        pushWorker(false,1,1,1);
        move(0,0,0,0);
    }

    public void soundTest() {
        String musicFile = "audio.mp3";
        Media sound = new Media(new File(musicFile)
                .toURI().toString());
        player = new MediaPlayer(sound);
        player.play();
    }
}