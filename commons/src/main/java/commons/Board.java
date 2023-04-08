package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Board
{
    /**
     * ID of the board
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    /**
     * Title of Board
     */
    public String title;

    /**
     * List of CardLists on the board
     */
    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("place")
    public List<CardList> cardLists;

    /**
     * Empty constructor for object mappers
     */
   @SuppressWarnings("unused")
   private Board() {
       // for object mappers
   }

   /**
    * Create a new Board instance.
    * @param title Title of the board
    */
   public Board(String title){
       this.title = title;
       this.cardLists = new ArrayList<CardList>();
   }

    /**
     * Checks if this Board instance is equivalent to another Board instance.
     * @param o object to compare this board to
     * @return true if the objects are equivalent, false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;

        if (!(o instanceof Board)) return false;

        Board board = (Board) o;

        return new EqualsBuilder().append(id, board.id).append(title, board.title).append(cardLists, board.cardLists).isEquals();
    }

    /**
     * Generates a hash code for this Board instance.
     * @return Hash code for this board
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(id).append(title).append(cardLists).toHashCode();
    }

    /**
     * Create a readable string representation for this Board instance.
     * @return String representation for this board
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .append("cardLists", cardLists)
                .toString();
    }
}
