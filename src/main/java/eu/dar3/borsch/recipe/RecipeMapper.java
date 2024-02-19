package eu.dar3.borsch.recipe;

import eu.dar3.borsch.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Objects.isNull;

@Component
public class RecipeMapper implements Mapper<Recipe, RecipeDto> {
    @Override
    public RecipeDto mapEntityToDto(Recipe source) throws RuntimeException {
        if (isNull(source)) {
            return null;
        }
        RecipeDto target = new RecipeDto();
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setNote(source.getNote());
        target.setRecipeOwner(source.getRecipeOwner());
        target.setRecipeAccessType(source.getRecipeAccessType());
        target.setCreatedDate(source.getCreatedDate());
        target.setUpdatedDate(source.getUpdatedDate());
        target.setTagList(source.getTagList());
        return target;
    }

    @Override
    public Recipe mapDtoToEntity(RecipeDto source) throws RuntimeException {
        if (isNull(source)) {
            return null;
        }
        Recipe target = new Recipe();
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setNote(source.getNote());
        target.setRecipeOwner(source.getRecipeOwner());
        target.setRecipeAccessType(source.getRecipeAccessType());
        target.setCreatedDate(source.getCreatedDate());
        target.setUpdatedDate(source.getUpdatedDate());
        target.setTagList(source.getTagList());
        return target;
    }

    @Override
    public List<RecipeDto> mapEntityToDto(List<Recipe> source) throws RuntimeException {
        return Mapper.super.mapEntityToDto(source);
    }

    @Override
    public List<Recipe> mapDtoToEntity(List<RecipeDto> source) throws RuntimeException {
        return Mapper.super.mapDtoToEntity(source);
    }
}
