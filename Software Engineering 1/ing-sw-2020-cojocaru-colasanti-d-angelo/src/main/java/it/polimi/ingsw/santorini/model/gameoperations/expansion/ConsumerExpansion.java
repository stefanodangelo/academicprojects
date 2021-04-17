package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Brief Specific supported Expansion for ExpConsumer
 * @param <T> The Consumer's input type
 * @param <S> The type of the supported expansions' marker
 * @see Expansion
 * @see ExpConsumer
 */
public class ConsumerExpansion<T, S> extends Expansion<Consumer<T>, S> {

    /**
     * Brief Constructor that sets the consumer and the markers list coupled with the expansion
     * @param consumer The consumer involved
     * @param markers The list of markers involved
     */
    public ConsumerExpansion(Consumer<T> consumer, List<S> markers) {
        super(consumer, markers);
    }

    /**
     * Brief Constructor that sets the consumer and the markers list as varargs coupled with the expansion
     * @param consumer The consumer involved
     * @param markers The list of markers involved as varargs
     */
    @SafeVarargs
    public ConsumerExpansion(Consumer<T> consumer, S... markers) {
        this(consumer, Arrays.asList(markers));
    }

    /**
     * Brief Constructor that sets only the consumer coupled with expansion
     * @param consumer The consumer involved
     */
    public ConsumerExpansion(Consumer<T> consumer) {
        super(consumer);
    }
}
