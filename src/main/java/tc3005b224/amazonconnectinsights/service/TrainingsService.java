package tc3005b224.amazonconnectinsights.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tc3005b224.amazonconnectinsights.dto.filter.FilterDTO;
import tc3005b224.amazonconnectinsights.dto.training.TrainingDTO;
import tc3005b224.amazonconnectinsights.dto.training.TrainingNoIdDTO;
import tc3005b224.amazonconnectinsights.models_sql.Alert;
import tc3005b224.amazonconnectinsights.models_sql.Training;
import tc3005b224.amazonconnectinsights.repository.AlertRepository;
import tc3005b224.amazonconnectinsights.repository.TrainingRepository;

@Service
@Transactional
public class TrainingsService extends BaseService {
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private AlertRepository alertRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public Training convertToEntity(TrainingNoIdDTO trainingDTO, Short id) {
        if (id == null) {
            return modelMapper.map(trainingDTO, Training.class);
        } else {
            Training training = modelMapper.map(trainingDTO, Training.class);
            training.setIdentifier(id);
            return training;
        }
    }

    public TrainingDTO convertToDTO(Training training) {
        return modelMapper.map(training, TrainingDTO.class);
    }

    public Iterable<TrainingDTO> convertToListDTO(Iterable<Training> trainings) {
        return StreamSupport
                .stream(trainings.spliterator(), false)
                .map(training -> convertToDTO(training))
                .collect(Collectors.toList());
    }

    public Iterable<TrainingDTO> findAll(List<FilterDTO> filters) throws BadRequestException {
        // Avoid excessive stream operations
        if (filters == null) {
            return convertToListDTO(trainingRepository.findAll());
        } else if (filters.isEmpty()) {
            return convertToListDTO(trainingRepository.findAll());
        }

        // Limit the number of filters
        if (filters.size() > 3) {
            throw new BadRequestException("Too many filters");
        }

        // Extract valid filter values from the list of filters
        Optional<Boolean> activeFilterValue = filters.stream()
                .filter(filter -> filter.getFilterKey().equals("isActive"))
                .map(filter -> filter.getFilterValues().get(0))
                .map(value -> {
                    try {
                        return Boolean.parseBoolean(value);
                    } catch (Error ex) {
                        return null;
                    }
                })
                .findFirst();
        Optional<Iterable<Alert>> alertsFilterValues = Optional.ofNullable(filters.stream()
                .filter(filter -> filter.getFilterKey().equals("alerts"))
                .map(filter -> filter.getFilterValues().stream()
                        .map(value -> {
                            try {
                                return alertRepository.findById(Long.parseLong(value)).orElse(null);
                            } catch (Error ex) {
                                return null;
                            }
                        })
                        .collect(Collectors.toList()))
                .findFirst()
                .orElse(null));
        Optional<Iterable<String>> denominationsFilterValues = Optional.ofNullable(filters.stream()
                .filter(filter -> filter.getFilterKey().equals("denomination"))
                .map(filter -> filter.getFilterValues())
                .findFirst()
                .orElse(null));
        Optional<Iterable<String>> resourceArnsFilterValues = Optional.ofNullable(filters.stream()
                .filter(filter -> filter.getFilterKey().equals("resource"))
                .map(filter -> filter.getFilterValues())
                .findFirst()
                .orElse(null));

        // Filters: isActive, alerts, denomination
        if (activeFilterValue.isPresent() &&
                alertsFilterValues.isPresent() &&
                denominationsFilterValues.isPresent()) {
            return convertToListDTO(trainingRepository.findByIsActiveAndAlertsInAndDenominationIn(
                    activeFilterValue.get(),
                    alertsFilterValues.get(),
                    denominationsFilterValues.get()));
            // Filters: isActive, alerts
        } else if (activeFilterValue.isPresent() && alertsFilterValues.isPresent()) {
            return convertToListDTO(trainingRepository.findByIsActiveAndAlerts(
                    activeFilterValue.get(),
                    alertsFilterValues.get()));
            // Filters: isActive, denomination
        } else if (activeFilterValue.isPresent() && denominationsFilterValues.isPresent()) {
            return convertToListDTO(trainingRepository.findByIsActiveAndDenominationIn(
                    activeFilterValue.get(),
                    denominationsFilterValues.get()));
            // Filters: isActive, resourceArn
        } else if (activeFilterValue.isPresent() && resourceArnsFilterValues.isPresent()) {
            return convertToListDTO(trainingRepository.findByIsActiveAndAlertsResourceIn(
                    activeFilterValue.get(),
                    resourceArnsFilterValues.get()));
            // Filters: isActive
        } else if (activeFilterValue.isPresent()) {
            return convertToListDTO(trainingRepository.findByIsActive(
                    activeFilterValue.get()));
            // Filters: alerts, denomination
        } else if (alertsFilterValues.isPresent() && denominationsFilterValues.isPresent()) {
            return convertToListDTO(trainingRepository.findByAlertsAndDenominationIn(
                    alertsFilterValues.get(),
                    denominationsFilterValues.get()));
            // Filters: alerts
        } else if (alertsFilterValues.isPresent()) {
            return convertToListDTO(trainingRepository.findByAlertsIn(
                    alertsFilterValues.get()));
            // Filters: denomination
        } else if (denominationsFilterValues.isPresent()) {
            return convertToListDTO(trainingRepository.findByDenominationIn(
                    denominationsFilterValues.get()));
            // Filters: resourceArn
        } else if (resourceArnsFilterValues.isPresent()) {
            return convertToListDTO(trainingRepository.findByAlertsResourceIn(
                    resourceArnsFilterValues.get()));
        } else {
            throw new BadRequestException("Invalid filters");
        }
    }

    public TrainingDTO saveTraining(TrainingNoIdDTO newTraining) {
        return convertToDTO(trainingRepository.save(convertToEntity(newTraining, null)));
    }

    public TrainingDTO updateTraining(Short id, Map<String, Object> fieldsToUpdate)
            throws NotFoundException, BadRequestException {
        // Check if the identifier field is present
        try {
            fieldsHasId(fieldsToUpdate);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("The identifier field cannot be modified");
        }
        // Look for the training entity
        Training trainingOptional = trainingRepository.findById(id).orElseThrow(
                () -> new NotFoundException());
        // Update the training entity
        mapValuesToEntity(fieldsToUpdate, trainingOptional);
        trainingRepository.save(trainingOptional);
        return convertToDTO(trainingOptional);
    }

    public TrainingDTO findById(Short id) throws NotFoundException {
        return convertToDTO(trainingRepository.findById(id).orElseThrow(
                () -> new NotFoundException()));
    }

    public void deleteTraining(Short id) throws NotFoundException {
        trainingRepository.findById(id).orElseThrow(() -> new NotFoundException());
        trainingRepository.deleteById(id);
    }
}
