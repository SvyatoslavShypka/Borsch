package eu.dar3.borsch.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class UserOptionsService {

    private final UserService userService;

    public UserOptions getOptions() {
        UserOptions options = new UserOptions();
        try {
            User currentUser = userService.getCurrentUser();
            options.setWidePage(currentUser.isWidePage());
        } catch (NoSuchElementException e) {
            //No action
        }
        return options;
    }
}
