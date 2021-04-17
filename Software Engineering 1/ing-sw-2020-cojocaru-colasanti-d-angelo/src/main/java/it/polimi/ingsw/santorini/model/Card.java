package it.polimi.ingsw.santorini.model;

import it.polimi.ingsw.santorini.model.gameoperations.GameOperation;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Brief Class representing a god card, having a unique power
 */
public class Card {
    private String name;
    private Integer id;
    private String title;
    private String description;
    private CardTrigger trigger;
    private Boolean advanced;
    private List<GameOperation<?, ?>> power;
    private File image;

    public Card(){

    }

    public Card(Integer id){
        this.id = id;
    }

    /**
     * Brief Assigns, if exixts, the intValueOf in the map passed as a parameter corresponding to any of its own attribute
     * @param cardContent is a map containing the card's values
     */
    public Card(HashMap<String, String> cardContent){
        super();
        Set<String> keys = cardContent.keySet();
        for(String key : keys){
            String lowerCaseKey = key.toLowerCase();
            String value = cardContent.get(key);
            if(lowerCaseKey.contains("id"))
                id = Integer.valueOf(value);
            else if(lowerCaseKey.contains("name"))
                name = value;
            else if(lowerCaseKey.contains("description"))
                description = value;
            else if(lowerCaseKey.contains("title"))
                title = value;
            else if(lowerCaseKey.contains("trigger"))
                trigger = CardTrigger.valueOf(value);
            else if(lowerCaseKey.contains("advanced"))
                advanced = Boolean.parseBoolean(value);
            else if(lowerCaseKey.contains("image"))
                image = new File(value);
        }
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public CardTrigger getTrigger() {
        return trigger;
    }

    public Boolean isAvanced() { return advanced; }

    public void setPower(List<GameOperation<?, ?>> power) {
        this.power = power;
    }

    public List<GameOperation<?, ?>> getPower() {
        return power;
    }

    public File getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(name, card.name) &&
                Objects.equals(id, card.id) &&
                Objects.equals(title, card.title) &&
                Objects.equals(description, card.description) &&
                trigger == card.trigger &&
                Objects.equals(advanced, card.advanced) &&
                Objects.equals(power, card.power) &&
                Objects.equals(image, card.image);
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", trigger=" + trigger +
                ", advanced=" + advanced +
                ", game operations= " + power +
                ", image=" + image +
                '}';
    }
}
