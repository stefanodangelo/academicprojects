package it.polimi.ingsw.santorini.model.gameoperations.expansion;

public abstract class Helper {

    static Integer getExpansiveSize(Expandable<?,?,?,?> expandable) {
        return (int) expandable.getExpansions().stream()
                .filter(expansion -> expansion.getType().equals(ExpansionType.EXPANSIVE))
                .count();
    }

    static Integer getRestrictiveSize(Expandable<?,?,?,?> expandable) {
        return (int) expandable.getExpansions().stream()
                .filter(expansion -> expansion.getType().equals(ExpansionType.RESTRICTIVE))
                .count();
    }
}
