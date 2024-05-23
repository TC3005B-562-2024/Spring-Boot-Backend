package tc3005b224.amazonconnectinsights.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import tc3005b224.amazonconnectinsights.service.AlertEngineService;

@Component
public class AlertEngineTask {
    @Autowired
    private AlertEngineService alertEngineService;

    private volatile boolean running = true;

    @Scheduled(fixedDelay = 1000)
    public void mainTask() {
        if (running) {
            alertEngineService.generateAlerts("token");
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }
}
