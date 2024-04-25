package tc3005b224.amazonconnectinsights.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tc3005b224.amazonconnectinsights.models_sql.Trainings;
import tc3005b224.amazonconnectinsights.repository.TrainingsRepository;

import java.util.Optional;

@Service
@Transactional
public class TrainingsService {
    @Autowired
    private TrainingsRepository trainingsRepository;

    public Iterable<Trainings> findAll() {
        return trainingsRepository.findAll();
    }

    public Trainings saveTrainings(Long id, Trainings newTrainings) {
        return trainingsRepository.save(newTrainings);
    }

    public Trainings findById(Long id) {
        Optional<Trainings> trainingsOptional = trainingsRepository.findById(id);

        if(trainingsOptional.isPresent()) {
            return trainingsOptional.get();
        }
        return null;
    }

    public void deleteTrainings(Long id) {
        trainingsRepository.deleteById(id);
    }
}
