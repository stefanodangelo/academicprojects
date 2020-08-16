package it.polimi.ingsw.santorini.model;



/**
 * Brief Enums all the possible game modes:
 * -BASIC consists in playing just with the Workers
 * -COMPLETE includes simple gods
 * -ADVANCED includes also advanced gods
 */
public enum GameMode {
    BASIC("BASIC"),
    COMPLETE("COMPLETE"),
    ADVANCED("ADVANCED");

    private final String mode;

    GameMode(String mode){
        this.mode = mode;
    }
    public String getMode(){
        return mode;
    }
    public static GameMode modeByInt(int mode) {
        return GameMode.values()[mode];
    }
}
