package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Brief Specific supported Expansion for ExpFunction
 * @param <T> The type of the Function input
 * @param <R> The type of the Function result
 * @param <S> The type of the supported expansions' marker
 * @see Expansion
 * @see ExpFunction
 */
public class FunctionExpansion<T, R, S> extends Expansion<Function<T, R>, S> {

    /**
     * Brief Constructor that sets the expansion function, the expansion type and the list of markers coupled with the
     * expansion
     * @param function The function involved
     * @param type The expansion type involved
     * @param markers The markers involved
     */
    public FunctionExpansion(Function<T, R> function, ExpansionType type, List<S> markers) {
        super(function, type, markers);
    }

    /**
     * Brief Constructor that sets the expansion function, the expansion type and the list of markers coupled with the
     * expansion
     * @param function The function involved
     * @param type The expansion type involved
     * @param markers The markers involved as varargs
     */
    @SafeVarargs
    public FunctionExpansion(Function<T, R> function, ExpansionType type, S... markers) {
        this(function, type, Arrays.asList(markers));
    }

    /**
     * Brief Constructor that sets the expansion's function and the expansion type
     * @param function The function involved
     * @param type The expansion type involved
     */
    public FunctionExpansion(Function<T, R> function, ExpansionType type) {
        super(function, type);
    }

    /**
     * Brief Constructor that sets the expansion's function, the expansion type as Boolean
     * and the expansion's coupled markers list
     * @param function The function involved
     * @param expansive The expansion type as a Boolean, it must be true to request expansive, false for restrictive
     * @param markers The markers list involved
     */
    public FunctionExpansion(Function<T, R> function, Boolean expansive, List<S> markers) {
        super(function, expansive, markers);
    }

    /**
     * Brief Constructor that sets the expansion's function, the expansion type as Boolean
     * and the expansion's coupled markers list as varargs
     * @param function The function involved
     * @param expansive The expansion type as a Boolean, it must be true to request expansive, false for restrictive
     * @param markers The markers list involved as varargs
     */
    @SafeVarargs
    public FunctionExpansion(Function<T, R> function, Boolean expansive, S... markers) {
        this(function, expansive, Arrays.asList(markers));
    }

    /**
     * Brief Constructor that sets the expansion's function and the expansion type as Boolean
     * @param function The function involved
     * @param expansive The expansion type involved as Boolean, it must be true to request expansive, false for restrictive
     */
    public FunctionExpansion(Function<T, R> function, Boolean expansive) {
        super(function, expansive);
    }
}
