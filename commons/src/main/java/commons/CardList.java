package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CardList
{
    /**
     * ID of the cardList
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    /**
     * Title of the cardList
     */
    public String title;

    /**
     * List of Cards in the cardList
     */
    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("place")
    public List<Card> cards;

    /**
     * Empty constructor for object mappers
     */
    @SuppressWarnings("unused")
    private CardList() {
        // for object mappers
    }

    /**
     * Create a new CardList instance.
     * @param title Title of the cardList
     */
    public CardList(String title)
    {
        this.title = title;
        this.cards = new ArrayList<Card>();
    }

    /**
     * Checks if this CardList instance is equivalent to another CardList instance.
     * @param o object to compare this cardList to
     * @return true if the objects are equivalent, false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;

        if (!(o instanceof CardList)) return false;

        CardList cardList = (CardList) o;

        return new EqualsBuilder().append(id, cardList.id).append(title, cardList.title).append(cards, cardList.cards).isEquals();
    }

    /**
     * Returns a hash code for this CardList instance.
     * @return a hash code for this CardList instance.
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(id).append(title).append(cards).toHashCode();
    }

    /**
     * Returns a string representation of this CardList instance.
     * @return a string representation of this CardList instance.
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .append("cards", cards)
                .toString();
    }

}
