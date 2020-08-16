package it.polimi.ingsw.santorini.model.gameoperations.custom;

import it.polimi.ingsw.santorini.model.gameoperations.GameOperation;

import java.util.function.Function;

/**
 * Brief A customizable GameOperation wrapper
 * @param <T> The type of the customizable GameOperation
 * @see GameOperation
 */
public abstract class CustomGameOperation<T extends GameOperation<?, ?>> {

    /**
     * Brief an ordering id variable
     */
    private Integer index;

    /**
     * Brief customization parameters
     */
    protected GameOperationParameters parameters;

    /**
     * Brief a name for the custom operation
     */
    protected String operationName;

    /**
     * Brief The Function the applies the customization parameters to a GameOperation
     */
    protected Function<GameOperationParameters, T> customizer;

    /**
     * Brief Applies the customization function
     * @return the customized GameOperation
     */
    public T customize() {
        return customizer.apply(parameters);
    }

    /**
     * Brief getter for parameters
     * @return parameters
     */
    public GameOperationParameters getParameters() {
        return parameters;
    }

    /**
     * Brief setter for parameters
     * @param parameters parameters
     */
    public void setParameters(GameOperationParameters parameters) {
        this.parameters = parameters;
    }

    /**
     * Brief getter for customizer
     * @return customizer
     */
    public Function<GameOperationParameters, T> getCustomizer() {
        return customizer;
    }

    /**
     * Brief setter for customizer
     * @param customizer customizer
     */
    public void setCustomizer(Function<GameOperationParameters, T> customizer) {
        this.customizer = customizer;
    }

    /**
     * Brief getter for operationName
     * @return operationName
     */
    public String getOperationName() {
        return operationName;
    }

    /**
     * Brief setter for operationName
     * @param operationName operationName
     */
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    /**
     * Brief getter for index
     * @return index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * Brief setter for index
     * @param index index
     */
    public void setIndex(Integer index) {
        this.index = index;
    }
}
