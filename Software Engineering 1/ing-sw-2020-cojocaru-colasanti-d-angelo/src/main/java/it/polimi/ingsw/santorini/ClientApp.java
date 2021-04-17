package it.polimi.ingsw.santorini;

import it.polimi.ingsw.MainArgsHandler;
import it.polimi.ingsw.santorini.view.View;
import it.polimi.ingsw.santorini.view.ViewType;
import it.polimi.ingsw.santorini.view.network.ClientNetworkHandler;

public class ClientApp {
    public static void main( String[] args )
    {
        String ip = MainArgsHandler.getCorrectIp(args);
        int port = MainArgsHandler.getCorrectPort(args);
        ViewType viewType = MainArgsHandler.getCorrectViewType(args);

        //connection to server
        new ClientNetworkHandler(ip, port, View.getView(viewType));
    }


}

