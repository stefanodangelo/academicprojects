package it.polimi.ingsw.santorini.view.gui.scenes.utils;

import it.polimi.ingsw.santorini.view.gui.scenes.GUIScene;
import javafx.scene.text.Font;

import java.io.InputStream;

public class CustomFont {

    private final static String customFontFile = "/fonts/HERAKLES.TTF";
    private final InputStream customFontStream = GUIScene.class.getResourceAsStream(customFontFile);

    public Font font(int size) {
        return Font.loadFont(customFontStream, size);
    }
}
