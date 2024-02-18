package eu.dar3.borsch.friendgroup;

import eu.dar3.borsch.user.User;
import eu.dar3.borsch.user.UserOptionsService;
import eu.dar3.borsch.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

import static eu.dar3.borsch.utils.Constants.URL_ACCOUNT;

@RequestMapping("/friendgroup")
@RequiredArgsConstructor
@Controller
public class FriendGroupController {
    private final FriendGroupService friendGroupService;
    private final UserService userService;
    //TODO check other variant
    private final UserOptionsService userOptionsService;

    @GetMapping("/edit")
    public ModelAndView editUserFamilyShowPage() {
        User user = userService.getCurrentUser();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("friendgroup/edit");
        modelAndView.addObject("friendgroup", user.getFriendGroup());
        modelAndView.addObject("options", userOptionsService.getOptions());
        return modelAndView;
    }

    @PostMapping("/edit")
    public RedirectView editUserFamily(@RequestParam(value = "id") UUID id,
                                       @RequestParam(value = "title") String title) {
        friendGroupService.updateFriendGroup(id, title);
        RedirectView redirect = new RedirectView();
        redirect.setUrl(URL_ACCOUNT);
        return redirect;
    }

    @GetMapping("/leave")
    public RedirectView leaveFamily() {
        friendGroupService.leaveFriendGroup();
        RedirectView redirect = new RedirectView();
        redirect.setUrl(URL_ACCOUNT);
        return redirect;
    }

    @PostMapping("/add")
    public RedirectView addFamily(@RequestParam(value = "code") String code) {
        RedirectView redirect = new RedirectView();
        try {
            FriendGroup familyByCode = friendGroupService.getFriendGroupByCode(code);
            friendGroupService.addFriendGroup(familyByCode);
            redirect.setUrl(URL_ACCOUNT);
        } catch (IllegalArgumentException e) {
            redirect.setUrl("/error/404");
        }
        return redirect;
    }

    @GetMapping("/create")
    public ModelAndView createFamilyShowPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("friendgroup/create");
        modelAndView.addObject("options", userOptionsService.getOptions());
        return modelAndView;
    }

    @PostMapping("/create")
    public RedirectView createFamily(@RequestParam(value = "title") String title) {
        FriendGroupDto friendGroupDto = friendGroupService.createFriendGroup(title);
        FriendGroup friendGroupByCode = friendGroupService.getFriendGroupByCode(friendGroupDto.getCode());
        friendGroupService.addFriendGroup(friendGroupByCode);
        RedirectView redirect = new RedirectView();
        redirect.setUrl(URL_ACCOUNT);
        return redirect;
    }
}
