package it.polimi.ingsw.santorini.view.gui.scenes.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Brief utility usable to produce various types of JavaFX-CSS fragments and combine them smartly through unwrapping and
 * consequent re-wrapping
 */
public class CSS {

    /**
     * Brief produces a CSS property-value line fragment that ends with semicolon
     * @param domain String the property domain
     * @param property String the property
     * @param value String the value
     * @return String CSS fragment
     */
    public static String propertyValue(String domain, String property, String value) {
        return propertyValue(domain + property, value);
    }

    /**
     * Brief produces a CSS property-value line fragment that ends with semicolon
     * @param domain String the property domain
     * @param property String the property
     * @param value Integer the value
     * @return String CSS fragment
     */
    public static String propertyValue(String domain, String property, Integer value) {
        return propertyValue(domain, property, (value != null) ? value.toString() : null);
    }

    /**
     * Brief produces a CSS property-value line fragment that ends with semicolon
     * @param domain String the property domain
     * @param property String the property
     * @param value Double the value
     * @return String CSS fragment
     */
    public static String propertyValue(String domain, String property, Double value) {
        return propertyValue(domain, property, (value != null) ? value.intValue() : null);
    }

    /**
     * Brief produces a CSS property-value line fragment that ends with semicolon
     * @param property String the property
     * @param value String the value
     * @return String CSS fragment
     */
    public static String propertyValue(String property, String value) {
        return (value != null) ? "-fx-" + property + ": " + value + ";" : "";
    }

    /**
     * Brief produces a CSS property-value line fragment that ends with semicolon
     * @param property String the property
     * @param value Integer the value
     * @return String CSS fragment
     */
    public static String propertyValue(String property, Integer value) {
        return propertyValue(property, (value != null) ? value.toString() : null);
    }

    /**
     * Brief produces a CSS property-value line fragment that ends with semicolon
     * @param property String the property
     * @param value Double the value
     * @return String CSS fragment
     */
    public static String propertyValue(String property, Double value) {
        return propertyValue(property, (value != null) ? value.intValue() : null);
    }

    /**
     * Brief produces a CSS for background-color
     * @param color String color
     * @return String CSS fragment
     */
    public static String background(String color) {
        final String domain = "background-";
        return propertyValue(domain, "color", color);
    }

    /**
     * Brief produces a CSS for background-color and background-radius
     * @param color String color
     * @param radius Double radius
     * @return String CSS fragment
     */
    public static String background(String color, Double radius) {
        final String domain = "background-";
        return background(color) +
                propertyValue(domain, "radius", radius);
    }

    /**
     * Brief produces a CSS for border-color
     * @param color String color
     * @return String CSS fragment
     */
    public static String border(String color) {
        final String domain = "border-";
        return propertyValue(domain, "color", color);
    }

    /**
     * Brief produces a CSS for border-color, border-radius and border-width
     * @param color String color
     * @param radius Double radius
     * @param width Double width
     * @return String CSS fragment
     */
    public static String border(String color, Double radius, Double width) {
        final String domain = "border-";
        return border(color) +
                propertyValue(domain, "radius", radius) +
                propertyValue(domain, "width", width);
    }

    /**
     * Brief produces a CSS for background-color, background-radius, border-color, border-radius and border-width
     * @param backgroundColor String background color
     * @param backgroundRadius Double background radius
     * @param borderColor String border color
     * @param borderRadius Double border radius
     * @param borderWidth Double border width
     * @return String CSS fragment
     */
    public static String style(String backgroundColor, Double backgroundRadius,
                               String borderColor, Double borderRadius, Double borderWidth) {
        return background(backgroundColor, backgroundRadius) +
                border(borderColor, borderRadius, borderWidth);
    }

    /**
     * Brief produces a CSS for background-radius, border-radius and border-width
     * @param backgroundRadius Double background radius
     * @param borderRadius Double border radius
     * @param borderWidth Double border width
     * @return String CSS fragment
     */
    public static String styleNoColor(Double backgroundRadius, Double borderRadius, Double borderWidth) {
        return background(null, backgroundRadius) +
                border(null, borderRadius, borderWidth);
    }

    /**
     * Brief unwraps a CSS fragment to a dictionary
     * @param css the css
     * @return the dictionary
     */
    public static Map<String, String> unwrap(String css) {
        Map<String, String> dictionaryCSS = new HashMap<>();
        int lastIndex = -1;
        while((lastIndex + 1) < (css.length() - 1)) {
            lastIndex++;
            int index = css.indexOf(';', lastIndex);
            String propertyValue = css.substring(lastIndex, index);
            String property, value;
            property = propertyValue.substring(4, propertyValue.indexOf(':'));
            value = propertyValue.substring(propertyValue.indexOf(':') + 2);
            dictionaryCSS.put(property, value);
            lastIndex = index;
        }
        return dictionaryCSS;
    }

    /**
     * Brief wraps a dictionary to a CSS fragment
     * @param dictionaryCSS the dictionary
     * @return String CSS fragment
     */
    public static String wrap(Map<String, String> dictionaryCSS) {
        final String[] css = {""};
        dictionaryCSS.forEach((property, value) -> css[0] = css[0] + propertyValue(property, value));
        return css[0];
    }

    /**
     * Brief merges two CSS dictionaries by putting each entry of the second one in the first one
     * @param css1 first CSS dictionary
     * @param css2 second CSS dictionary
     * @return the merged CSS dictionary
     */
    public static Map<String, String> merge(Map<String, String> css1, Map<String, String> css2) {
        css2.forEach(css1::put);
        return css1;
    }
}
