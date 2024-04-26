package tc3005b224.amazonconnectinsights.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tc3005b224.amazonconnectinsights.dto.connections.ConnectionDTO;
import tc3005b224.amazonconnectinsights.models_sql.Connection;
import tc3005b224.amazonconnectinsights.repository.ConnectionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConnectionsService {
    @Autowired
    private ConnectionRepository connectionRepository;


    public List<ConnectionDTO> findAll() {
        List<Connection> connections = (List<Connection>) connectionRepository.findAll();
        return connections.stream().map(this::toConnectionDTO).collect(Collectors.toList());
    }

    public ConnectionDTO findById(Short id) {
        Optional<Connection> connectionOptional = connectionRepository.findById(id);
        if (connectionOptional.isPresent()) {
            return toConnectionDTO(connectionOptional.get());
        }
        return null;
    }

    public ConnectionDTO createConnection(ConnectionDTO connectionDTO) {
        Connection connection = toConnection(connectionDTO);
        connection = connectionRepository.save(connection);
        return toConnectionDTO(connection);
    }

    public ConnectionDTO updateConnection(Short id, ConnectionDTO connectionDTO) {
        Optional<Connection> connectionOptional = connectionRepository.findById(id);
        if (connectionOptional.isPresent()) {
            Connection connection = connectionOptional.get();
            connection.setDenomination(connectionDTO.getDenomination());
            connection.setDescription(connectionDTO.getDescription());
            connection.setDateJoined(connectionDTO.getDateJoined());
            connection.setDateUpdated(connectionDTO.getDateUpdated());
            connection.setActive(connectionDTO.getIsActive());
            return toConnectionDTO(connection);
        }
        return null;
    }

    public boolean deleteConnection(Short id) {
        if (connectionRepository.existsById(id)) {
            connectionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Connection toConnection(ConnectionDTO dto) {
        Connection connection = new Connection();
        connection.setDenomination(dto.getDenomination());
        connection.setDescription(dto.getDescription());
        connection.setDateJoined(dto.getDateJoined());
        connection.setDateUpdated(dto.getDateUpdated());
        if (dto.getIsActive() != null) {
            connection.setActive(dto.getIsActive());
        }
        return connection;
    }
    private ConnectionDTO toConnectionDTO(Connection connection) {
        ConnectionDTO dto = new ConnectionDTO();
        dto.setIdentifier(connection.getIdentifier());
        dto.setDenomination(connection.getDenomination());
        dto.setDescription(connection.getDescription());
        dto.setDateJoined(connection.getDateJoined());
        dto.setDateUpdated(connection.getDateUpdated());
        dto.setIsActive(connection.isActive());
        return dto;
    }


}


