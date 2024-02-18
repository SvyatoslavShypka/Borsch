package eu.dar3.borsch.tag;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@Data
@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public List<TagDto> listAll() {
        return tagMapper.mapEntityToDto(tagRepository.findAll());
    }

    private TagDto getByIdAndTitle(UUID id, String title) {
        Tag targetTag = tagRepository.getReferenceById(id);
        if (targetTag.getTitle().equalsIgnoreCase(title)) {
            return tagMapper.mapEntityToDto(targetTag);
        }
        return null;
    }

    public TagDto addIfNotExists(TagDto tagDto) {
        Tag tag = tagMapper.mapDtoToEntity(tagDto);
        if (isNull(tag)) {
            return null;
        }
        if (!isNull(tag.getId())) {
            return getByIdAndTitle(tag.getId(), tag.getTitle());
        }
        List<TagDto> listTag = tagMapper.mapEntityToDto(tagRepository.findAllByTitle(tagDto.getTitle()));
        if (listTag.isEmpty()) {
            return tagMapper.mapEntityToDto(tagRepository.save(tag));
        }
        return listTag.stream().findFirst().get();
    }

    public TagDto addIfNotExists(String title) {
        TagDto tagDto = new TagDto();
        tagDto.setTitle(title);
        return addIfNotExists(tagDto);
    }

    public TagDto add(TagDto tagDto) {
        Tag tag = tagMapper.mapDtoToEntity(tagDto);
        return tagMapper.mapEntityToDto(tagRepository.save(tag));
    }
}
