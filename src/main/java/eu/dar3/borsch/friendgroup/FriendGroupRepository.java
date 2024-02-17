package eu.dar3.borsch.friendgroup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FriendGroupRepository extends JpaRepository<FriendGroup, UUID> {
    Optional<FriendGroup> findByCode(String code);
}
