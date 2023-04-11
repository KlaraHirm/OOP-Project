package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import javax.persistence.*;

@Entity
public class Tag {

    /**
     * ID of the Tag
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    /**
     * Title of Tag
     */
    public String title;

    /**
     * Empty constructor for object mappers
     */
    @SuppressWarnings("unused")
    private Tag() {
        // for object mappers
    }

    /**
     * Creates a new Tag
     * @param title - Title of Tag
     */
    public Tag(String title){
        this.title = title;
    }

    /**
     * Checks if this Tag instance is equivalent to another Tag instance.
     * @param o object to compare this Tag to
     * @return true if the objects are equivalent, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return new EqualsBuilder()
                .append(id, tag.id)
                .append(title, tag.title)
                .isEquals();
    }

    /**
     * Generates a hash code for this Tag instance.
     * @return hash code of this Tag instance
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(title)
                .toHashCode();
    }

    /**
     * Generates a string representation of this Tag instance.
     * @return string representation of this Tag instance
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .toString();
    }
}

