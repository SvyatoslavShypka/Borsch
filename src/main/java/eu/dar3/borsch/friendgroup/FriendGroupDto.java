package eu.dar3.borsch.friendgroup;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FriendGroupDto {
    private UUID id;
    private String title;
    private String code;
}
