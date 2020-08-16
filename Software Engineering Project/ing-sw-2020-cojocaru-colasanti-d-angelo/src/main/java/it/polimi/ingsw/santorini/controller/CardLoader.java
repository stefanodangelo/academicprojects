package it.polimi.ingsw.santorini.controller;

import it.polimi.ingsw.santorini.controller.parser.Parser;
import it.polimi.ingsw.santorini.model.Card;
import it.polimi.ingsw.santorini.model.exceptions.NoCardWithSuchIdException;
import it.polimi.ingsw.santorini.model.gameoperations.GameOperation;
import it.polimi.ingsw.santorini.model.gameoperations.custom.CustomBuild;
import it.polimi.ingsw.santorini.model.gameoperations.custom.CustomMove;
import it.polimi.ingsw.santorini.model.gameoperations.factory.BuildFactory;
import it.polimi.ingsw.santorini.model.gameoperations.factory.MoveFactory;

import java.util.*;

/**
 * Brief Singleton responsible for the loading of all the gods' cards
 */
public class CardLoader {
    private static CardLoader instance;
    private static ArrayList<Card> cards = new ArrayList<>();
    private static final String cardsFilePath = "/GodList/godslist.xml";

    private CardLoader() {
    }

    /**
     * @return the only one instance of the created CardLoader object
     */
    public static CardLoader getInstance() {
        if(instance == null){
            instance = new CardLoader();
        }
        return instance;
    }

    /**
     * Brief Loads the cards from the file with the specified extension
     */
    public static void loadCards(){
        if(!cards.isEmpty()) cards.removeIf(card -> true);
        Parser parser = Objects.requireNonNull(Parser.getParser(cardsFilePath));

        HashMap<Integer, HashMap<String, String>> cardsMeta = parser.parseFileForMeta(cardsFilePath);
        HashMap<Integer, List<CustomMove>> godCustomMoves = parser.parseFileForEffects(cardsFilePath, "move", CustomMove::new);
        HashMap<Integer, List<CustomBuild>> godCustomBuilds = parser.parseFileForEffects(cardsFilePath, "build", CustomBuild::new);

        for(int i = 0; i < cardsMeta.size(); i++) {
            HashMap<String, String> mappedValues = cardsMeta.get(i);
            Card card = new Card(mappedValues);

            List<GameOperation<?,?>> power = new ArrayList<>();
            List<CustomMove> customMoves = godCustomMoves.get(i);
            List<CustomBuild> customBuilds = godCustomBuilds.get(i);

            for (int j = 0; j < customMoves.size() + customBuilds.size(); j++) {
                int finalJ = j;

                customMoves.stream().filter(cm -> cm.getIndex().equals(finalJ)).forEach(cm ->
                        power.add(MoveFactory.getMove(cm.getOperationName(), cm.getParameters())));

                customBuilds.stream().filter(cm -> cm.getIndex().equals(finalJ)).forEach(cm ->
                    power.add(BuildFactory.getBuild(cm.getOperationName(), cm.getParameters())));

            }
            card.setPower(power);
            cards.add(card);
        }
    }

    /**
     * @return a copy of the list of card opportunely casted
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Card> getCards() {
        return (ArrayList<Card>) cards.clone();
    }

    /**
     * @param id is the card's id
     * @return the card with the corresponding id or throws NoCardWithSuchIdException if no card was found
     */
    public static Card getCardById(Integer id){
        for (Card c : cards){
            if (id.equals(c.getId()))
                return c;
        }
        throw new NoCardWithSuchIdException();
    }

    /**
     * Brief Replaces all the cards with the selected ones
     * @param selectedCardsIds are the chosen cards' ids
     */
    public static void selectCards(List<Integer> selectedCardsIds){
        ArrayList<Card> cards = new ArrayList<>();
        for(Integer id : selectedCardsIds)
            cards.add(getCardById(id));
        CardLoader.cards = cards;
    }

    @Override
    public String toString() {
        StringBuilder cardsStringBuilder = new StringBuilder();

        for(int i = 0; i < cards.size(); i++){
            cardsStringBuilder.append("card at ")
                              .append(i)
                              .append("= ")
                              .append(cards.get(i))
                              .append("\n");
        }

        return "CardLoader{" +
                "cards= " +
                cardsStringBuilder +
                '}'
                ;
    }
}
