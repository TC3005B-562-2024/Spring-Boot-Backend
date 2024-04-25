package tc3005b224.amazonconnectinsights.models_sql;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Alrts")
public class Alerts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short identifier;

    @ManyToOne
    @JoinColumn(name = "Connection_identifier", nullable = false)
    private Connections connections;

    @ManyToOne
    @JoinColumn(name = "Insight_identifier", nullable = false)
    private Insights insights;

    @Column(name = "is_solved", nullable = false)
    private Boolean isSolved = false;

    @Column(name = "date_registered", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dateRegistered = LocalDateTime.now();

    @Column(name = "date_updated", nullable = false, columnDefinition = "TIMESTAMO DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dateUpdated = LocalDateTime.now();

    @Column(name = "resource_arn", nullable = false)
    private String resourceArn;

    public Short getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Short identifier) {
        this.identifier = identifier;
    }

    public Connections getConnections() {
        return connections;
    }

    public void setConnections(Connections connections) {
        this.connections = connections;
    }

    public Insights getInsights() {
        return insights;
    }

    public void setInsights(Insights insights) {
        this.insights = insights;
    }

    public Boolean getSolved() {
        return isSolved;
    }

    public void setSolved(Boolean solved) {
        isSolved = solved;
    }

    public LocalDateTime getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(LocalDateTime dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getResourceArn() {
        return resourceArn;
    }

    public void setResourceArn(String resourceArn) {
        this.resourceArn = resourceArn;
    }
}
