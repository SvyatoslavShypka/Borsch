package eu.dar3.borsch.friendgroup;

import eu.dar3.borsch.user.User;
import eu.dar3.borsch.user.UserMapper;
import eu.dar3.borsch.user.UserService;
import eu.dar3.borsch.utils.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static eu.dar3.borsch.utils.Constants.FRIENDGROUP_CODE_LENGTH;

@Slf4j
@RequiredArgsConstructor
@Service
public class FriendGroupService {
    private final FriendGroupRepository friendGroupRepository;
    private final FriendGroupMapper friendGroupMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public FriendGroup getFriendGroupByCode(String code) {
        Optional<FriendGroup> optional = friendGroupRepository.findByCode(code);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("FriendGroup not found");
        }
        return optional.get();
    }

    public FriendGroup getFriendGroupById(UUID id) {
        Optional<FriendGroup> optional = friendGroupRepository.findById(id);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("FriendGroup not found");
        }
        return optional.get();
    }

    public void leaveFriendGroup() {
        User currentUser = userService.getCurrentUser();
        userService.deleteUserFriendGroup(currentUser.getUserId());
    }

    public void addFriendGroup(FriendGroup friendGroup) {
        User currentUser = userService.getCurrentUser();
        currentUser.setFriendGroup(friendGroup);
        userService.updateUser(userMapper.mapEntityToDto(currentUser));
    }

    public FriendGroupDto createFriendGroup(String title) {
        FriendGroup friendGroup = FriendGroup.builder().title(title).build();
        friendGroup.setCode(getNewFriendGroupCode());
        friendGroupRepository.save(friendGroup);
        return friendGroupMapper.mapEntityToDto(friendGroup);
    }

    public void updateFriendGroup(UUID id, String title) {
        FriendGroup friendGroupById = getFriendGroupById(id);
        friendGroupById.setTitle(title);
        friendGroupRepository.save(friendGroupById);
    }

    private String getNewFriendGroupCode() {
        String newCode = Util.getRandomString(FRIENDGROUP_CODE_LENGTH);
        try {
            getFriendGroupByCode(newCode);
        } catch (Exception e) {
            return newCode;
        }
        return getNewFriendGroupCode();
    }
}
