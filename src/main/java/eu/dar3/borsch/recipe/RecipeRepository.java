package eu.dar3.borsch.recipe;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import eu.dar3.borsch.friendgroup.Friendgroup;
import eu.dar3.borsch.user.User;

import java.util.UUID;

@Repository
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, UUID>,
        JpaRepository<Recipe, UUID> {
    @Query("SELECT distinct n FROM Recipe n LEFT JOIN n.tagList t INNER JOIN n.recipeOwner u WHERE (u = :recipeOwner OR u.friendgroup = :friendgroup) AND (UPPER(n.title) like UPPER(CONCAT('%',:searchText,'%')) OR UPPER(t.title) like UPPER(CONCAT('%',:searchText,'%'))) ORDER BY n.title")
    Page<Recipe> findRecipeList(@Param("recipeOwner") User recipeOwner, @Param("friendgroup") Friendgroup friendgroup,
                            @Param("searchText") String searchText, Pageable pageable);
}
