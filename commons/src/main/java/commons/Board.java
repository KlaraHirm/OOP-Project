package commons;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Board
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

   @ElementCollection
   public List<CardList> cardLists = new ArrayList<CardList>();
}
