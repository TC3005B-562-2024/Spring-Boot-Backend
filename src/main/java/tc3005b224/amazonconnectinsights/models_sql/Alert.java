package tc3005b224.amazonconnectinsights.models_sql;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "alert")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identifier")
    private Long identifier;

    @ManyToOne
    @JoinColumn(name = "connection_identifier", nullable = false)
    private Connection connection;

    @ManyToOne
    @JoinColumn(name = "insight_identifier", nullable = false)
    private Insight insight;

    @ManyToOne
    @JoinColumn(name = "training_identifier", nullable = true)
    private Training training;

    @Column(name = "is_solved", nullable = false)
    private Boolean isSolved = false;

    @Column(name = "date_registered", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date dateRegistered;

    @Column(name = "date_updated", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date dateUpdated;

    @Column(name = "resource", nullable = false, length = 100)
    private String resourceArn;

    @Column(name = "date_training_completed", nullable = true, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date dateTrainingCompleted;

    public Date getDateTrainingCompleted() {
        return dateTrainingCompleted;
    }

    public void setDateTrainingCompleted(Date dateTrainingCompleted) {
        this.dateTrainingCompleted = dateTrainingCompleted;
    }

    // Getters & Setters
    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Insight getInsight() {
        return insight;
    }

    public void setInsight(Insight insight) {
        this.insight = insight;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public Boolean getSolved() {
        return isSolved;
    }

    public void setSolved(Boolean solved) {
        isSolved = solved;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getResourceArn() {
        return resourceArn;
    }

    public void setResourceArn(String resourceArn) {
        this.resourceArn = resourceArn;
    }
}
