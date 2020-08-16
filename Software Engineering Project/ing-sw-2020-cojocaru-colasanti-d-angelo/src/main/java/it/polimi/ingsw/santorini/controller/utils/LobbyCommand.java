package it.polimi.ingsw.santorini.controller.utils;

import it.polimi.ingsw.santorini.communication.QuitMessage;
import it.polimi.ingsw.santorini.communication.TextMessage;
import it.polimi.ingsw.santorini.controller.server.ServerNetworkHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Brief Class containing all the available commands when players are in the lobby
 */
public class LobbyCommand {
    private static final String newGameDescription = "new game";
    private static final String resizeDescription = "resize";
    /**
     * @return the available commands during the lobby phase
     */
    public static List<String> getLobbyCommands(){
        List<String> lobbyCommands = new ArrayList<>();
        lobbyCommands.add(TextMessage.getStartRequest());
        lobbyCommands.add(QuitMessage.getRequest());
        lobbyCommands.addAll(getLobbyResizeCommands());
        return lobbyCommands;
    }

    /**
     * @return the list of commands available during the lobby phase to resize the lobby's dimension
     */
    private static List<String> getLobbyResizeCommands(){
        List<String> commands = new ArrayList<>();
        for(int i = ServerNetworkHandler.getMinCapability(); i <= ServerNetworkHandler.getMaxCapability(); i++)
            if(i != ServerNetworkHandler.getActualCapability())
                commands.add(String.valueOf(i));
        return commands;
    }

    /**
     * @return the textual content helping the host to understand what each command does
     */
    public static List<String> getCommandsHelpingText(){
        List<String> list = new ArrayList<>();
        list.add(newGameDescription);
        list.add(QuitMessage.getCommandDescription());
        for(String ignored : getLobbyResizeCommands())
            list.add(resizeDescription);
        return list;
    }
}
