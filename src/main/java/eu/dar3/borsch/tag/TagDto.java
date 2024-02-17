package eu.dar3.borsch.tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDto {
    private UUID id;
    private String title;
    private Instant createdDate;
    private Instant updatedDate;
}
