package tc3005b224.amazonconnectinsights.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertHighPriorityDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertPriorityDTO;
import tc3005b224.amazonconnectinsights.models_sql.Alert;
import tc3005b224.amazonconnectinsights.models_sql.Connection;
import tc3005b224.amazonconnectinsights.models_sql.Insight;
import tc3005b224.amazonconnectinsights.models_sql.Training;
import tc3005b224.amazonconnectinsights.repository.AlertRepository;
import tc3005b224.amazonconnectinsights.repository.ConnectionRepository;
import tc3005b224.amazonconnectinsights.repository.InsightRepository;
import tc3005b224.amazonconnectinsights.repository.TrainingRepository;

@Service
@Transactional
public class AlertService extends BaseService {
    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private InsightRepository insightRepository;

    // Service that returns all the unsolved alerts, ordered by priority that belong to a given connection.
    public AlertPriorityDTO findAll(int connectionIdentifier, String denominationAlike, String resource, String logs) {
        if(!logs.equals("true") && !logs.equals("false")) {
            throw new IllegalArgumentException("Invalid value for logs parameter. Must be 'true' or 'false'.");
        }

        // Instantiate an AlertPriorityDTO
        AlertPriorityDTO response = new AlertPriorityDTO();

        for (int priority = 1; priority <= 3; priority++) {
            // Query using the defined parameters
            Iterable<Alert> queryPriority;

            if(logs.equals("true")){
                queryPriority = alertRepository.findByConnectionIdentifierAndResourceContainingAndSolvedIsNotNullAndInsight_Category_PriorityAndInsight_Category_DenominationContaining(connectionIdentifier, resource, priority, denominationAlike);
            }else{
                queryPriority = alertRepository.findByConnectionIdentifierAndResourceContainingAndSolvedAndInsight_Category_PriorityAndInsight_Category_DenominationContaining(connectionIdentifier, resource, null, priority, denominationAlike);
            }

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
    public void updateAlert(Long alertIdentifier, AlertDTO alertDTO) {
        Alert queriedAlert = this.findByIdentifier(alertIdentifier);

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

        queriedAlert.updateFromDTO(alertDTO, connection, insight, training);

        this.saveAlert(queriedAlert);
    }

    // Service that checks if the alert exists, if id does deletes it, otherwise raises an exception.
    public void deleteById(Long id) {
        alertRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Alert not found!"));
        alertRepository.deleteById(id);
    }

    // Service that ignores (deletes logically) an alert.
    public void ignoreById(Long id) {
        // Is solved set to false
        AlertDTO alertDTO = new AlertDTO(null, null, null, null, false, null);
        this.updateAlert(id, alertDTO);
    }

    // Service that accepts an alert and executes a series of functions so that
    // the insight of that alert is followed.
    public String acceptById(Long id) {
        // TODO: If category = Training, save training to DB.
        // TODO: If category = Intervene, barge into call.
        // TODO: If category = Transfer, move agent to the desired resurce.

        Alert alert = alertRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Alert not found!"));
        String alertInsightCategoryDenomination = alert.getInsight().getCategory().getDenomination();

        // Is solved set to true
        AlertDTO alertDTO = new AlertDTO(null, null, null, null, true, null);
        this.updateAlert(id, alertDTO);
        return alertInsightCategoryDenomination;
    }

    /**
     * New method to find the highest priority of the alerts
     * TODO: Add check for unexisting resource
     * 
     * @param token
     * @param resource
     * @return AlertHighPriorityDTO
     * 
     * @author Andrew Williams SirPotat28
     * 
     * @see AlertRepository
     * @see AlertHighPriorityDTO
     * @see ConnectClientInfo
     */
    public AlertHighPriorityDTO findHighestPriority(String token, String resource) {
        Optional<Integer> highestPriority;
        ConnectClientInfo clientInfo = getConnectClientInfo(token);
        if (resource != null) {
            highestPriority = alertRepository.findHighestPriorityByResource(resource,
                    clientInfo.getConnectionIdentifier());
        } else {
            highestPriority = alertRepository.findHighestPriority(clientInfo.getConnectionIdentifier());
        }

        if (highestPriority.isPresent()) {
            int priorityValue = highestPriority.get();
            switch (priorityValue) {
                case 3:
                    return new AlertHighPriorityDTO("high");
                case 2:
                    return new AlertHighPriorityDTO("medium");
                case 1:
                    return new AlertHighPriorityDTO("low");
                default:
                    return new AlertHighPriorityDTO(null);
            }
        } else {
            return new AlertHighPriorityDTO(null);
        }
    }

    /**
     * Service that returns all the alerts where is_solved is 1, has_training is 1 and the resource is the same as the one given.
     * 
     * @param token
     * @param resource
     * @return List<Alert>
     * @throws BadRequestException
     * 
     * @see Alert
     * @see BadRequestException
     * 
     * @author Moisés Adame
     * 
     */
    public Iterable<Alert> findTrainingAlerts(String token, String resource) {
        ConnectClientInfo clientInfo = getConnectClientInfo(token);
        return alertRepository.findByConnectionIdentifierAndResourceAndSolvedAndHasTraining(clientInfo.getConnectionIdentifier(), resource, true, true);
    }

    /**
     * Service that returns all the alerts with a given resource, insightId and that are between two dates.
     * 
     * @param resource
     * @param insightIdentifier
     * 
     * @see alertRepository.findByResourceAndInsightIdentifierAndDateRegisteredBetween()
     * 
     * @return Iterable<Alert>
     * 
     * @author Moisés Adame
     * 
     */
    public Iterable<Alert> checkAlertExists(String resource, Short insightIdentifier) {
        Date currentDate = new Date();
        Date currentMinusOneHourDate = new Date(System.currentTimeMillis() - 3600 * 1000);

        return alertRepository.findByResourceAndInsightIdentifierAndDateRegisteredBetween(resource, insightIdentifier, currentMinusOneHourDate, currentDate);
    }
}
