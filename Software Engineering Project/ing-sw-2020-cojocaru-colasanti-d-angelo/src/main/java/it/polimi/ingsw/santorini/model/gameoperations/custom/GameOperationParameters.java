package it.polimi.ingsw.santorini.model.gameoperations.custom;

/**
 * Brief Customization parameters for custom GameOperation wrapper
 * @see CustomGameOperation
 */
public class GameOperationParameters {

    /**
     * Brief tells if the operation must be optional
     */
    private Boolean isOptional;

    /**
     * Brief tells if the operation must have default expansions pre-loaded
     */
    private Boolean hasDefaultRules;

    /**
     * Brief tells if the operation must ask for worker selection
     */
    private Boolean requiresWorkerSelection;

    /**
     * Brief getter for isOptional
     * @return isOptional
     */
    public Boolean isOptional() {
        return isOptional;
    }

    /**
     * Brief setter for isOptional
     * @param optional optional
     */
    public void setOptional(Boolean optional) {
        isOptional = optional;
    }

    /**
     * Brief getter for hasDefaultRules
     * @return hasDefaultRules
     */
    public Boolean getHasDefaultRules() {
        return hasDefaultRules;
    }

    /**
     * Brief setter for hasDefaultRules
     * @param hasDefaultRules hasDefaultRules
     */
    public void setHasDefaultRules(Boolean hasDefaultRules) {
        this.hasDefaultRules = hasDefaultRules;
    }

    /**
     * Brief getter for requiresWorkerSelection
     * @return requiresWorkerSelection
     */
    public Boolean getRequiresWorkerSelection() {
        return requiresWorkerSelection;
    }

    /**
     * Brief setter for requiresWorkerSelection
     * @param requiresWorkerSelection requiresWorkerSelection
     */
    public void setRequiresWorkerSelection(Boolean requiresWorkerSelection) {
        this.requiresWorkerSelection = requiresWorkerSelection;
    }
}
