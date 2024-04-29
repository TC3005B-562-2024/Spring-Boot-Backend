package tc3005b224.amazonconnectinsights.dto.training;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tc3005b224.amazonconnectinsights.models_sql.Alert;

/**
 * Data Transfer Object for Training entity information
 * 
 * @author Diego Jacobo Djmr5
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainingNoIdDTO {

    private String denomination;
    private String description;
    private Date dateRegistered;
    private Date dateUpdated;
    private boolean isActive;
    @JsonIgnore
    private List<Alert> alerts;

}
