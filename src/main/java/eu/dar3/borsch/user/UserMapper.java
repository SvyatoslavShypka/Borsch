package eu.dar3.borsch.user;
import eu.dar3.borsch.friendgroup.FriendgroupMapper;
import eu.dar3.borsch.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapper implements Mapper<User, UserDto> {

    private final FriendgroupMapper friendgroupMapper;

    @Override
    public UserDto mapEntityToDto(User source) throws RuntimeException {
        UserDto target = new UserDto();
        target.setPassword(source.getPassword());
        target.setId(source.getUserId());
        target.setEmail(source.getEmail());
        target.setNickname(source.getNickname());
        target.setBirthDate(source.getBirthDate());
        target.setGenderId(source.getGenderId());
        target.setEnable(source.isEnable());
        target.setCreatedDate(source.getCreatedDate());
        target.setUpdatedDate(source.getUpdatedDate());
        target.setRole(source.getRole());
        target.setFriendgroup(friendgroupMapper.mapEntityToDto(source.getFriendgroup()));
        target.setFullWidth(source.isFullWidth());
        return target;
    }

    @Override
    public User mapDtoToEntity(UserDto source) throws RuntimeException {
        User target = new User();
        target.setPassword(source.getPassword());
        target.setUserId(source.getId());
        target.setEmail(source.getEmail());
        target.setNickname(source.getNickname());
        target.setBirthDate(source.getBirthDate());
        target.setGenderId(source.getGenderId());
        target.setEnable(source.isEnable());
        target.setCreatedDate(source.getCreatedDate());
        target.setUpdatedDate(source.getUpdatedDate());
        target.setRole(source.getRole());
        target.setFriendgroup(friendgroupMapper.mapDtoToEntity(source.getFriendgroup()));
        target.setFullWidth(source.isFullWidth());
        return target;
    }
}
