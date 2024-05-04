package tc3005b224.amazonconnectinsights.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tc3005b224.amazonconnectinsights.dto.connections.ConnectionDTO;
import tc3005b224.amazonconnectinsights.models_sql.Connection;
import tc3005b224.amazonconnectinsights.repository.ConnectionRepository;

@Service
@Transactional
public class ConnectionsService {
    @Autowired
    private ConnectionRepository connectionRepository;

    // Service that returns an Iterable of all the connections in the database.
    public Iterable<Connection> findAll() {
        return connectionRepository.findAll();
    }

    // Service that looks for a Connection, given its id. If the Connection is not found, returns a null.
    public Connection findById(Short id) {
        Optional<Connection> connectionsOptional = connectionRepository.findById(id);

        if(connectionsOptional.isPresent()) {
            return connectionsOptional.get();
        }
        return null;
    }

    // Service that instantiates a category, given a CategoryDTO and stores it in the DB.
    public Connection createConnection(ConnectionDTO connectionDTO) {
        Connection connection = new Connection(connectionDTO);
        return connectionRepository.save(connection);
    }

    // Service that updates a category considering all the non-null attributes of CategoryDTO.
    public Connection updateConnection(Short id, ConnectionDTO connectionDTO) {
        Optional<Connection> connectionOptional = connectionRepository.findById(id);
        if (connectionOptional.isPresent()) {
            connectionOptional.get().updateFromDTO(connectionDTO);
            return connectionRepository.save(connectionOptional.get());
        }
        return null;
    }

    // Service that deletes a Category given its id. If the action is successful returns a true, else a false.
    public boolean deleteConnection(Short id) {
        if (connectionRepository.existsById(id)) {
            connectionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
