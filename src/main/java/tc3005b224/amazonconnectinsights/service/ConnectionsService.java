package tc3005b224.amazonconnectinsights.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tc3005b224.amazonconnectinsights.models_sql.Connection;
import tc3005b224.amazonconnectinsights.repository.ConnectionRepository;

import java.util.Optional;

@Service
@Transactional
public class ConnectionsService {
    @Autowired
    private ConnectionRepository connectionRepository;

    public Iterable<Connection> findAll() {
        return connectionRepository.findAll();
    }

    public Connection savaConnections(Short id, Connection newConnection) {
        return connectionRepository.save(newConnection);
    }

    public Connection findById(Short id) {
        Optional<Connection> connectionsOptional = connectionRepository.findById(id);

        if(connectionsOptional.isPresent()) {
            return connectionsOptional.get();
        }
        return null;
    }

    public void deleteConnections(Short id) {
        connectionRepository.deleteById(id);
    }
}
