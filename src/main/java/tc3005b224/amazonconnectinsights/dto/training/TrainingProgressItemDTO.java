package tc3005b224.amazonconnectinsights.dto.training;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for Training Progress Item entity information.
 * 
 * @author Moisés Adame
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainingProgressItemDTO {
    private String resourceName;
    private Float resourceTrainingProgress;
}
