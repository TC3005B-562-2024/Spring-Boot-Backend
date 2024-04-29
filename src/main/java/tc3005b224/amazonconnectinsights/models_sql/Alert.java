package tc3005b224.amazonconnectinsights.models_sql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "alert")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identifier")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "connection_identifier")
    private Connection connection;

    @ManyToOne
    @JoinColumn(name = "insight_identifier")
    private Insight insight;

    @ManyToOne
    @JoinColumn(name = "training_identifier")
    private Training training;

    @Column(name = "resource")
    private String resource;

    @Column(name = "date_registered")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRegistered;

    @Column(name = "date_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdated;

    @Column(name = "is_solved")
    private boolean solved;

    @Column(name = "date_training_completed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTrainingCompleted;

    @Column(name = "has_training", insertable = false, updatable = false)
    private boolean hasTraining;

    @Column(name = "is_training_completed", insertable = false, updatable = false)
    private boolean trainingCompleted;

    // Constructors, getters, and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
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

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public Date getDateTrainingCompleted() {
        return dateTrainingCompleted;
    }

    public void setDateTrainingCompleted(Date dateTrainingCompleted) {
        this.dateTrainingCompleted = dateTrainingCompleted;
    }

    public boolean isHasTraining() {
        return hasTraining;
    }

    public void setHasTraining(boolean hasTraining) {
        this.hasTraining = hasTraining;
    }

    public boolean isTrainingCompleted() {
        return trainingCompleted;
    }

    public void setTrainingCompleted(boolean trainingCompleted) {
        this.trainingCompleted = trainingCompleted;
    }
}

