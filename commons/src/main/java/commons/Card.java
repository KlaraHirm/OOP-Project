package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
public class Card
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String title;

    public int place;

    @SuppressWarnings("unused")
    private Card() {
        // for object mappers
    }

    public Card(String title){
        this.title = title;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;

        if (!(o instanceof Card)) return false;

        Card card = (Card) o;

        return new EqualsBuilder().append(id, card.id).append(title, card.title).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(id).append(title).toHashCode();
    }
}
