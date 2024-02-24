package eu.dar3.borsch.friendgroup;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendgroupRepository extends JpaRepository<Friendgroup, UUID> {
    Optional<Friendgroup> findByCode(String code);
}
