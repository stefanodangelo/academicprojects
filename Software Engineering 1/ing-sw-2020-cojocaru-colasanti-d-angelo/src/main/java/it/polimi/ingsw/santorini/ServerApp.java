package it.polimi.ingsw.santorini;

import it.polimi.ingsw.MainArgsHandler;
import it.polimi.ingsw.santorini.controller.server.ServerNetworkHandler;


public class ServerApp {
    public static void main( String[] args )
    {
        int port = MainArgsHandler.getCorrectPort(args);
        ServerNetworkHandler server = ServerNetworkHandler.getInstance();
        server.startServer(port);
    }

}

