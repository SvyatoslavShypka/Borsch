package eu.dar3.borsch.user;

import eu.dar3.borsch.friendgroup.FriendGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String name);
    List<User> findAllByFriendGroup(FriendGroup friendGroup);
}
