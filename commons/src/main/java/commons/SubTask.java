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
     */
    public SubTask(String title, boolean done) {
        this.title = title;
        this.done = done;
    }

    /**
     * Create a new SubTask instance, which has done status as false
     * @param title title of a subtask
     */
    public SubTask(String title) {
        this.title = title;
        this.done = false;
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
                .toString();
    }
}
