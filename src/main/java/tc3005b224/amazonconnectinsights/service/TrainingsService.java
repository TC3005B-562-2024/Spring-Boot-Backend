package tc3005b224.amazonconnectinsights.service;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
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

    public Iterable<TrainingDTO> findAll(String resource, String alertId, String isActive) throws BadRequestException {
        // Avoid excessive stream operations
        if (resource.isEmpty() && alertId.isEmpty() && isActive.isEmpty()) {
            return convertToListDTO(trainingRepository.findAll());
        } else if (!resource.isEmpty() && alertId.isEmpty() && !isActive.isEmpty()) {
            if (isActive.equals("true")) {
                return convertToListDTO(trainingRepository.findByIsActiveAndAlertsResource(true, resource));
            } else if (isActive.equals("false")) {
                return convertToListDTO(trainingRepository.findByIsActiveAndAlertsResource(false, resource));
            } else {
                throw new BadRequestException("Invalid value at isActive: " + isActive);
            }
        } else if (!resource.isEmpty() && alertId.isEmpty() && isActive.isEmpty()) {
            return convertToListDTO(trainingRepository.findByAlertsResource(resource));
        } else if (resource.isEmpty() && !alertId.isEmpty() && isActive.isEmpty()) {
            Alert alert = alertRepository.findById(Long.parseLong(alertId)).orElseThrow(
                    () -> new BadRequestException("Invalid alertId"));
            return convertToListDTO(trainingRepository.findByAlerts(alert));
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
