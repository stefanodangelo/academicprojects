package it.polimi.ingsw.santorini.view.gui.scenes.utils;

import javafx.beans.value.ObservableValue;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

/**
 * Brief utility usable to bind Region's or ImageView's different sizes
 * @see Region
 * @see ImageView
 * @see SizeType
 */
public class Binder {

    /**
     * Brief binds a Region's size
     * @param region the region
     * @param type the affected size type
     * @param width the observable width
     * @param height the observable height
     */
    public static void bind(Region region, SizeType type, ObservableValue<Number> width, ObservableValue<Number> height) {
        switch (type) {
            case MAX:
                bindMax(region, width, height);
                break;
            case MIN:
                bindMin(region, width, height);
                break;
            case PREF:
                bindPref(region, width, height);
                break;
        }
    }

    /**
     * Brief binds a Region's max size
     * @param region the region
     * @param width the observable width
     * @param height the observable height
     */
    public static void bindMax(Region region, ObservableValue<Number> width, ObservableValue<Number> height) {
        if (width != null) region.maxWidthProperty().bind(width);
        if (height != null) region.maxHeightProperty().bind(height);
    }

    /**
     * Brief binds a Region's min size
     * @param region the region
     * @param width the observable width
     * @param height the observable height
     */
    public static void bindMin(Region region, ObservableValue<Number> width, ObservableValue<Number> height) {
        if (width != null) region.minWidthProperty().bind(width);
        if (height != null) region.minHeightProperty().bind(height);
    }

    /**
     * Brief binds a Region's pref size
     * @param region the region
     * @param width the observable width
     * @param height the observable height
     */
    public static void bindPref(Region region, ObservableValue<Number> width, ObservableValue<Number> height) {
        if (width != null) region.prefWidthProperty().bind(width);
        if (height != null) region.prefHeightProperty().bind(height);
    }

    /**
     * Brief binds an ImageView's fit size
     * @param view the ImageView
     * @param width the observable width
     * @param height the observable height
     */
    public static void bindFit(ImageView view, ObservableValue<Number> width, ObservableValue<Number> height) {
        if (width != null) view.fitWidthProperty().bind(width);
        if (height != null) view.fitHeightProperty().bind(height);
    }
}
