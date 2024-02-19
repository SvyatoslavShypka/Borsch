package eu.dar3.borsch.friendgroup;

import eu.dar3.borsch.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "friendgroups", schema = "access")
public class FriendGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "code")
    private String code;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "friendGroup")
    private Set<User> users;
}
