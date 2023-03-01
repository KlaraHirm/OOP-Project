package commons;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CardList
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    String title;

    @OneToMany
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
}
