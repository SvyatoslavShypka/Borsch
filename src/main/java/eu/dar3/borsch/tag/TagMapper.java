package eu.dar3.borsch.tag;

import eu.dar3.borsch.mapper.Mapper;

import java.util.List;

import static java.util.Objects.isNull;

public class TagMapper implements Mapper<Tag, TagDto> {
    @Override
    public TagDto mapEntityToDto(Tag source) throws RuntimeException {
        if (isNull(source)) {
            return null;
        }
        TagDto target = new TagDto();
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setCreatedDate(source.getCreatedDate());
        target.setUpdatedDate(source.getUpdatedDate());
        return target;
    }

    @Override
    public Tag mapDtoToEntity(TagDto source) throws RuntimeException {
        if (isNull(source)) {
            return null;
        }
        Tag target = new Tag();
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setCreatedDate(source.getCreatedDate());
        target.setUpdatedDate(source.getUpdatedDate());
        return target;
    }

    @Override
    public List<TagDto> mapEntityToDto(List<Tag> source) throws RuntimeException {
        return Mapper.super.mapEntityToDto(source);
    }

    @Override
    public List<Tag> mapDtoToEntity(List<TagDto> source) throws RuntimeException {
        return Mapper.super.mapDtoToEntity(source);
    }
}
