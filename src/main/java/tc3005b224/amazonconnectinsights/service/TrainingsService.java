package tc3005b224.amazonconnectinsights.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tc3005b224.amazonconnectinsights.models_sql.Training;
import tc3005b224.amazonconnectinsights.repository.TrainingRepository;

import java.util.Optional;

@Service
@Transactional
public class TrainingsService {
    @Autowired
    private TrainingRepository trainingRepository;

    public Iterable<Training> findAll() {
        return trainingRepository.findAll();
    }

    public Training saveTrainings(Long id, Training newTraining) {
        return trainingRepository.save(newTraining);
    }

    public Training findById(Long id) {
        Optional<Training> trainingsOptional = trainingRepository.findById(id);

        if(trainingsOptional.isPresent()) {
            return trainingsOptional.get();
        }
        return null;
    }

    public void deleteTrainings(Long id) {
        trainingRepository.deleteById(id);
    }
}
