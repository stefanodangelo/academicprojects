package it.polimi.ingsw.santorini.model.gameoperations.result;

import it.polimi.ingsw.santorini.model.Position;
import it.polimi.ingsw.santorini.model.Worker;
import it.polimi.ingsw.santorini.model.gameoperations.rules.expandable.ExpandableRulesContainer;

/**
 * Brief Represents the result being returned after a GameOperation execution
 * @see it.polimi.ingsw.santorini.model.gameoperations.GameOperation
 */
public class GameOperationResult {

    /**
     * Brief The current active worker
     */
    private Worker activeWorker;

    /**
     * Brief The current active position
     */
    private Position activePosition;

    /**
     * Brief The win report, true if the current player won during the last operation, false otherwise
     */
    private Boolean winReport;

    /**
     * Brief The expansionRules that store all the new expansions to apply to the next following operations
     */
    private final ExpandableRulesContainer expansionRules = new ExpandableRulesContainer();

    /**
     * Brief Flag to indicate whether the following current player's operations must be aborted
     */
    private Boolean abortNextOperations = false;

    /**
     * Brief Flag to indicate whether the last executed operation has been skipped by the user (it was an optional operation)
     */
    private Boolean wasSkipped;

    /**
     * Brief Getter for the active position
     * @return Position The active position
     */
    public Position getActivePosition() {
        return activePosition;
    }

    /**
     * Brief Setter for the active position
     * @param activePosition Position The position being set
     */
    public void setActivePosition(Position activePosition) {
        this.activePosition = activePosition;
    }

    /**
     * Brief Getter for the win report
     * @return Boolean The win report
     */
    public Boolean getWinReport() {
        return winReport;
    }

    /**
     * Brief Getter for abortNextOperations
     * @return Boolean abortNextOperations
     */
    public Boolean getAbortNextOperations() {
        return abortNextOperations;
    }

    /**
     * Brief Setter for abortNextOperations
     * @param abortNextOperations Boolean abortNextOperations
     */
    public void setAbortNextOperations(Boolean abortNextOperations) {
        this.abortNextOperations = abortNextOperations;
    }

    /**
     * Brief Setter for winReport
     * @param result Boolean result
     */
    public void setWinReport(Boolean result) {
        winReport = result;
    }

    /**
     * Brief Getter for activeWorker
     * @return Worker activeWorker
     */
    public Worker getActiveWorker() {
        return activeWorker;
    }

    /**
     * Brief Setter for activeWorker
     * @param activeWorker Worker activeWorker
     */
    public void setActiveWorker(Worker activeWorker) {
        this.activeWorker = activeWorker;
    }

    /**
     * Brief Getter for expansionRules
     * @return ExpandableRulesContainer expansionRules
     */
    public ExpandableRulesContainer expansionRules() {
        return expansionRules;
    }

    /**
     * Brief Getter for wasSkipped
     * @return Boolean wasSkipped
     */
    public Boolean getWasSkipped() {
        return wasSkipped != null ? wasSkipped : false;
    }

    /**
     * Brief Setter for wasSkipped
     * @param wasSkipped Boolean wasSkipped
     */
    public void setWasSkipped(Boolean wasSkipped) {
        this.wasSkipped = wasSkipped;
    }
}
