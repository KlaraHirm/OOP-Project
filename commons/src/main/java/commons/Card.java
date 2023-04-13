package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Card
{
    /**
     * ID of the card
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    /**
     * Title of the card
     */
    public String title;

    /**
     * Order of the card in the cardList
     */
    public int place;

    /**
     * Description of the card
     */
    public String description;

    /**
     * Whether the card is checked/done
     */
    public boolean done;

    /**
     * List of SUbTasks in the card
     */
    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("place")
    public List<Subtask> subtasks;

    /**
     * List of Tags in the Card
     */

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "TAG_CARDS",
            joinColumns = @JoinColumn(name = "card_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    public List<Tag> tags;


    /**
     * Empty constructor for object mappers
     */
    @SuppressWarnings("unused")
    private Card() {
        // for object mappers
    }

    /**
     * Create a new Card instance.
     * @param title Title of the card
     * @param place Place of the card (to determine order in the list)
     * @param description Description of the card
     * @param done If the card is checked/done
     *
     */
    public Card(String title, int place, String description, boolean done){
        this.title = title;
        this.place = place;
        this.description = description;
        this.done = done;
        this.subtasks = new ArrayList<Subtask>();
        this.tags = new ArrayList<Tag>();
    }

    /**
     * Create a new Card instance. Set default values for all other properties
     * @param title Title of the card
     */
    public Card(String title){
        this.title = title;
        this.place = 0;
        this.description = "";
        this.done = false;
        this.subtasks = new ArrayList<Subtask>();
    }

    /**
     * Checks if this Card instance is equivalent to another Card instance.
     * @param o object to compare this card to
     * @return true if the objects are equivalent, false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;

        if (!(o instanceof Card)) return false;

        Card card = (Card) o;

        return new EqualsBuilder()
                .append(id, card.id)
                .append(title, card.title)
                .append(description, card.description)
                .append(tags, card.tags)
                .append(done, card.done)
                .append(subtasks, card.subtasks)
                .isEquals();
    }

    /**
     * Generates a hash code for this Card instance.
     * @return hash code of this Card instance
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(title)
                .append(description)
                .append(done)
                .append(subtasks)
                .append(tags)
                .toHashCode();
    }

    /**
     * Generates a string representation of this Card instance.
     * @return string representation of this Card instance
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .append("place", place)
                .append("description", description)
                .append("tags", tags)
                .append("done", done)
                .append("subTasks", subtasks)
                .toString();
    }
}
