package tc3005b224.amazonconnectinsights.dto.training;

import java.util.List;

/**
 * Data Transfer Object for a list of Training entity information
 * 
 * @author Diego Jacobo Djmr5
 */
public class ListTrainingDTO {

    private List<TrainingDTO> trainings;

    public ListTrainingDTO() {
    }

    public ListTrainingDTO(List<TrainingDTO> trainings) {
        this.trainings = trainings;
    }

    public List<TrainingDTO> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<TrainingDTO> trainings) {
        this.trainings = trainings;
    }

}