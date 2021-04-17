package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import java.util.Arrays;
import java.util.List;

/**
 * Brief Specific supported Expansion for ExpListFunction
 * @param <T> The type of the ListFunction input
 * @param <E> The type of the ListFunction result's list element
 * @param <S> The type of the markers that can be coupled with the expansion
 * @see Expansion
 * @see ExpListFunction
 */
public class ListFunctionExpansion<T, E, S> extends FunctionExpansion<T, List<E>, S> {

    /**
     * Brief Constructor that sets the expansion ListFunction, the expansion type and the list of markers coupled with the
     * expansion
     * @param function The ListFunction involved
     * @param type The expansion type involved
     * @param markers The markers involved
     */
    public ListFunctionExpansion(ListFunction<T, E> function, ExpansionType type, List<S> markers) {
        super(function, type, markers);
    }

    /**
     * Brief Constructor that sets the expansion ListFunction, the expansion type and the list of markers coupled with the
     * expansion
     * @param function The ListFunction involved
     * @param type The expansion type involved
     * @param markers The markers involved as varargs
     */
    @SafeVarargs
    public ListFunctionExpansion(ListFunction<T, E> function, ExpansionType type, S... markers) {
        this(function, type, Arrays.asList(markers));
    }

    /**
     * Brief Constructor that sets the expansion's ListFunction and the expansion type
     * @param function The ListFunction involved
     * @param type The expansion type involved
     */
    public ListFunctionExpansion(ListFunction<T, E> function, ExpansionType type) {
        super(function, type);
    }

    /**
     * Brief Constructor that sets the expansion's ListFunction, the expansion type as Boolean
     * and the expansion's coupled markers list
     * @param function The ListFunction involved
     * @param expansive The expansion type as a Boolean, it must be true to request expansive, false for restrictive
     * @param markers The markers list involved
     */
    public ListFunctionExpansion(ListFunction<T, E> function, Boolean expansive, List<S> markers) {
        super(function, expansive, markers);
    }

    /**
     * Brief Constructor that sets the expansion's ListFunction, the expansion type as Boolean
     * and the expansion's coupled markers list as varargs
     * @param function The ListFunction involved
     * @param expansive The expansion type as a Boolean, it must be true to request expansive, false for restrictive
     * @param markers The markers list involved as varargs
     */
    @SafeVarargs
    public ListFunctionExpansion(ListFunction<T, E> function, Boolean expansive, S... markers) {
        this(function, expansive, Arrays.asList(markers));
    }

    /**
     * Brief Constructor that sets the expansion's ListFunction and the expansion type as Boolean
     * @param function The ListFunction involved
     * @param expansive The expansion type involved as Boolean, it must be true to request expansive, false for restrictive
     */
    public ListFunctionExpansion(ListFunction<T, E> function, Boolean expansive) {
        super(function, expansive);
    }
}
