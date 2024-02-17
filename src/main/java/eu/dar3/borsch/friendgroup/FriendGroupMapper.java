package eu.dar3.borsch.friendgroup;

import eu.dar3.borsch.mapper.Mapper;

import java.util.List;

import static java.util.Objects.isNull;

public class FriendGroupMapper implements Mapper<FriendGroup, FriendGroupDto> {

    @Override
    public FriendGroupDto mapEntityToDto(FriendGroup source) throws RuntimeException {
        if (isNull(source)) {
            return null;
        }

        return FriendGroupDto.builder()
                .title(source.getTitle())
                .id(source.getId())
                .code(source.getCode())
                .build();
    }

    @Override
    public FriendGroup mapDtoToEntity(FriendGroupDto source) throws RuntimeException {
        if (isNull(source)) {
            return null;
        }
        return FriendGroup.builder()
                .title(source.getTitle())
                .id(source.getId())
                .code(source.getCode())
                .build();
    }

    @Override
    public List<FriendGroupDto> mapEntityToDto(List<FriendGroup> source) throws RuntimeException {
        return Mapper.super.mapEntityToDto(source);
    }

    @Override
    public List<FriendGroup> mapDtoToEntity(List<FriendGroupDto> source) throws RuntimeException {
        return Mapper.super.mapDtoToEntity(source);
    }

}
