package tc3005b224.amazonconnectinsights.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tc3005b224.amazonconnectinsights.task.AlertEngine;

@RestController
@RequestMapping("/alert-engine")
public class AlertEngineController {
    @Autowired
    private AlertEngine alertEngineTask;

    @PostMapping("/start")
    public String startTask() {
        alertEngineTask.setRunning(true);
        return "Task started";
    }

    @PostMapping("/stop")
    public String stopTask() {
        alertEngineTask.setRunning(false);
        return "Task stopped";
    }

    @GetMapping("/status")
    public String getTaskStatus() {
        return alertEngineTask.isRunning() ? "Task is running" : "Task is not running";
    }
}
