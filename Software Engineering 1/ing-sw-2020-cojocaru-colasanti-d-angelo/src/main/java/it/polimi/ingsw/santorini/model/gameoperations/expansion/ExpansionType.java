package it.polimi.ingsw.santorini.model.gameoperations.expansion;

/**
 * Brief Enum used to distinguish expansive expansions from restrictive expansions
 * @see Expansion
 */
public enum ExpansionType {
    EXPANSIVE,
    RESTRICTIVE;

    /**
     * Brief Supplier of ExpansionType through a boolean
     * @param expansive The boolean passed for the request
     * @return ExpansionType returns EXPANSIVE if the boolean parameter is true, RESTRICTIVE otherwise
     */
    public static ExpansionType byBoolean(boolean expansive) {
        return (expansive) ? EXPANSIVE : RESTRICTIVE;
    }
}
