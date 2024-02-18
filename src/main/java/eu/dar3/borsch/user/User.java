package eu.dar3.borsch.user;

import eu.dar3.borsch.friendgroup.FriendGroup;
import eu.dar3.borsch.recipe.Recipe;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "users", schema = "access")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;
    @Column(name = "username")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "enabled")
    private boolean isEnable;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "birthday")
    private Date birthDate;
    //TODO to change on ENUM
    @Column(name = "gender_id")
    private int genderId;
    @Column(name = "created_date")
    @CreationTimestamp
    private Instant createdDate;
    @Column(name = "updated_date")
    @UpdateTimestamp
    private Instant updatedDate;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipeOwner")
    private List<Recipe> recipes;
    @ManyToOne
    @JoinColumn(name = "friendgroup_id")
    private FriendGroup friendGroup;
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoles role;
    @Column(name = "wide_page")
    private boolean isWidePage;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return userId.equals(user.userId);
    }
    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
