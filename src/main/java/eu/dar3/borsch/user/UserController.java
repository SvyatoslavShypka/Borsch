package eu.dar3.borsch.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Objects;

import static eu.dar3.borsch.utils.Constants.GENDERS;

@RequestMapping("/")
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserOptionsService userOptionsService;


    @GetMapping("/account")
    public ModelAndView getAccountPage() {
        ModelAndView result = new ModelAndView("user/account");

        User currentUser = userService.getCurrentUser();
        UserDto userDto = userMapper.mapEntityToDto(currentUser);
        List<User> friendGroupUsers = userService.getFriendGroupUsers(currentUser.getFriendGroup());
        List<UserDto> usersFriendGroupDtos = userMapper.mapEntityToDto(friendGroupUsers);

        result.addObject("user", userDto);
        result.addObject("friendgroup", userDto.getFriendGroupDto());
        result.addObject("gender", GENDERS);
        result.addObject("usersFriendGroupDtos", usersFriendGroupDtos);
        result.addObject("options", userOptionsService.getOptions());
        return result;
    }

    @PostMapping("/account")
    public RedirectView updateAccount(@RequestParam(value = "oldEmail") String oldEmail,
                                      @RequestParam(value = "email") String email,
                                      @RequestParam(value = "password") String password,
                                      @RequestParam(value = "nickname") String nickname,
                                      @RequestParam(value = "birthDate") String birthDate,
                                      @RequestParam(value = "gender") int gender,
                                      @RequestParam(value = "isWidePage", required = false) String isWidePage) {
        RedirectView redirect = new RedirectView();

        userService.updateUser(email, password, nickname, birthDate, gender, isWidePage);

        redirect.setUrl("/account");
        if (!Objects.equals(oldEmail, email)) {
            redirect.setUrl("/logout");
        }
        return redirect;
    }
}
