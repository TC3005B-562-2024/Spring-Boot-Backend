package tc3005b224.amazonconnectinsights.dto.information;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InformationMetricSectionDTO {
    private String sectionTitle;
    private Double sectionValue;
    private Double sectionParentValue;
    private String color;
}
