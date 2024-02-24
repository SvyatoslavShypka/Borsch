package eu.dar3.borsch.user;

import eu.dar3.borsch.friendgroup.Friendgroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String name);
    List<User> findAllByFriendgroup(Friendgroup friendgroup);
}
