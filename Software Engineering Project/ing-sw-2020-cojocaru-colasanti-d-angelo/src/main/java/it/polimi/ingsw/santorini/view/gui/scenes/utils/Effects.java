package it.polimi.ingsw.santorini.view.gui.scenes.utils;

import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;

/**
 * Brief Effect factory
 * @see Effect
 */
public class Effects {

    /**
     * Brief returns a GaussianBlur, the most likely to be used
     * @return Effect blur
     */
    public static Effect blur() {
        return new GaussianBlur();
    }
}
