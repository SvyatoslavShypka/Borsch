package eu.dar3.borsch.recipe;

import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.beans.BeanUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.Data;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.awt.Toolkit;
import java.util.stream.Collectors;
import java.util.List;
import java.util.UUID;


import eu.dar3.borsch.user.UserService;
import eu.dar3.borsch.tag.TagService;
import eu.dar3.borsch.tag.TagMapper;
import eu.dar3.borsch.utils.Util;
import eu.dar3.borsch.tag.TagDto;
import eu.dar3.borsch.user.User;
import eu.dar3.borsch.tag.Tag;

@Data
@Service
@RequiredArgsConstructor
public class RecipeService {
    private static final int TAG_COUNT_ZERO = 0;

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final UserService userService;
    private final TagService tagService;
    private final TagMapper tagMapper;
    private final RecipeValidator recipeValidator;


    public Page<RecipeDto> findAllByRecipeOwnerFriendgroup(Pageable pageable, String searchText) {
        User currentUser = userService.getCurrentUser();
        return recipeRepository.findRecipeList(currentUser, currentUser.getFriendgroup(), searchText, pageable)
                .map(this::convertToObjectDto);
    }

    private RecipeDto convertToObjectDto(Recipe recipe) {
        return recipeMapper.mapEntityToDto(recipe);
    }

    public RecipeDto add(RecipeDto recipeDto) {
        recipeDto.setRecipeOwner(userService.getCurrentUser());
        Recipe recipe = recipeMapper.mapDtoToEntity(recipeDto);
        recipeValidator.validate(recipeDto);
        Recipe savedRecipe = recipeRepository.save(recipe);
        return recipeMapper.mapEntityToDto(savedRecipe);
    }

    public void deleteById(UUID id) {
        recipeRepository.deleteById(id);
    }

    public void update(RecipeDto recipeDto) {
        RecipeDto dto = getById(recipeDto.getId());
        BeanUtils.copyProperties(recipeDto,
                dto, Util.getNullPropertyNames(recipeDto));
        recipeValidator.validate(recipeDto);
        recipeRepository.save(recipeMapper.mapDtoToEntity(dto));
    }

    public RecipeDto getById(UUID id) throws EntityNotFoundException {
        Recipe recipe = recipeRepository.getReferenceById(id);
        return recipeMapper.mapEntityToDto(recipe);
    }

    public void copyLink(String url) {
        StringSelection stringSelection = new StringSelection(url);
        System.setProperty("java.awt.headless", "false");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    public RecipeDto addTag(UUID recipeId, String tagTitle) {
        TagDto tagDto = tagService.addIfNotExists(tagTitle);
        RecipeDto recipeDto = getById(recipeId);
        List<Tag> tagList = recipeDto.getTagList();
        String tagDtoTitle = tagDto.getTitle();
        if (tagList.stream().filter(tag -> tag.getTitle().equalsIgnoreCase(tagDtoTitle))
                .count() == TAG_COUNT_ZERO && !tagDtoTitle.isBlank()) {
            tagList.add(tagMapper.mapDtoToEntity(tagDto));
            update(recipeDto);
        }
        return recipeDto;
    }

    public RecipeDto deleteTag(UUID recipeId, UUID tagID) {
        RecipeDto recipeDto = getById(recipeId);
        List<TagDto> tagList = tagMapper.mapEntityToDto(recipeDto.getTagList());
        List<TagDto> newTagList = tagList.stream().filter(tag -> !tagID.equals(tag.getId()))
                .collect(Collectors.toList());
        recipeDto.setTagList(tagMapper.mapDtoToEntity(newTagList));
        update(recipeDto);
        return recipeDto;
    }

    public String getSharedLink(UUID recipeId, UriComponentsBuilder uriComponentsBuilder) {
        return uriComponentsBuilder.replacePath(null).replaceQuery(null).build().toString()
                + "/recipe/share/" + recipeId;
    }
}
