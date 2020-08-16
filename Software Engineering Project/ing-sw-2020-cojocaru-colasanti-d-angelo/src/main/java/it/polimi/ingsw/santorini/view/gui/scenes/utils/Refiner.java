package it.polimi.ingsw.santorini.view.gui.scenes.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.Map;

/**
 * Brief utility class usable to refine in an easy and clean way various Region's aspects ranging from size to style.
 * It uses Binder and CSS to perform such tasks.
 * @see Region
 * @see Binder
 * @see CSS
 */
public class Refiner {

    /**
     * Brief Binds a Region's size to an observable width and height
     * @param region the region
     * @param type the type of the affected size
     * @param width the observable width
     * @param height the observable height
     */
    public static void size(Region region, SizeType type, ObservableValue<Number> width, ObservableValue<Number> height) {
        Binder.bind(region, type, width, height);
    }

    /**
     * Brief Binds a Region's size to an observable width and height. Sets the Region's opacity
     * @param region the region
     * @param opacity the opacity
     * @param type the type of the affected size
     * @param width the observable width
     * @param height the observable height
     */
    public static void sizeOpacity(Region region, Double opacity, SizeType type, ObservableValue<Number> width, ObservableValue<Number> height) {
        Binder.bind(region, type, width, height);
        region.setOpacity(opacity);
    }

    /**
     * Brief it overrides a Region's current style
     * @param region the region
     * @param style the provided style
     */
    public static void newStyle(Region region, String style) {
        region.setStyle(style);
    }

    /**
     * Brief it updates a Region's current style by merging it with another one
     * @param region the region
     * @param style the provided style
     */
    public static void styleUpdate(Region region, String style) {
        Map<String, String> cssOld = CSS.unwrap(region.getStyle());
        Map<String, String> cssNew = CSS.unwrap(style);
        Map<String, String> cssUpdated = CSS.merge(cssOld, cssNew);
        String updatedStyle = CSS.wrap(cssUpdated);
        region.setStyle(updatedStyle);
    }

    /**
     * Brief updates a Region's style to set its background-color
     * @param region the region
     * @param color the color
     */
    public static void backgroundColor(Region region, String color) {
        styleUpdate(region, CSS.background(color));
    }

    /**
     * Brief updates a Region's style to set its border-color
     * @param region the region
     * @param color the color
     */
    public static void borderColor(Region region, String color) {
        styleUpdate(region, CSS.border(color));
    }

    /**
     * Brief updates a Region's style to set both its background-color and border-color
     * @param region the region
     * @param backgroundColor the background color
     * @param borderColor the border color
     */
    public static void color(Region region, String backgroundColor, String borderColor) {
        backgroundColor(region, backgroundColor);
        borderColor(region, borderColor);
    }

    /**
     * Brief updates a Region's style to set its background-radius, border-radius and border-width
     * @param region the region
     * @param backgroundRadius the background radius
     * @param borderRadius the border radius
     * @param borderWidth the border width
     */
    public static void radiusBorder(Region region, Double backgroundRadius, Double borderRadius, Double borderWidth) {
        Refiner.styleUpdate(region, CSS.styleNoColor(backgroundRadius, borderRadius, borderWidth));
    }

    /**
     * Brief updates more Regions' style to set their background-radius, border-radius and border-width
     * @param backgroundRadius the background radius
     * @param borderRadius the border radius
     * @param borderWidth the border width
     * @param regions the regions
     */
    public static void radiusBorder(Double backgroundRadius, Double borderRadius, Double borderWidth, Region... regions) {
        for (Region region : regions)
            radiusBorder(region, backgroundRadius, borderRadius, borderWidth);
    }

    /**
     * Brief adds more nodes to a parent
     * @param parent the parent
     * @param nodes the nodes
     */
    public static void addTo(Pane parent, Node... nodes) {
        for (Node node : nodes)
            if (node != null) parent.getChildren().add(node);
    }

    /**
     * Brief removes all nodes from a parent
     * @param parent the parent
     */
    public static void removeFrom(Pane parent) {
        parent.getChildren().removeIf((child) -> true);
    }

    /**
     * Brief add a new width change listener to a Region
     * @param region the region
     * @param listener the listener
     */
    public static void addWidthListener(Region region, ChangeListener<Number> listener) {
        region.widthProperty().addListener(listener);
    }

    /**
     * Brief add a new height change listener to a Region
     * @param region the region
     * @param listener the listener
     */
    public static void addHeightListener(Region region, ChangeListener<Number> listener) {
        region.heightProperty().addListener(listener);
    }
}
