package eu.dar3.borsch.user;

import eu.dar3.borsch.friendgroup.FriendGroupDto;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

public class UserDto {
    private UUID id;
    private String email;
    private String password;
    private boolean isEnable;
    private String nickname;
    private Date birthDate;
    private int genderId;
    private Instant updatedDate;
    private Instant createdDate;
    @Enumerated(EnumType.STRING)
    private FriendGroupDto family;
    private UserRoles role;
    private boolean fullWidth;
}
