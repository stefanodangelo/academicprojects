package it.polimi.ingsw;

import it.polimi.ingsw.santorini.view.ViewType;

/**
 * Brief Handles the main method's arguments
 */
public class MainArgsHandler {
    private static final String ip = "127.0.0.1";
    private static final int port = 9090;
    private static ViewType viewType = ViewType.GUI;
    private static final String ipCommand = "-ip";
    private static final String portCommand = "-p";
    private static final String viewCommand = "-v";

    /**
     * Brief Checks if it was provided an IP address among the main's arguments
     * @param args are the main's arguments
     * @return the chosen IP address or the default one if there wasn't any IP choice among the parameters
     */
    public static String getCorrectIp(String[] args) {
        String ipPattern = "(.*).(.*).(.*).(.*)";

        for(int i = 0; i < args.length; i++){
            if(args[i] != null && args[i].equals(ipCommand)){
                try {
                    return checkIPFormatting(args[++i], ipPattern);
                } catch(IndexOutOfBoundsException ignored){
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * Brief Checks if the given ip respects the IPv4 formatting
     * @param arg is the argument from command line
     * @param ipPattern is the IPv4 pattern
     * @return arg if the ip was well formatted, otherwise returns the default ip
     */
    private static String checkIPFormatting(String arg, String ipPattern) {
        if (arg != null && arg.matches(ipPattern)) {
            String[] parts = arg.split("\\.");

            for (int j = 0; j < 4; j++) {
                try {
                    Integer.parseInt(parts[j]);
                } catch (NumberFormatException e) {
                    return ip;
                }
            }
            return arg;
        }
        return ip;
    }

    /**
     * Brief Checks if it was provided a port among the main's arguments
     * @param args are the main's arguments
     * @return the chosen port or the default one if there wasn't any port choice among the parameters
     */
    public static int getCorrectPort(String[] args) {
        int newPort = port;

        for(int i = 0; i < args.length; i++){
            if(args[i] != null && args[i].equals(portCommand)){
                //noinspection finally
                try {
                    newPort = Integer.parseInt(args[++i]);
                } catch (NumberFormatException | IndexOutOfBoundsException ignored) {
                } finally {
                    //noinspection ContinueOrBreakFromFinallyBlock
                    break;
                }
            }
        }
        return newPort;
    }

    /**
     * Brief Checks if it was provided a type of view among the main's arguments
     * @param args are the main's arguments
     * @return the chosen type of view or the default one if there wasn't any view choice among the parameters
     */
    public static ViewType getCorrectViewType(String[] args){
        for(int i = 0; i < args.length; i++){
            if(args[i] != null && args[i].equals(viewCommand)){
                //noinspection finally
                try{
                    viewType = ViewType.valueOf(args[++i].toUpperCase());
                } catch (IllegalArgumentException | IndexOutOfBoundsException ignored){
                } finally {
                    //noinspection ContinueOrBreakFromFinallyBlock
                    break;
                }
            }
        }
        return viewType;
    }

    public static ViewType getChosenViewType(){
        return viewType;
    }
}
