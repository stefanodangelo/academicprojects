package it.polimi.ingsw.santorini.view.gui.scenes.utils;

import it.polimi.ingsw.santorini.view.gui.scenes.GUIScene;
import javafx.beans.value.ObservableValue;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URISyntaxException;
import java.util.Objects;

/**
 * Brief custom ImageView useful to easily load it from a source and constrain its size
 * @see ImageView
 */
public class SimpleImageView extends ImageView {

    /**
     * Brief constructor that loads the ImageView from a given PNG image source
     * @param source the .png image source
     */
    public SimpleImageView(String source) {
        Image image = null;
        try {
            image = new Image(Objects.requireNonNull(GUIScene.class.getResource(source + ".png"))
                    .toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        setImage(image);
        setSmooth(true);
        setCache(true);
    }

    /**
     * Brief constructor that loads the ImageView from a given PNG image source and binds its size
     * @param source the .png image source
     * @param width the observable width
     * @param height the observable height
     */
    public SimpleImageView(String source, ObservableValue<Number> width, ObservableValue<Number> height) {
        this(source);
        Binder.bindFit(this, width, height);
    }

    /**
     * Brief constructor that loads the ImageView from a given PNG image source and binds its size. It sets also an Effect.
     * @param source the .png image source
     * @param width the observable width
     * @param height the observable height
     * @param effect the Effect
     */
    public SimpleImageView(String source, ObservableValue<Number> width, ObservableValue<Number> height, Effect effect) {
        this(source, width, height);
        setEffect(effect);
    }
}
