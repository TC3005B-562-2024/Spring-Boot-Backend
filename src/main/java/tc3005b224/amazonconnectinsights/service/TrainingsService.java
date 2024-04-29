package tc3005b224.amazonconnectinsights.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TrainingsService {
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
        if (filters.isEmpty()) {
            return convertToListDTO(trainingRepository.findAll());
        }

        if (filters.size() > 3) {
            throw new IllegalArgumentException("Too many filters");
        }

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

        if (activeFilterValue.isPresent() &&
                alertsFilterValues.isPresent() &&
                denominationsFilterValues.isPresent()) {
            return convertToListDTO(trainingRepository.findByIsActiveAndAlertsInAndDenominationIn(
                    activeFilterValue.get(),
                    alertsFilterValues.get(),
                    denominationsFilterValues.get()));
        } else if (activeFilterValue.isPresent() && alertsFilterValues.isPresent()) {
            return convertToListDTO(trainingRepository.findByIsActiveAndAlerts(
                    activeFilterValue.get(),
                    alertsFilterValues.get()));
        } else if (activeFilterValue.isPresent() && denominationsFilterValues.isPresent()) {
            return convertToListDTO(trainingRepository.findByIsActiveAndDenominationIn(
                    activeFilterValue.get(),
                    denominationsFilterValues.get()));
        } else if (activeFilterValue.isPresent()) {
            return convertToListDTO(trainingRepository.findByIsActive(
                    activeFilterValue.get()));
        } else if (alertsFilterValues.isPresent() && denominationsFilterValues.isPresent()) {
            return convertToListDTO(trainingRepository.findByAlertsAndDenominationIn(
                    alertsFilterValues.get(),
                    denominationsFilterValues.get()));
        } else if (alertsFilterValues.isPresent()) {
            System.out.println("Alerts filter values: " + alertsFilterValues.get());
            return convertToListDTO(trainingRepository.findByAlertsIn(
                    alertsFilterValues.get()));
        } else if (denominationsFilterValues.isPresent()) {
            return convertToListDTO(trainingRepository.findByDenominationIn(
                    denominationsFilterValues.get()));
        } else {
            throw new BadRequestException("Invalid filters");
        }
    }

    public TrainingDTO saveTraining(Short id, TrainingNoIdDTO newTraining) {
        return convertToDTO(trainingRepository.save(convertToEntity(newTraining, id)));
    }

    public TrainingDTO findById(Long id) {
        Optional<Training> trainingsOptional = trainingRepository.findById(id);

        if (trainingsOptional.isPresent()) {
            return convertToDTO(trainingsOptional.get());
        }
        return null;
    }

    public void deleteTraining(Long id) {
        trainingRepository.deleteById(id);
    }
}
