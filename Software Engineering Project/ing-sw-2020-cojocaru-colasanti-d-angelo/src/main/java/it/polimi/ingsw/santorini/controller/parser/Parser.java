package it.polimi.ingsw.santorini.controller.parser;

import it.polimi.ingsw.santorini.model.gameoperations.custom.CustomGameOperation;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

/**
 * Brief Factory class for the type of Parser to use when querying a file
 */
public abstract class Parser {
    private static final String XMLEXTENSION = "xml";

    /**
     * Brief Factory method for the Parser
     * @param filePath is the file to parse
     * @return the correct Parser's instance
     */
    public static Parser getParser(String filePath){
        if(filePath.endsWith(XMLEXTENSION))
            return new DOMParser();
        return null;
    }

    /**
     * Brief Parses a file specified by its path to extract cards' meta data
     * @param filePath is the path of the file to parse
     * @return a structure containing the file's queried information with each key corresponding to each god (with respect to file order)
     */
    public abstract HashMap<Integer, HashMap<String, String>> parseFileForMeta(String filePath);

    /**
     * Brief Parses a file specified by its path to extract cards' effects data
     * @param filePath is the path of the file to parse
     * @param effectType is the effect type being parsed
     * @param effectSupplier is the supplier of the effect being parsed
     * @param <T> is the type of the CustomGameOperation that contains the effect being parsed
     * @return a structure containing the file's queried information with each key corresponding to each god (with respect to file order)
     */
    public abstract <T extends CustomGameOperation<?>>
    HashMap<Integer, List<T>> parseFileForEffects(String filePath, String effectType, Supplier<T> effectSupplier);
}
