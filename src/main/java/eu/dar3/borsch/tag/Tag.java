package eu.dar3.borsch.tag;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import eu.dar3.borsch.recipe.Recipe;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tag", schema = "recipes")
public class Tag {
    private static final int TAG_TITLE_MAX_LENGTH = 500;

    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "title", length = TAG_TITLE_MAX_LENGTH, nullable = false, unique = true)
    private String title;
    @Column(name = "created_date")
    @CreationTimestamp
    private Instant createdDate;
    @Column(name = "updated_date")
    @UpdateTimestamp
    private Instant updatedDate;
    @JoinTable(
            name = "recipe_tag",
            schema = "recipes",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    @ManyToMany
    private List<Recipe> recipeLists;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        return id.equals(tag.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
