package tc3005b224.amazonconnectinsights.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import software.amazon.awssdk.services.connect.model.MonitorCapability;
import software.amazon.awssdk.services.connect.model.MonitorContactRequest;
import software.amazon.awssdk.services.connect.model.MonitorContactResponse;
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
    @Autowired
    private UserService userService;

    //Service to monitor a contact via BARGE
    public MonitorContactResponse monitorContact(String userUuid, String contactId) {
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);

        String userId = "7d76a01c-674f-431b-94ed-2d9a936ff3e3";

        MonitorContactRequest request = MonitorContactRequest.builder()
                .instanceId(clientInfo.getInstanceId())
                .contactId(contactId)
                .userId(userId)
                .allowedMonitorCapabilities(MonitorCapability.SILENT_MONITOR, MonitorCapability.BARGE)
                .build();

        return getConnectClient(clientInfo.getAccessKeyId(), clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .monitorContact(request);
    }

    // Service that returns all the unsolved alerts, ordered by priority that belong to a given connection.
    public AlertPriorityDTO findAll(String userUuid, String denominationAlike, String resource, String logs) {
        if(!logs.equals("true") && !logs.equals("false")) {
            throw new IllegalArgumentException("Invalid value for logs parameter. Must be 'true' or 'false'.");
        }

        // Get the connection identifier
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);
        int connectionIdentifier = clientInfo.getIdentifier();

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

    // Service that returns an alert given its alertId, if it is not found, returns
    // a null.
    public Alert findByIdentifier(String userUuid, Long id) throws Exception {
        Optional<Alert> alertsOptional = alertRepository.findById(id);

        if (alertsOptional.isPresent()) {
            if (alertsOptional.get().getConnection().getIdentifier() == getConnectClientInfo(userUuid)
                    .getIdentifier()) {
                return alertsOptional.get();
            }
            throw new Exception("Unauthorized access to alert");
        }
        return null;
    }

    // Service that returns all the unsolved alerts, ordered by priority that belong to a given resource given its resourceArn.
    public AlertPriorityDTO findByResource(String userUuid, String resourceArn){
        // Get the client information
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);
        int connectionIdentifier = clientInfo.getIdentifier();
        
        // Instantiate an AlertPriorityDTO
        AlertPriorityDTO response = new AlertPriorityDTO();

        for (int priority = 1; priority <= 3; priority++) {
            // Query using the defined parameters
            Iterable<Alert> queryPriority = alertRepository.findByConnectionIdentifierAndResourceAndSolvedAndInsight_Category_Priority(connectionIdentifier, resourceArn, null, priority);
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
    public Alert saveAlert(String userUuid, Alert newAlert) throws BadRequestException {
        return alertRepository.save(newAlert);
    }

    // Method that gets an AlertDTO and an Alert as an input, updating only the AlertDTO attributes that are null.
    public void updateAlert(String userUuid, Long alertIdentifier, AlertDTO alertDTO) throws Exception {        
        Alert queriedAlert = this.findByIdentifier(userUuid, alertIdentifier);

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

        this.saveAlert(userUuid, queriedAlert);
    }

    // Service that checks if the alert exists, if id does deletes it, otherwise raises an exception.
    public void deleteById(String userUuid, Long id) throws Exception {
        alertRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Alert not found!"));
        if (alertRepository.findById(id).get().getConnection().getIdentifier() != getConnectClientInfo(userUuid)
                .getIdentifier()) {
            throw new Exception("Unauthorized access to alert");
        }
        alertRepository.deleteById(id);
    }

    // Service that ignores (deletes logically) an alert.
    public void ignoreById(String userUuid, Long id) throws Exception {
        // Is solved set to false
        AlertDTO alertDTO = new AlertDTO();
        alertDTO.setSolved(false);
        this.updateAlert(userUuid, id, alertDTO);
    }

    // Service that accepts an alert and executes a series of functions so that
    // the insight of that alert is followed.
    public String acceptById(String userUuid, Long id) throws Exception {
        Alert alert = alertRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Alert not found!"));
        String alertInsightCategoryDenomination = alert.getInsight().getCategory().getDenomination();
        AlertDTO alertDTO = new AlertDTO();

        try {
            if (alertInsightCategoryDenomination.equals("training")) {
                // Save training to DB
                alertDTO.setTrainingCompleted(false);
            } else if (alertInsightCategoryDenomination.equals("intervene")) {
                // Barge into call
                this.monitorContact(userUuid, alert.getInterveneContact());
            } else if (alertInsightCategoryDenomination.equals("transfer")) {
                // Move agent to the desired resource
                userService.transfer(userUuid, alert.getTransferedAgent(), alert.getDestinationRoutingProfile());
            }
        } catch (Exception e) {
            throw new Exception("Error while accepting alert: " + e.getMessage());
        }

        // Is solved set to true
        alertDTO.setSolved(true);
        this.updateAlert(userUuid, id, alertDTO);
        return alertInsightCategoryDenomination;
    }

    /**
     * New method to find the highest priority of the alerts
     * TODO: Add check for unexisting resource
     * 
     * @param userUuid
     * @param resource
     * @return AlertHighPriorityDTO
     * 
     * @author Andrew Williams SirPotat28
     * 
     * @see AlertRepository
     * @see AlertHighPriorityDTO
     * @see ConnectClientInfo
     */
    public AlertHighPriorityDTO findHighestPriority(String userUuid, String resource) {
        Optional<Integer> highestPriority;
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);
        if (resource != null) {
            highestPriority = alertRepository.findHighestPriorityByResource(resource,
                    clientInfo.getIdentifier());
        } else {
            highestPriority = alertRepository.findHighestPriority(clientInfo.getIdentifier());
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
     * @param userUuid
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
    public Iterable<Alert> findTrainingAlerts(String userUuid, String resource) {
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);
        return alertRepository.findByConnectionIdentifierAndResourceAndSolvedAndHasTraining(clientInfo.getIdentifier(), resource, true, true);
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

    /**
     * Service that calls the get_alert_insight_category_count stored procedure.
     * 
     * @param userUuid
     * 
     * @see alertRepository.callAlertInsightCategoryCountProcedure
     * 
     * @return Iterable<?>
     * 
     * @author Moisés Adame
     * 
     */
    public Iterable<?> callAlertInsightCategoryCountProcedure(String userUuid) {
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);
        return alertRepository.callAlertInsightCategoryCountProcedure(clientInfo.getIdentifier(), 1, true);
    }

    /**
     * Service that calls the get_alert_training_count stored procedure.
     * 
     * @param userUuid
     * 
     * @see alertRepository.callTrainigProcedure
     * 
     * @return Iterable<?>
     * 
     * @author Moisés Adame
     * 
     */
    public Iterable<?> callTrainingCountProcedure(String userUuid) {
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);
        return alertRepository.callTrainigProcedure(clientInfo.getIdentifier(), 1, true);
    }
}
