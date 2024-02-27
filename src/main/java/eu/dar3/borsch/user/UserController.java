package eu.dar3.borsch.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
        List<User> friendgroupUsers = userService.getFriendgroupUsers(currentUser.getFriendgroup());
        List<UserDto> usersFriendgroupDtos = userMapper.mapEntityToDto(friendgroupUsers);

        result.addObject("user", userDto);
        result.addObject("friendgroup", userDto.getFriendgroup());
        result.addObject("gender", GENDERS);
        result.addObject("usersFriendgroupDtos", usersFriendgroupDtos);
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
                                      @RequestParam(value = "fullWidth", required = false) String fullWidth) {

        RedirectView redirect = new RedirectView();
        userService.updateUser(email, password, nickname, birthDate, gender, fullWidth);

        redirect.setUrl("/account");
        if (!Objects.equals(oldEmail, email)) {
            redirect.setUrl("/logout");
        }
        return redirect;
    }
//TODO POST take email= & code=
    @GetMapping("/account/confirmation/{id}")
    public RedirectView confirmAccount(@PathVariable String id) {

        RedirectView redirect = new RedirectView();

        redirect.setUrl("/account");
        redirect.setUrl("/logout");
        return redirect;
    }

}
