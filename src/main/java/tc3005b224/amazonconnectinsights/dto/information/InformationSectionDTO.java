package tc3005b224.amazonconnectinsights.dto.information;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InformationSectionDTO {
    private String sectionTitle;
    private String sectionValue;
    private String color;
}
