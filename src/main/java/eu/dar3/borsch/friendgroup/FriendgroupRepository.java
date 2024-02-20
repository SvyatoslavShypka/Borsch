package eu.dar3.borsch.friendgroup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FriendgroupRepository extends JpaRepository<Friendgroup, UUID> {
    Optional<Friendgroup> findByCode(String code);
}
