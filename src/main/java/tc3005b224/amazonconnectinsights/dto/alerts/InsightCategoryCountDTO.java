package tc3005b224.amazonconnectinsights.dto.alerts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsightCategoryCountDTO {
    private Object groupCount;
    private Object insightDenomination;
    private Object categoryDenomination;
    private Object trainingCompletedCount;
    private Object trainingCompletedDifference;
    private Object solvedCount;
    private Object solvedDifference;
}
