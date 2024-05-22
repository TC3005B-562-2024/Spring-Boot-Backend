package tc3005b224.amazonconnectinsights.dto.queue;

import tc3005b224.amazonconnectinsights.dto.alerts.AlertPriorityDTO;
import tc3005b224.amazonconnectinsights.dto.information.InformationSectionListDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QueueDTO {
    private String id;
    private String arn;
    private InformationSectionListDTO information;
    private InformationSectionListDTO metrics;
    private AlertPriorityDTO alerts;
    private String trainings;
    private String agents;
}
