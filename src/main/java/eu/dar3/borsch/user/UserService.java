package eu.dar3.borsch.user;

import eu.dar3.borsch.friendgroup.Friendgroup;
import eu.dar3.borsch.recipe.Recipe;
import eu.dar3.borsch.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final int GENDER_ZERO = 0;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;


    public List<User> getFriendgroupUsers(Friendgroup friendgroup) {
        return userRepository.findAllByFriendgroup(friendgroup);
    }

    public User findUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("User with id: [" + id + "] does not exist!"));
        List<Recipe> recipes = user.getRecipes();
        user.setRecipes(recipes);
        return user;
    }

    public void updateUser(UserDto userDto) {
        UserDto dto = userMapper.mapEntityToDto(findUserById(userDto.getId()));
        BeanUtils.copyProperties(userDto, dto, Util.getNullPropertyNames(userDto));
        userValidator.setUserService(this);
        userValidator.validate(dto, false);
        userRepository.save(userMapper.mapDtoToEntity(dto));
    }

    public void updateUser(String email,
                           String password,
                           String nickname,
                           String birthDate,
                           int gender, String fullWidth) {
        User currentUser = getCurrentUser();
        UserDto userDto = userMapper.mapEntityToDto(currentUser);
        userDto.setEmail(email);
        if (!birthDate.isBlank()) {
            userDto.setBirthDate(Date.valueOf(Util.getLocalDateFromString(birthDate)));
        }
        userDto.setNickname(nickname);
        userDto.setGenderId(gender);

        userDto.setFullWidth(fullWidth != null);

        if (!password.isBlank()) {
            if (!passwordEncoder.matches(password, currentUser.getPassword())) {
                userDto.setPassword(passwordEncoder.encode(password));
            }
        }
        updateUser(userDto);
    }

    public void deleteUserFriendgroup(UUID userId) {
        User userById = findUserById(userId);
        userById.setFriendgroup(null);
        userRepository.save(userById);
    }

    public void createNewUser(String username, String password, String nickname) {
        UserDto userDto = UserDto.builder()
                .email(username)
                .password(password)
                .nickname(nickname)
//                TODO make default false and after confirmation - true
                .isEnable(false)
                .genderId(GENDER_ZERO)
                .role(UserRoles.ROLE_USER)
                .build();
        userValidator.setUserService(this);
        userValidator.validate(userDto, true);
        userDto.setPassword(passwordEncoder.encode(password));
        User user = userMapper.mapDtoToEntity(userDto);
        userRepository.save(user);
    }

    public User findUserByName(String userName) {
//        System.out.println("Username: " + userName);
//        List<User> all = userRepository.findAll();
//        Optional<User> user_tmp = userRepository.findByEmail(userName);
        User user = userRepository.findByEmail(userName).orElseThrow(() -> new NoSuchElementException("User with email: [" + userName + "] does not exist!"));
        List<Recipe> recipes = user.getRecipes();
        user.setRecipes(recipes);
        return user;
    }

    public User getCurrentUser() {
        String username = "";
        try {
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            username = principal.getUsername();
        } catch (Exception e) {
            //No action
        }
        return findUserByName(username);
    }
}
