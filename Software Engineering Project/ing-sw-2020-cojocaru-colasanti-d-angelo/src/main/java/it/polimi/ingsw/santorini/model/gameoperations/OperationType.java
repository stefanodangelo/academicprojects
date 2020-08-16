package it.polimi.ingsw.santorini.model.gameoperations;

/**
 * Brief Enum for the type of operation being executed, that is MOVE or BUILD
 */
public enum OperationType {
    MOVE(Move.class, "Move"),
    BUILD(Build.class, "Build");

    private final Class<?> type;
    private final String name;

    OperationType(Class<?> type, String name){
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * @param operationClass is the class of the executing operation
     * @return the type of the operation being executed or null if there's not any class that matches the given one
     */
    public static OperationType getTypeByClass(Class<?> operationClass){
        for(OperationType op : OperationType.values())
            if(op.type.equals(operationClass))
                return op;
        return null;
    }
}
