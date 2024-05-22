package tc3005b224.amazonconnectinsights.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertEngineService {
    @Autowired
    private MetricService metricService;

    public void sendAlert(String message) {
        try {
            metricService.getMetricsById("token", "AGENT", "arn:aws:connect:us-east-1:674530197385:instance/7c78bd60-4a9f-40e5-b461-b7a0dfaad848/agent/6887b106-f684-485e-9c47-a6b1e16cdd21");            
        } catch (Exception e) {
            System.out.println("Sending alert: " + message);
        }
    }
}
