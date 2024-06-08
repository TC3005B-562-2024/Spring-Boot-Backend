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
    private int groupCount;
    private String insightDenomination;
    private String categoryDenomination;
    private int trainingCompletedCount;
    private int trainingCompletedDifference;
    private int solvedCount;
    private int solvedDifference;
}
