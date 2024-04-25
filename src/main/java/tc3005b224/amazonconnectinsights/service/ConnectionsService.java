package tc3005b224.amazonconnectinsights.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tc3005b224.amazonconnectinsights.models_sql.Connections;
import tc3005b224.amazonconnectinsights.repository.ConnectionsRepository;

import java.util.Optional;

@Service
@Transactional
public class ConnectionsService {
    @Autowired
    private ConnectionsRepository connectionsRepository;

    public Iterable<Connections> findAll() {
        return connectionsRepository.findAll();
    }

    public Connections savaConnections(Short id, Connections newConnections) {
        return connectionsRepository.save(newConnections);
    }

    public Connections findById(Short id) {
        Optional<Connections> connectionsOptional = connectionsRepository.findById(id);

        if(connectionsOptional.isPresent()) {
            return connectionsOptional.get();
        }
        return null;
    }

    public void deleteConnections(Short id) {
        connectionsRepository.deleteById(id);
    }
}
