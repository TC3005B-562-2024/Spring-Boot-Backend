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
    public Alert fromDTO(AlertDTO alertDTO) {

        Connection connection = connectionRepository.findById(alertDTO.getConnectionId())
                .orElseThrow(() -> new IllegalArgumentException("Connection not found with ID: " + alertDTO.getConnectionId()));
        Insight insight = insightRepository.findById(alertDTO.getInsightId())
                .orElseThrow(() -> new IllegalArgumentException("Insight not found with ID: " + alertDTO.getInsightId()));
        Training training = alertDTO.getTrainingId() != null ? trainingRepository.findById(alertDTO.getTrainingId())
                .orElseThrow(() -> new IllegalArgumentException("Training not found with ID: " + alertDTO.getTrainingId())) : null;

        return new Alert(alertDTO, connection, insight, training);
    }

    // Method that stores a new alert to the database.
    public Alert saveAlert(Alert newAlert) {
        return alertRepository.save(newAlert);
    }

    // Method that gets an AlertDTO and an Alert as an input, updating only the AlertDTO attributes that are null.
    public Alert updateAlert(Alert alert, AlertDTO alertDTO) {
        Connection connection = null;
        Insight insight = null;
        Training training = null;

        if(alertDTO.getConnectionId() != null){
            connection = connectionRepository.findById(alertDTO.getConnectionId())
                    .orElseThrow(() -> new IllegalArgumentException("Connection not found with ID: " + alertDTO.getConnectionId()));
        }

        if(alertDTO.getInsightId() != null){
            insight = insightRepository.findById(alertDTO.getInsightId())
                    .orElseThrow(() -> new IllegalArgumentException("Insight not found with ID: " + alertDTO.getInsightId()));
        }

        if(alertDTO.getTrainingId() != null){
            training = trainingRepository.findById(alertDTO.getTrainingId())
                    .orElseThrow(() -> new IllegalArgumentException("Training not found with ID: " + alertDTO.getTrainingId()));
        }

        alert.updateFromDTO(alertDTO, connection, insight, training);

        return alert;
    }

    // Service that checks if the alert exists, if id does deletes it, otherwise raises an exception.
    public void deleteById(Long id) {
        alertRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Alert not found!"));
        alertRepository.deleteById(id);
    }
}
