package eu.dar3.borsch.recipe;

import eu.dar3.borsch.tag.Tag;
import eu.dar3.borsch.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private UUID id;
    private String title;
    private String note;
    private User recipeOwner;
    private RecipeAccessType recipeAccessType;
    private Instant createdDate;
    private Instant updatedDate;
    private List<Tag> tagList;

}
