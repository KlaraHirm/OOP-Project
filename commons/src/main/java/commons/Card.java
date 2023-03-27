package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
     * Empty constructor for object mappers
     */
    @SuppressWarnings("unused")
    private Card() {
        // for object mappers
    }

    /**
     * Create a new Card instance.
     * @param title Title of the card
     */
    public Card(String title){
        this.title = title;
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

        return new EqualsBuilder().append(id, card.id).append(title, card.title).isEquals();
    }

    /**
     * Generates a hash code for this Card instance.
     * @return hash code of this Card instance
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(id).append(title).toHashCode();
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
                .toString();
    }
}
