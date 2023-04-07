package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SubTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String title;

    public boolean done;

    public int place;

    /**
     * Empty constructor for object mappers
     */
    @SuppressWarnings("unused")
    private SubTask() {
        // for object mappers
    }

    /**
     * Create a new SubTask instance
     * @param title title of a subtask
     * @param done done status of a subtask
     * @param place place of a SubTask in a card
     */
    public SubTask(String title, boolean done, int place) {
        this.title = title;
        this.done = done;
        this.place = place;
    }

    /**
     * Create a new SubTask instance, which has done status as false and place as 0
     * @param title title of a subtask
     */
    public SubTask(String title) {
        this.title = title;
        this.done = false;
        this.place = 0;
    }

    /**
     * Checks if this SubTask instance is equivalent to another SubTask instance.
     * @param o object to compare this card to
     * @return true if the objects are equivalent, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof SubTask)) return false;

        SubTask subTask = (SubTask) o;

        return new EqualsBuilder()
                .append(id, subTask.id)
                .append(title, subTask.title)
                .append(done, subTask.done)
                .append(place, subTask.place)
                .isEquals();
    }

    /**
     * Generates a hash code for this SubTask instance.
     * @return hash code of this SubTask instance
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(title)
                .append(done)
                .append(place)
                .toHashCode();
    }

    /**
     * Generates a string representation of this SubTask instance.
     * @return string representation of this SubTask instance
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .append("done", done)
                .append("place", place)
                .toString();
    }
}
