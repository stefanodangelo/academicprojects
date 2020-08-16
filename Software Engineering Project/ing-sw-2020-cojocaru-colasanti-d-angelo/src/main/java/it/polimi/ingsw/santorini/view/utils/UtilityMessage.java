package it.polimi.ingsw.santorini.view.utils;

/**
 * Brief Class containing the rules of the Santorini game
 */
public class UtilityMessage {
    public static final String textualRules = "\nMove your selected Worker into one of the unoccupied (not containing a Worker or Dome) neighboring spaces.\n"
                                            + "A Worker may not move up more than one level.\n"
                                            + "If one of your Workers moves up on top of level 3 during your turn, you instantly win!\n"
                                            + "You must always perform a move then build on your turn. If you are unable to, you lose.\n";
    public static final String textualTitle = "███████╗ █████╗ ███╗   ██╗████████╗ ██████╗ ██████╗ ██╗███╗   ██╗██╗" + "\n" +
                                              "██╔════╝██╔══██╗████╗  ██║╚══██╔══╝██╔═══██╗██╔══██╗██║████╗  ██║██║" + "\n" +
                                              "███████╗███████║██╔██╗ ██║   ██║   ██║   ██║██████╔╝██║██╔██╗ ██║██║" + "\n" +
                                              "╚════██║██╔══██║██║╚██╗██║   ██║   ██║   ██║██╔══██╗██║██║╚██╗██║██║" + "\n" +
                                              "███████║██║  ██║██║ ╚████║   ██║   ╚██████╔╝██║  ██║██║██║ ╚████║██║" + "\n" +
                                              "╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝";
    public static final String credits = "Developed by Cristian Cojocaru, Luca Colasanti and Stefano D'Angelo";
    public static final String helpCommand = "help";
    public static final String cardsEffectCommand = "card";
    public static final String undoCommand = "undo";
    public static final String interruptionCommand = "interrupt";
}
