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
public class FriendgroupController {
    private final FriendgroupService friendgroupService;
    private final UserService userService;
    //TODO check other variant
    private final UserOptionsService userOptionsService;

    @GetMapping("/edit")
    public ModelAndView editUserFriendgroupShowPage() {
        User user = userService.getCurrentUser();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("friendgroup/edit");
        modelAndView.addObject("friendgroup", user.getFriendgroup());
        modelAndView.addObject("options", userOptionsService.getOptions());
        return modelAndView;
    }

    @PostMapping("/edit")
    public RedirectView editUserFriendgroup(@RequestParam(value = "id") UUID id,
                                            @RequestParam(value = "title") String title) {
        friendgroupService.updateFriendgroup(id, title);
        RedirectView redirect = new RedirectView();
        redirect.setUrl(URL_ACCOUNT);
        return redirect;
    }

    @GetMapping("/leave")
    public RedirectView leaveFriendgroup() {
        friendgroupService.leaveFriendgroup();
        RedirectView redirect = new RedirectView();
        redirect.setUrl(URL_ACCOUNT);
        return redirect;
    }

    @PostMapping("/add")
    public RedirectView addFriendgroup(@RequestParam(value = "code") String code) {
        RedirectView redirect = new RedirectView();
        try {
            Friendgroup familyByCode = friendgroupService.getFriendgroupByCode(code);
            friendgroupService.addFriendgroup(familyByCode);
            redirect.setUrl(URL_ACCOUNT);
        } catch (IllegalArgumentException e) {
            redirect.setUrl("/error/404");
        }
        return redirect;
    }

    @GetMapping("/create")
    public ModelAndView createFriendgroupShowPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("friendgroup/create");
        modelAndView.addObject("options", userOptionsService.getOptions());
        return modelAndView;
    }

    @PostMapping("/create")
    public RedirectView createFriendgroup(@RequestParam(value = "title") String title) {
        FriendgroupDto friendgroupDto = friendgroupService.createFriendgroup(title);
        Friendgroup friendgroupByCode = friendgroupService.getFriendgroupByCode(friendgroupDto.getCode());
        friendgroupService.addFriendgroup(friendgroupByCode);
        RedirectView redirect = new RedirectView();
        redirect.setUrl(URL_ACCOUNT);
        return redirect;
    }
}
