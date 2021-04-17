package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import it.polimi.ingsw.santorini.model.gameoperations.exceptions.ApplicationAllowanceException;
import it.polimi.ingsw.santorini.model.gameoperations.exceptions.ExpansionAllowanceException;
import it.polimi.ingsw.santorini.model.gameoperations.exceptions.MarkerLessExpansionAdded;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Brief Represents an expandable object. Expandable refers to the ability of an object's behaviour of being refined
 * by adding to it custom expansions. The expansions can be of two types (expansive and restrictive)
 * @param <T> The type of the object that refines the expandable's behaviour
 * @param <S> The type of the supported expansions' marker
 * @param <U> The type of supported expansions
 * @param <V> The type of expandable that can be merged with the expandable
 * @see Expansion Any supported expansion must extend this base class
 */
public abstract class Expandable<T, S, U extends Expansion<T, S>, V extends Expandable<T, S, U, V>> {

    /**
     * Brief returns whether this Expandable allow adding (or removing) Expansions
     */
    private Boolean expandable;

    /**
     * Brief returns whether this Expandable allow computation on its Expansions
     */
    private Boolean applicable;

    /**
     * Brief The filter by marker used to apply the expansions
     */
    protected Predicate<S> filter;

    /**
     * Brief constructor that initializes the expandable and applicable state of the Expandable.
     * Resets also the filter's state of the expandable
     * @param expandable Boolean true if expansions allowed
     * @param applicable Boolean true if applications on expansions allowed
     */
    public Expandable(Boolean expandable, Boolean applicable) {
        this.expandable = expandable;
        this.applicable = applicable;
        resetFilter();
    }

    /**
     * Brief getter for expandable
     * @return Boolean expandable
     */
    public Boolean isExpandable() {
        return expandable;
    }

    /**
     * Brief setter for expandable
     * @param expandable Boolean expandable
     */
    public void setExpandable(Boolean expandable) {
        this.expandable = expandable;
    }

    /**
     * Brief getter for applicable
     * @return Boolean applicable
     */
    public Boolean isApplicable() {
        return applicable;
    }

    /**
     * Brief setter for applicable
     * @param applicable Boolean applicable
     */
    public void setApplicable(Boolean applicable) {
        this.applicable = applicable;
    }

    /**
     * Brief setter for both expandable and applicable
     * @param expandable Boolean expandable
     * @param applicable Boolean applicable
     */
    public void setPermissions(Boolean expandable, Boolean applicable) {
        setExpandable(expandable);
        setApplicable(applicable);
    }

    /**
     * Brief throws an exception if the Expandable is not expansion-allowed
     * @throws ExpansionAllowanceException if is not expandable allowed
     */
    public void requiresExpandable() throws ExpansionAllowanceException {
        if (!isExpandable()) throw new ExpansionAllowanceException(true);
    }

    /**
     * Brief throws an exception if the Expandable is not application-allowed
     * @throws ApplicationAllowanceException if is not applicable allowed
     */
    public void requiresApplicable() throws ApplicationAllowanceException {
        if (!isApplicable()) throw new ApplicationAllowanceException(true);
    }

    /**
     * Brief throws an exception if the Expandable is expansion-allowed
     * @throws ExpansionAllowanceException if is not expandable allowed
     */
    public void requiresNotExpandable() throws ExpansionAllowanceException {
        if (!isExpandable()) throw new ExpansionAllowanceException(false);
    }

    /**
     * Brief throws an exception if the Expandable is application-allowed
     * @throws ApplicationAllowanceException if is not applicable allowed
     */
    public void requiresNotApplicable() throws ApplicationAllowanceException {
        if (!isApplicable()) throw new ApplicationAllowanceException(false);
    }

    /**
     * Brief Adds an expansion to the expandable (in the position provided by index)
     * @param expansion The expansion being added
     * @param index the position
     * @throws MarkerLessExpansionAdded Thrown if the adding expansion has an empty list of markers
     */
    public abstract void addExpansion(U expansion, Integer index) throws ExpansionAllowanceException, MarkerLessExpansionAdded;

    /**
     * Brief Adds an expansion to the expandable (as last expansion)
     * @param expansion The expansion being added
     * @throws MarkerLessExpansionAdded Thrown if the adding expansion has an empty list of markers
     */
    public void addExpansion(U expansion) throws ExpansionAllowanceException, MarkerLessExpansionAdded {
        addExpansion(expansion, size());
    }

    /**
     * Brief Adds an expansion to the expandable (as first expansion)
     * @param expansion The expansion being added
     * @throws MarkerLessExpansionAdded Thrown if the adding expansion has an empty list of markers
     */
    public void addExpansionFirst(U expansion) throws ExpansionAllowanceException, MarkerLessExpansionAdded {
        addExpansion(expansion, 0);
    }

    /**
     * Brief Removes an expansion from the expandable at the specified position
     * @param index the position
     */
    public abstract void removeExpansion(Integer index) throws ExpansionAllowanceException;

    /**
     * Brief overwrites an expansion of the expandable (in the position provided by index)
     * @param expansion The expansion being added instead
     * @param index the position
     * @throws MarkerLessExpansionAdded Thrown if the adding expansion has an empty list of markers
     */
    public void overwriteExpansion(U expansion, Integer index) throws ExpansionAllowanceException, MarkerLessExpansionAdded {
        removeExpansion(index);
        addExpansion(expansion, index);
    }

    /**
     * Brief returns all the stored expansions
     * @return the stored expansions
     */
    public abstract List<U> getExpansions();

    /**
     * Brief Returns the number of Expansions currently stored within
     * @return Integer the size of the Expandable
     */
    public abstract Integer size();

    /**
     * Brief Merges the expandable with another supported expandable. Merging them means that the expandable will
     * add to himself all the merging expandable's expansions
     * @param expandable The expandable being merged
     */
    public abstract void merge(V expandable) throws ExpansionAllowanceException;

    /**
     * Brief Indicates if the expandable has no expansions
     * @return Boolean true if the expandable has no expansions, false otherwise
     */
    public Boolean isEmpty() {
        return size().equals(0);
    }

    /**
     * Brief Empties the expandable, deleting all its expansions
     */
    public abstract void empty() throws ExpansionAllowanceException;

    /**
     * Brief Removes the undesired expansions
     * @param undesired Filter used to indicate undesired expansions
     */
    public abstract void removeExpansionsByFilter(Predicate<S> undesired) throws ExpansionAllowanceException;

    /**
     * Brief Remove expansions from a list of expansions applying a filter
     * @param expansions List of expansions to be filtered
     * @param undesired Filter used to indicate undesired expansions
     */
    protected void removeExpansionsByFilter(List<U> expansions, Predicate<S> undesired) throws ExpansionAllowanceException {
        requiresExpandable();
        expansions.forEach((expansion) -> expansion.getMarkers().removeIf(undesired));
        expansions.removeIf((expansion) -> expansion.getMarkers().isEmpty());
    }

    /**
     * Brief Applies all the expansions previously added to the expandable. It only applies filtered expansions.
     * @param expansions The expansions being applied
     * @param consumer The consumer that must accept each expansion as input
     */
    void applyExpansions(List<U> expansions, Consumer<U> consumer) throws ApplicationAllowanceException {
        requiresApplicable();
        expansions.stream().filter(this::filterExpansion).forEach(consumer);
    }

    /**
     * Brief Filters the expansion using the filter
     * @param expansion The expansion being filtered
     * @return Boolean true if the expansion meets any match with respect to the filter, false otherwise
     */
    private Boolean filterExpansion(Expansion<T,S> expansion) {
        return expansion.getMarkers().stream().anyMatch(filter);
    }

    /**
     * Brief Filter setter
     * @param filter Filter being set
     */
    public void setFilter(Predicate<S> filter) {
        this.filter = filter;
    }

    /**
     * Brief Filter getter
     * @return Predicate the filter
     */
    public Predicate<S> getFilter() {
        return filter;
    }

    /**
     * Brief Resets the filter to default state (all markers are accepted)
     */
    public void resetFilter() {
        filter = (marker) -> true;
    }
}
