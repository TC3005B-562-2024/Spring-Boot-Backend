package tc3005b224.amazonconnectinsights.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import tc3005b224.amazonconnectinsights.task.AlertEngineTask;

@RestController
@RequestMapping("/alert-engine")
public class AlertEngineController {
    @Autowired
    private AlertEngineTask alertEngineTask;

    @Operation(summary = "Start the alert engine task", responses = {
            @ApiResponse(responseCode = "200", description = "Task started", content = {
                    @Content(schema = @Schema(implementation = String.class)) }) })
    @PostMapping("/start")
    public String startTask() {
        alertEngineTask.setRunning(true);
        return "Task started";
    }

    @Operation(summary = "Stop the alert engine task", responses = {
        @ApiResponse(responseCode = "200", description = "Task started", content = {
                @Content(schema = @Schema(implementation = String.class)) }) })
    @PostMapping("/stop")
    public String stopTask() {
        alertEngineTask.setRunning(false);
        return "Task stopped";
    }

    @Operation(summary = "Get the status of the alert engine task", responses = {
        @ApiResponse(responseCode = "200", description = "Task started", content = {
                @Content(schema = @Schema(implementation = String.class)) }) })
    @GetMapping("/status")
    public String getTaskStatus() {
        return alertEngineTask.isRunning() ? "Task is running" : "Task is not running";
    }
}
