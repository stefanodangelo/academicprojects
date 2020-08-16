package it.polimi.ingsw.santorini.view.gui.scenes.utils;
/**
 * Brief game map Worker
 */
public class MapWorker {

    /**
     * Brief the color
     */
    private final Integer color;

    /**
     * Brief the gender
     */
    private final String gender;

    /**
     * Brief main constructor: sets the color and the gender of the worker
     * @param color the color
     * @param gender the gender
     */
    public MapWorker(String color, String gender) {
        this.color = it.polimi.ingsw.santorini.view.Color.valueOf(color).ordinal();
        this.gender = getGenderConversion(gender);
    }

    /**
     * Brief getter for the color
     * @return the color codified as ordinal integer
     */
    public Integer getColor() {
        return color;
    }

    /**
     * Brief getter for the gender
     * @return the gender as string ("female" or "male")
     */
    public String getGender() {
        return gender;
    }

    /**
     * Brief gets a
     * @param gender as String ("female" or "male")
     * @return the gender
     */
    public static String getGenderConversion(String gender) {
        return gender.equals("▲") ? "male" : "female";
    }
}
