package commons;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Card
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String title;

    @SuppressWarnings("unused")
    private Card() {
        // for object mappers
    }

    public Card(String title){
        this.title = title;
    }
}
