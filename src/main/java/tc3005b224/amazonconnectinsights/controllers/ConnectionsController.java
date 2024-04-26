package tc3005b224.amazonconnectinsights.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tc3005b224.amazonconnectinsights.dto.connections.ConnectionDTO;
import tc3005b224.amazonconnectinsights.service.ConnectionsService;

import java.util.List;

@RestController
@RequestMapping("/connections")
public class ConnectionsController {
    @Autowired
    private ConnectionsService connectionsService;

    @GetMapping
    public ResponseEntity<List<ConnectionDTO>> getAllConnection() {
        List<ConnectionDTO> connection = connectionsService.findAll();
        return ResponseEntity.ok(connection);
    }
    @GetMapping("/{connectionId}")
    public ResponseEntity<ConnectionDTO> getConnectionById(@PathVariable Short connectionId) {
        ConnectionDTO connectionDTO = connectionsService.findById(connectionId);
        if (connectionDTO != null) {
            return ResponseEntity.ok(connectionDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ConnectionDTO> createConnection(@RequestBody ConnectionDTO connectionDTO) {
        connectionDTO = connectionsService.createConnection(connectionDTO);
        return new ResponseEntity<>(connectionDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{connectionId}")
    public ResponseEntity<ConnectionDTO> updateConnection(@PathVariable Short connectionId, @RequestBody ConnectionDTO connectionDTO) {
        ConnectionDTO updatedConnectionDTO = connectionsService.updateConnection(connectionId, connectionDTO);
        if (updatedConnectionDTO != null) {
            return ResponseEntity.ok(updatedConnectionDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{connectionId}")
    public ResponseEntity<Void> deleteConnection(@PathVariable Short connectionId) {
        boolean isDeleted = connectionsService.deleteConnection(connectionId);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
