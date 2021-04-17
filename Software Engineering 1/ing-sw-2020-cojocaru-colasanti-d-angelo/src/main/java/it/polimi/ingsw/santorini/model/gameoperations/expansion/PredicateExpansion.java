package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Brief Specific supported Expansion for ExpPredicate
 * @param <T> The type of the Predicate input
 * @param <S> The type of the markers that can be coupled with the expansion
 * @see Expansion
 * @see ExpPredicate
 */
public class PredicateExpansion<T, S> extends Expansion<Predicate<T>, S> {

    /**
     * Brief Constructor that sets the expansion Predicate, the expansion type and the list of markers coupled with the
     * expansion
     * @param predicate The Predicate involved
     * @param type The expansion type involved
     * @param markers The markers involved
     */
    public PredicateExpansion(Predicate<T> predicate, ExpansionType type, List<S> markers) {
        super(predicate, type, markers);
    }

    /**
     * Brief Constructor that sets the expansion Predicate, the expansion type and the list of markers coupled with the
     * expansion
     * @param predicate The Predicate involved
     * @param type The expansion type involved
     * @param markers The markers involved as varargs
     */
    @SafeVarargs
    public PredicateExpansion(Predicate<T> predicate, ExpansionType type, S... markers) {
        this(predicate, type, Arrays.asList(markers));
    }

    /**
     * Brief Constructor that sets the expansion's Predicate and the expansion type
     * @param predicate The Predicate involved
     * @param type The expansion type involved
     */
    public PredicateExpansion(Predicate<T> predicate, ExpansionType type) {
        super(predicate, type);
    }

    /**
     * Brief Constructor that sets the expansion's Predicate, the expansion type as Boolean
     * and the expansion's coupled markers list
     * @param predicate The Predicate involved
     * @param expansive The expansion type as a Boolean, it must be true to request expansive, false for restrictive
     * @param markers The markers list involved
     */
    public PredicateExpansion(Predicate<T> predicate, Boolean expansive, List<S> markers) {
        super(predicate, expansive, markers);
    }

    /**
     * Brief Constructor that sets the expansion's Predicate, the expansion type as Boolean
     * and the expansion's coupled markers list as varargs
     * @param predicate The Predicate involved
     * @param expansive The expansion type as a Boolean, it must be true to request expansive, false for restrictive
     * @param markers The markers list involved as varargs
     */
    @SafeVarargs
    public PredicateExpansion(Predicate<T> predicate, Boolean expansive, S... markers) {
        this(predicate, expansive, Arrays.asList(markers));
    }

    /**
     * Brief Constructor that sets the expansion's Predicate and the expansion type as Boolean
     * @param predicate The ListFunction involved
     * @param expansive The expansion type involved as Boolean, it must be true to request expansive, false for restrictive
     */
    public PredicateExpansion(Predicate<T> predicate, Boolean expansive) {
        super(predicate, expansive);
    }
}
