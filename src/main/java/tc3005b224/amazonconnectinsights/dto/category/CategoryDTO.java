package tc3005b224.amazonconnectinsights.dto.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {
    private Short identifier;
    private String denomination;
    private String description;
    private Short priority;
    private LocalDateTime dateRegistered;
    private LocalDateTime dateUpdated;
    private Boolean isActive;
}
