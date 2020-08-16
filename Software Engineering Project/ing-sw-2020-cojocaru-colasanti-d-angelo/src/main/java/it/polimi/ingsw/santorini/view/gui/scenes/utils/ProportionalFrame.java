package it.polimi.ingsw.santorini.view.gui.scenes.utils;

import javafx.beans.binding.Bindings;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Brief custom StackPane that scales proportionally inside the stage
 */
public class ProportionalFrame extends StackPane {

    /**
     * Brief constructor that binds the sizes in the proper way
     * @param root the scene's root
     * @param ratio the width / height ratio
     */
    public ProportionalFrame(Pane root, double ratio) {
        Refiner.size(this, SizeType.MAX,
                maxHeightProperty().multiply(ratio),
                Bindings.min(root.heightProperty(), root.widthProperty().divide(ratio)));
    }
}
