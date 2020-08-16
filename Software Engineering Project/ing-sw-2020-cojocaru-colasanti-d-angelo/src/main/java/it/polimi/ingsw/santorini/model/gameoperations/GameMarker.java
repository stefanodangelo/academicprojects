package it.polimi.ingsw.santorini.model.gameoperations;

import it.polimi.ingsw.santorini.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Brief The game expansions marker used for all expansions in the game. It is useful as specifies the owner and the
 * target Player of the expansion
 * @see it.polimi.ingsw.santorini.model.gameoperations.expansion.Expansion
 * @see it.polimi.ingsw.santorini.model.gameoperations.expansion.Expandable
 */
public class GameMarker {

    /**
     * Brief The expansion owner Player
     */
    private Player expansionOwner;

    /**
     * Brief The expansion target Player
     */
    private Player expansionTarget;

    /**
     * Brief Support boolean for other uses
     */
    private Boolean mark;

    /**
     * Brief Default constructor used for a defaultMarker() instance
     */
    public GameMarker() {}

    /**
     * Brief Default constructor used for a Marker using mark variable
     * @param mark is the mark
     */
    public GameMarker(Boolean mark) {
        this.mark = mark;
    }

    /**
     * Brief The constructor that sets both the expansion owner and target Player
     * @param expansionOwner The owner Player involved
     * @param expansionTarget The target Player involved
     */
    public GameMarker(Player expansionOwner, Player expansionTarget) {
        this.expansionOwner = expansionOwner;
        this.expansionTarget = expansionTarget;
    }

    /**
     * Brief The constructor that sets both the expansion owner and target Player as the uniquePlayer
     * @param uniquePlayer The owner and target Player involved
     */
    public GameMarker(Player uniquePlayer) {
        this(uniquePlayer, uniquePlayer);
    }

    /**
     * Brief Provides a default marker that has no defined owner nor target
     * @return GameMarker
     */
    public static GameMarker defaultMarker() {
        return new GameMarker();
    }

    /**
     * Brief Provides a list of markers that has in common a current Player as owner. One marker is
     * provided per enemy Player (set as target)
     * @param current current Player
     * @param enemies list of enemies Players
     * @return The list of produced markers
     */
    public static List<GameMarker> enemiesAsTarget(Player current, List<Player> enemies) {
        final List<GameMarker> markers = new ArrayList<>();
        enemies.forEach((enemy) -> markers.add(new GameMarker(current, enemy)));
        return markers;
    }

    /**
     * Brief Provides a marker that is owned and targets the current player
     * @param current current Player
     * @return GameMarker The produced marker
     */
    public static GameMarker currentPlayerAsTarget(Player current) {
        return new GameMarker(current);
    }

    /**
     * Brief Check whether a specific owner equals the marker's owner
     * @param owner The specific owner
     * @return Boolean true if the equality is confirmed
     */
    public Boolean ownerEquals(Player owner) {
        return (owner == null && expansionOwner == null) || (owner != null && owner.equals(expansionOwner));
    }

    /**
     * Brief Check whether a specific target equals the marker's target
     * @param target The specific target
     * @return Boolean true if the equality is confirmed
     */
    public Boolean targetEquals(Player target) {
        return (target == null && expansionTarget == null) || (target != null && target.equals(expansionTarget));
    }

    /**
     * Brief Check whether a specific owner equals the marker's owner and
     * a specific target equals the marker's target
     * @param owner The specific owner
     * @param target The specific target
     * @return Boolean true if the equality is confirmed
     */
    public Boolean equals(Player owner, Player target) {
        return ownerEquals(owner) && targetEquals(target);
    }

    /**
     * Brief Checks whether the marker is a default marker
     * @return Boolean true if ha both reference Players set to null
     */
    public Boolean equalsDefault() {
        return equals(null, null);
    }

    /**
     * Brief Getter for the owner
     * @return Player
     */
    public Player getOwner() {
        return expansionOwner;
    }

    /**
     * Brief Getter for the target
     * @return Player
     */
    public Player getTarget() {
        return expansionTarget;
    }

    /**
     * Brief Getter for the mark boolean
     * @return Boolean mark
     */
    public Boolean getMark() {
        return mark != null ? mark : false;
    }

    /**
     * Brief Setter for the mark boolean
     * @param mark Boolean mark
     */
    public void setMark(Boolean mark) {
        this.mark = mark;
    }

    /**
     * Brief provides a filter to allow only expansions marked by a default marker
     * @return Predicate of GamemMarker
     */
    public static Predicate<GameMarker> filterByDefaultMarker() {
        return GameMarker::equalsDefault;
    }

    /**
     * Brief provides a filter to allow only expansions marked by a true mark variable
     * @return Predicate of GameMarker
     */
    public static Predicate<GameMarker> filterByTrueMark() {
        return GameMarker::getMark;
    }

    /**
     * Brief provides a filter to allow only expansions that have a specified target
     * @param target The target Player
     * @return Predicate of GameMarker
     */
    public static Predicate<GameMarker> filterByTarget(Player target) {
        return (marker) -> marker.targetEquals(target);
    }

    /**
     * Brief provides a filter to allow only expansions that have a specified owner
     * @param owner The owner Player
     * @return Predicate of GameMarker
     */
    public static Predicate<GameMarker> filterByOwner(Player owner) {
        return (marker) -> marker.ownerEquals(owner);
    }

    /**
     * Brief provides a filter to allow only expansions that are made for the current player only
     * @param player The current Player
     * @return Predicate of GameMarker
     */
    public static Predicate<GameMarker> filterByCurrentPlayer(Player player) {
        return (marker) -> marker.equalsDefault() || marker.targetEquals(player);
    }
}
