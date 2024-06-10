package tc3005b224.amazonconnectinsights.dto.alerts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingCountDTO {
    private Object groupCount;
    private Object denomination;
    private Object trainingCompletedCount;
    private Object trainingCompletedDifference;
    private Object solvedCount;
    private Object solvedDifference;
}
