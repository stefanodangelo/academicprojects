package it.polimi.ingsw.santorini.communication;

import java.io.Serializable;

/**
 * Brief Class representing an immutable god card, used for the communication between client and server
 */
public class ImmutableCard implements Serializable {
    private final String name;
    private final Integer id;
    private final String title;
    private final String description;

    public ImmutableCard(String name, Integer id, String title, String description) {
        this.name = name;
        this.id = id;
        this.title = title;
        this.description = description;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableCard that = (ImmutableCard) o;
        return name.equals(that.name) &&
                id.equals(that.id) &&
                title.equals(that.title) &&
                description.equals(that.description);
    }

    @Override
    public String toString() {
        return "ImmutableCard{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
