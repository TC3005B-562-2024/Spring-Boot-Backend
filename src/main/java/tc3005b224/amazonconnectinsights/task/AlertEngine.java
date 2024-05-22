package tc3005b224.amazonconnectinsights.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AlertEngine {
    private volatile boolean running = true;

    @Scheduled(fixedRate = 1000)
    public void mainTask() {
        if (running) {
            System.out.println("Task executed at " + System.currentTimeMillis());
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }
}
