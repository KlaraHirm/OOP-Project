package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

   @OneToMany(cascade = CascadeType.ALL)
   public List<CardList> cardLists;

   @SuppressWarnings("unused")
   private Board() {
       // for object mappers
   }

   public Board(String title){
       this.title = title;
       this.cardLists = new ArrayList<CardList>();
   }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;

        if (!(o instanceof Board)) return false;

        Board board = (Board) o;

        return new EqualsBuilder().append(id, board.id).append(title, board.title).append(cardLists, board.cardLists).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(id).append(title).append(cardLists).toHashCode();
    }
}
