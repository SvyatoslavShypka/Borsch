package eu.dar3.borsch.friendgroup;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FriendgroupDto {

    private UUID id;
    private String title;
    private String code;
}
