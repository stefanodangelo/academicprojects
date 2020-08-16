package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Brief Represents an expansion that has a custom object, a type (can be expansive or restrictive), one or more markers
 * @param <T> The type of the object being wrapped in the expansion
 * @param <S> The type of the markers that can be coupled with the expansion
 */
public class Expansion<T, S> {
    private final T object;
    private ExpansionType type;
    private List<S> markers;

    /**
     * Brief Constructor that sets the object, the type and the list of markers for the expansion
     * @param object The object involved
     * @param type The expansion type involved
     * @param markers The list of markers coupled with the expansion
     */
    public Expansion(T object, ExpansionType type, List<S> markers) {
        this.object = object;
        this.type = type;
        this.markers = new ArrayList<>(markers);
    }

    /**
     * Brief Constructor that sets the object and the expansion type
     * @param object The object involved
     * @param type The expansion type involved
     */
    public Expansion(T object, ExpansionType type) {
        this.object = object;
        this.type = type;
    }

    /**
     * Brief Constructor that sets the object, the expansion type through a Boolean and the list of markers for the
     * expansion
     * @param object The object involved
     * @param expansive The expansion type as a Boolean, it must be true to request expansive, false for restrictive
     * @param markers The list of markers coupled with the expansion
     */
    public Expansion(T object, Boolean expansive, List<S> markers) {
        this.object = object;
        this.type = ExpansionType.byBoolean(expansive);
        this.markers = new ArrayList<>(markers);
    }

    /**
     * Brief Constructor that sets the object, the expansion type through a Boolean and the list of markers for the
     * expansion as varargs
     * @param object The object involved
     * @param expansive The expansion type as a Boolean, it must be true to request expansive, false for restrictive
     * @param markers The list of markers coupled with the expansion as varargs
     */
    @SafeVarargs
    public Expansion(T object, Boolean expansive, S... markers) {
        this(object, expansive, Arrays.asList(markers));
    }

    /**
     * Brief Constructor that sets the object and the expansion type as Boolean
     * @param object The object involved
     * @param expansive The expansion type as a Boolean, it must be true to request expansive, false for restrictive
     */
    public Expansion(T object, Boolean expansive) {
        this.object = object;
        this.type = ExpansionType.byBoolean(expansive);
    }

    /**
     * Brief Constructor that sets the object and the list of markers for the expansion
     * @param object The object involved
     * @param markers The list of expansions involved
     */
    public Expansion(T object, List<S> markers) {
        this.object = object;
        this.markers = new ArrayList<>(markers);
    }

    /**
     * Brief Constructor that sets the object and the list of markers for the expansion as varargs
     * @param object The object involved
     * @param markers The list of expansions involved as varargs
     */
    @SafeVarargs
    public Expansion(T object, S... markers) {
        this(object, Arrays.asList(markers));
    }

    /**
     * Brief Constructor that sets the object
     * @param object The object involved
     */
    public Expansion(T object) {
        this.object = object;
    }

    /**
     * Brief Getter for the markers' list
     * @return List list of markers coupled with the expansion
     */
    public List<S> getMarkers() {
        return markers;
    }

    /**
     * Brief Getter for the expansion object
     * @return T the expansion object
     */
    public T getObject() {
        return object;
    }

    /**
     * Brief Getter for the type of the expansion
     * @return ExpansionType the type of the expansion
     */
    public ExpansionType getType() {
        return type;
    }

    /**
     * Brief Checker that tells if the expansion has a type set
     * @return Boolean true if the expansion's type is not null, false otherwise
     */
    public Boolean hasType() {
        return type != null;
    }
}
