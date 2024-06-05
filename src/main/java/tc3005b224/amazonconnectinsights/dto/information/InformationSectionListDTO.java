package tc3005b224.amazonconnectinsights.dto.information;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InformationSectionListDTO {
    private String sectionTitle;
    private List<InformationSectionDTO> sections; 
}
