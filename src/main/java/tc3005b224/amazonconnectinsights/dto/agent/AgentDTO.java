package tc3005b224.amazonconnectinsights.dto.agent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertPriorityDTO;
import tc3005b224.amazonconnectinsights.dto.information.ContactInformationDTO;
import tc3005b224.amazonconnectinsights.dto.information.InformationSectionListDTO;
import tc3005b224.amazonconnectinsights.dto.utils.IdAndNameDTO;
import tc3005b224.amazonconnectinsights.models_sql.Alert;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgentDTO {
    private String id;
    private String arn;
    private InformationSectionListDTO information;
    private InformationSectionListDTO metrics;
    private AlertPriorityDTO alerts;
    private Iterable<Alert> trainings;
    private Iterable<IdAndNameDTO> queues;
    private Iterable<ContactInformationDTO> contactInformationDTO;
}
