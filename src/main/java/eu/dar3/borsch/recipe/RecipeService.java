package eu.dar3.borsch.recipe;

import eu.dar3.borsch.tag.Tag;
import eu.dar3.borsch.tag.TagDto;
import eu.dar3.borsch.tag.TagMapper;
import eu.dar3.borsch.tag.TagService;
import eu.dar3.borsch.user.User;
import eu.dar3.borsch.user.UserService;
import eu.dar3.borsch.utils.Util;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public Page<RecipeDto> findAllByRecipeOwnerFamily(Pageable pageable, String searchText) {
        User currentUser = userService.getCurrentUser();
        return recipeRepository.findRecipeList(currentUser, currentUser.getFriendGroup(), searchText, pageable)
                .map(this::convertToObjectDto);
    }

    private RecipeDto convertToObjectDto(Recipe note) {
        return recipeMapper.mapEntityToDto(note);
    }

    public RecipeDto add(RecipeDto noteDto) {
        noteDto.setRecipeOwner(userService.getCurrentUser());
        Recipe note = recipeMapper.mapDtoToEntity(noteDto);
        recipeValidator.validate(noteDto);
        Recipe savedNote = recipeRepository.save(note);
        return recipeMapper.mapEntityToDto(savedNote);
    }

    public void deleteById(UUID id) {
        recipeRepository.deleteById(id);
    }

    public void update(RecipeDto noteDto) {
        RecipeDto dto = getById(noteDto.getId());
        BeanUtils.copyProperties(noteDto, dto, Util.getNullPropertyNames(noteDto));
        recipeValidator.validate(noteDto);
        recipeRepository.save(recipeMapper.mapDtoToEntity(dto));
    }

    public RecipeDto getById(UUID id) throws EntityNotFoundException {
        Recipe note = recipeRepository.getReferenceById(id);
        return recipeMapper.mapEntityToDto(note);
    }

    public void copyLink(String url) {
        StringSelection stringSelection = new StringSelection(url);
        System.setProperty("java.awt.headless", "false");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    public RecipeDto addTag(UUID noteId, String tagTitle) {
        TagDto tagDto = tagService.addIfNotExists(tagTitle);
        RecipeDto noteDto = getById(noteId);
        java.util.List<Tag> tagList = noteDto.getTagList();
        String tagDtoTitle = tagDto.getTitle();
        if (tagList.stream().filter(tag -> tag.getTitle().equalsIgnoreCase(tagDtoTitle))
                .count() == TAG_COUNT_ZERO && !tagDtoTitle.isBlank()) {
            tagList.add(tagMapper.mapDtoToEntity(tagDto));
            update(noteDto);
        }
        return noteDto;
    }

    public RecipeDto deleteTag(UUID noteId, UUID tagID) {
        RecipeDto noteDto = getById(noteId);
        java.util.List<TagDto> tagList = tagMapper.mapEntityToDto(noteDto.getTagList());
        List<TagDto> newTagList = tagList.stream().filter(tag -> !tagID.equals(tag.getId()))
                .collect(Collectors.toList());
        noteDto.setTagList(tagMapper.mapDtoToEntity(newTagList));
        update(noteDto);
        return noteDto;
    }

    public String getSharedLink(UUID noteId, UriComponentsBuilder uriComponentsBuilder) {
        return uriComponentsBuilder.replacePath(null).replaceQuery(null).build().toString()
                + "/note/share/" + noteId;
    }
}
