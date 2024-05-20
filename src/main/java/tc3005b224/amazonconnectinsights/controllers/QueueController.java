package tc3005b224.amazonconnectinsights.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tc3005b224.amazonconnectinsights.dto.queue.QueueCardDTO;
import tc3005b224.amazonconnectinsights.service.QueueService;

@RestController
@RequestMapping("/queues")
public class QueueController {
    @Autowired
    private QueueService queueService;

    @GetMapping
    public ResponseEntity<Iterable<QueueCardDTO>> getAllQueues() {
        try {
            Iterable<QueueCardDTO> queueCards = queueService.findAll("1", "");
            return ResponseEntity.ok(queueCards);
        }catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.notFound().build();
        }
    }
}
