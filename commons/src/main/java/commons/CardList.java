package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CardList
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public String title;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("place")
    public List<Card> cards;

    @SuppressWarnings("unused")
    private CardList() {
        // for object mappers
    }

    public CardList(String title)
    {
        this.title = title;
        this.cards = new ArrayList<Card>();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;

        if (!(o instanceof CardList)) return false;

        CardList cardList = (CardList) o;

        return new EqualsBuilder().append(id, cardList.id).append(title, cardList.title).append(cards, cardList.cards).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(id).append(title).append(cards).toHashCode();
    }

}
