package eu.dar3.borsch.friendgroup;

import eu.dar3.borsch.user.User;
import eu.dar3.borsch.user.UserMapper;
import eu.dar3.borsch.user.UserService;
import eu.dar3.borsch.utils.Util;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static eu.dar3.borsch.utils.Constants.FRIENDGROUP_CODE_LENGTH;

@Slf4j
@RequiredArgsConstructor
@Service
public class FriendgroupService {
    private final FriendgroupRepository friendgroupRepository;
    private final FriendgroupMapper friendgroupMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public Friendgroup getFriendgroupByCode(String code) {
        Optional<Friendgroup> optional = friendgroupRepository.findByCode(code);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Friendgroup not found");
        }
        return optional.get();
    }

    public Friendgroup getFriendgroupById(UUID id) {
        Optional<Friendgroup> optional = friendgroupRepository.findById(id);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Friendgroup not found");
        }
        return optional.get();
    }

    public void leaveFriendgroup() {
        User currentUser = userService.getCurrentUser();
        userService.deleteUserFriendgroup(currentUser.getUserId());
    }

    public void addFriendgroup(Friendgroup friendgroup) {
        User currentUser = userService.getCurrentUser();
        currentUser.setFriendgroup(friendgroup);
        userService.updateUser(userMapper.mapEntityToDto(currentUser));
    }

    public FriendgroupDto createFriendgroup(String title) {
        Friendgroup friendgroup = Friendgroup.builder().title(title).build();
        friendgroup.setCode(getNewFriendgroupCode());
        friendgroupRepository.save(friendgroup);
        return friendgroupMapper.mapEntityToDto(friendgroup);
    }

    public void updateFriendgroup(UUID id, String title) {
        Friendgroup friendgroupById = getFriendgroupById(id);
        friendgroupById.setTitle(title);
        friendgroupRepository.save(friendgroupById);
    }

    private String getNewFriendgroupCode() {
        String newCode = Util.getRandomString(FRIENDGROUP_CODE_LENGTH);
        try {
            getFriendgroupByCode(newCode);
        } catch (Exception e) {
            return newCode;
        }
        return getNewFriendgroupCode();
    }
}
