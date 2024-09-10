package eu.dar3.borsch.recipe;

import eu.dar3.borsch.tag.Tag;
import eu.dar3.borsch.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinTable;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToMany;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import static eu.dar3.borsch.utils.Constants.RECIPE_NOTE_LENGTH;
import static eu.dar3.borsch.utils.Constants.RECIPE_TITLE_MAX_LENGTH;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recipe", schema = "recipes")
public class Recipe {
    @Id
    @Column(name = "recipe_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "title", length = RECIPE_TITLE_MAX_LENGTH, nullable = false)
    private String title;
    @Column(name = "note", length = RECIPE_NOTE_LENGTH, nullable = false)
    private String note;
    @ManyToOne
    @JoinColumn(name = "recipe_owner", nullable = false)
    private User recipeOwner;
    @Enumerated(EnumType.STRING)
    @Column(name = "recipe_access_type", nullable = false)
    private RecipeAccessType recipeAccessType;
    @Column(name = "created_date")
    @CreationTimestamp
    private Instant createdDate;
    @Column(name = "updated_date")
    @UpdateTimestamp
    private Instant updatedDate;
    @JoinTable(
            name = "recipe_tag",
            schema = "recipes",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @ManyToMany
    private List<Tag> tagList;
}
