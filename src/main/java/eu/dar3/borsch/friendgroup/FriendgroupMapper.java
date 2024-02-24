package eu.dar3.borsch.friendgroup;

import static java.util.Objects.isNull;

import eu.dar3.borsch.mapper.Mapper;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FriendgroupMapper implements Mapper<Friendgroup, FriendgroupDto> {

    @Override
    public FriendgroupDto mapEntityToDto(Friendgroup source) throws RuntimeException {
        if (isNull(source)) {
            return null;
        }

        return FriendgroupDto.builder()
            .title(source.getTitle())
            .id(source.getId())
            .code(source.getCode())
            .build();
    }

    @Override
    public Friendgroup mapDtoToEntity(FriendgroupDto source) throws RuntimeException {
        if (isNull(source)) {
            return null;
        }
        return Friendgroup.builder()
            .title(source.getTitle())
            .id(source.getId())
            .code(source.getCode())
            .build();
    }

    @Override
    public List<FriendgroupDto> mapEntityToDto(List<Friendgroup> source) throws RuntimeException {
        return Mapper.super.mapEntityToDto(source);
    }

    @Override
    public List<Friendgroup> mapDtoToEntity(List<FriendgroupDto> source) throws RuntimeException {
        return Mapper.super.mapDtoToEntity(source);
    }
}
