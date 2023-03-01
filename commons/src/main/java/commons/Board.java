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

    public String title;

   @OneToMany
   public List<CardList> cardLists;

   @SuppressWarnings("unused")
   private Board() {
       // for object mappers
   }

   public Board(String title){
       this.title = title;
       this.cardLists = new ArrayList<CardList>();
   }

}
