package it.polimi.ingsw.santorini.view.delegates;

/**
 * Brief Delegate communicating between the View and the MessageAnalyzer
 */
public interface ViewDelegate {
    void onRunRequested();
    void onIPUpdated(String ip);
    void onPortUpdated(Integer port);
    void sendMessage(Object message);
}
