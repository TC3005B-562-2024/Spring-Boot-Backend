package tc3005b224.amazonconnectinsights.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertPriorityDTO;
import tc3005b224.amazonconnectinsights.models_sql.Alert;
import tc3005b224.amazonconnectinsights.models_sql.Connection;
import tc3005b224.amazonconnectinsights.models_sql.Insight;
import tc3005b224.amazonconnectinsights.models_sql.Training;
import tc3005b224.amazonconnectinsights.repository.AlertRepository;
import tc3005b224.amazonconnectinsights.repository.ConnectionRepository;
import tc3005b224.amazonconnectinsights.repository.InsightRepository;
import tc3005b224.amazonconnectinsights.repository.TrainingRepository;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AlertService {
    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private InsightRepository insightRepository;

    // Service that returns all the unsolved alerts, ordered by priority that belong to a given connection.
    public AlertPriorityDTO findAll(int connectionIdentifier, String denominationAlike) {
        // Instantiate an AlertPriorityDTO
        AlertPriorityDTO response = new AlertPriorityDTO();

        for (int priority = 1; priority <= 3; priority++) {
            // Query using the defined parameters
            Iterable<Alert> queryPriority = alertRepository.findByConnectionIdentifierAndSolvedAndInsight_Category_PriorityAndInsight_Category_DenominationContaining(connectionIdentifier, false, priority, denominationAlike);
            List<Alert> listByPriority = new ArrayList<>();
            queryPriority.forEach(alert -> listByPriority.add(alert));

            // Divide the alerts by priority
            if(priority == 1){
                response.setLow(listByPriority);
            } else if (priority == 2) {
                response.setMedium(listByPriority);
            }else{
                response.setHigh(listByPriority);
            }
        }

        return response;
    }

    // Service that returns an alert given its alertId, if it is not found, returns a null.
    public Alert findByIdentifier(Long id) {
        Optional<Alert> alertsOptional = alertRepository.findById(id);

        if(alertsOptional.isPresent()) {
            return alertsOptional.get();
        }
        return null;
    }

    // Service that returns all the unsolved alerts, ordered by priority that belong to a given resource given its resourceArn.
    public AlertPriorityDTO findByResource(int connectionIdentifier, String resourceArn){
        // Instantiate an AlertPriorityDTO
        AlertPriorityDTO response = new AlertPriorityDTO();

        for (int priority = 1; priority <= 3; priority++) {
            // Query using the defined parameters
            Iterable<Alert> queryPriority = alertRepository.findByConnectionIdentifierAndResourceAndSolvedAndInsight_Category_Priority(connectionIdentifier, resourceArn, false, priority);
            List<Alert> listByPriority = new ArrayList<>();
            queryPriority.forEach(alert -> listByPriority.add(alert));

            // Divide the alerts by priority
            if(priority == 1){
                response.setLow(listByPriority);
            } else if (priority == 2) {
                response.setMedium(listByPriority);
            }else{
                response.setHigh(listByPriority);
            }
        }

        return response;
    }

    // Method that gets an AlertDTO as an input and returns an instance of an Alert.
    public Alert fromDTO(AlertDTO dto) {

        Connection connection = connectionRepository.findById(dto.getConnectionId())
                .orElseThrow(() -> new IllegalArgumentException("Connection not found with ID: " + dto.getConnectionId()));
        Insight insight = insightRepository.findById(dto.getInsightId())
                .orElseThrow(() -> new IllegalArgumentException("Insight not found with ID: " + dto.getInsightId()));
        Training training = dto.getTrainingId() != null ? trainingRepository.findById(dto.getTrainingId())
                .orElseThrow(() -> new IllegalArgumentException("Training not found with ID: " + dto.getTrainingId())) : null;

        Alert alert = new Alert();
        alert.setConnection(connection);
        alert.setInsight(insight);
        alert.setTraining(training);
        alert.setResource(dto.getResource());
        alert.setDateRegistered(dto.getDateRegistered());
        alert.setDateUpdated(dto.getDateUpdated());
        alert.setSolved(dto.isSolved());
        alert.setDateTrainingCompleted(dto.getDateTrainingCompleted());
        alert.setHasTraining(dto.getTrainingId() != null);
        alert.setTrainingCompleted(dto.getDateTrainingCompleted() != null);

        return alert;
    }

    // Method that stores a new alert to the database.
    public Alert saveAlert(Alert newAlert) {
        return alertRepository.save(newAlert);
    }

    // Method that gets an AlertDTO and an Alert as an input, updating only the AlertDTO attributes that are null.
    public Alert updateAlert(Alert oldAlert, AlertDTO dto) {
        if(dto.getConnectionId() != null){
            Connection connection = connectionRepository.findById(dto.getConnectionId())
                    .orElseThrow(() -> new IllegalArgumentException("Connection not found with ID: " + dto.getConnectionId()));
            oldAlert.setConnection(connection);
        }

        if(dto.getInsightId() != null){
            Insight insight = insightRepository.findById(dto.getInsightId())
                    .orElseThrow(() -> new IllegalArgumentException("Insight not found with ID: " + dto.getInsightId()));
            oldAlert.setInsight(insight);
        }

        if(dto.getTrainingId() != null){
            Training training = trainingRepository.findById(dto.getTrainingId())
                    .orElseThrow(() -> new IllegalArgumentException("Training not found with ID: " + dto.getTrainingId()));
            oldAlert.setTraining(training);
        }

        if(dto.getResource() != null){
            oldAlert.setResource(dto.getResource());
        }

        if(dto.getDateRegistered() != null){
            oldAlert.setDateRegistered(dto.getDateRegistered());
        }

        if(dto.getDateUpdated() != null){
            oldAlert.setDateRegistered(dto.getDateUpdated());
        }

        if(dto.isSolved() != null){
            oldAlert.setSolved(dto.isSolved());
        }

        if(dto.getDateTrainingCompleted() != null){
            oldAlert.setDateTrainingCompleted(dto.getDateTrainingCompleted());
        }

        return oldAlert;
    }

    // Service that checks if the alert exists, if id does deletes it, otherwise raises an exception.
    public void deleteById(Long id) {
        alertRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Alert not found!"));
        alertRepository.deleteById(id);
    }
}
