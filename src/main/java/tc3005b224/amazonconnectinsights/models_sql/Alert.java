package tc3005b224.amazonconnectinsights.models_sql;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertDTO;

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

    @Column(name = "intervene_contact")
    private String interveneContact;

    @Column(name = "intervene_agent")
    private String interveneAgent;

    @Column(name = "original_routing_profile")
    private String originalRoutingProfile;

    @Column(name = "destination_routing_profile")
    private String destinationRoutingProfile;

    @Column(name = "transfered_agent")
    private String transferedAgent;

    @Column(name = "resource")
    private String resource;

    @Column(name = "date_registered")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRegistered;

    @Column(name = "date_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdated;

    @Column(name = "is_solved")
    private Boolean solved;

    @Column(name = "date_training_completed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTrainingCompleted;

    @Column(name = "has_training", insertable = false, updatable = false)
    private Boolean hasTraining;

    @Column(name = "is_training_completed", insertable = false, updatable = false)
    private Boolean trainingCompleted;

    // Constructors, getters, and setters
    public Alert() {
        ;
    }
    public Alert(AlertDTO alertDTO, Connection connection, Insight insight, Training training) {
        this.connection = connection;
        this.insight = insight;
        this.training = training;
        this.resource = alertDTO.getResource();
        this.dateRegistered = new Date();
        this.dateUpdated = new Date();
        this.solved = alertDTO.isSolved();
        this.trainingCompleted = training != null ? alertDTO.getTrainingCompleted() : null;
    }

    public void updateFromDTO(AlertDTO alertDTO, Connection connection, Insight insight, Training training){
        this.dateUpdated = new Date();

        if(connection != null){
            this.connection = connection;
        }

        if(insight != null){
            this.insight = insight;
        }

        if(training != null){
            this.training = training;
        }

        if(alertDTO.getResource() != null){
            this.resource = alertDTO.getResource();
        }

        if(alertDTO.isSolved() != null){
            this.solved = alertDTO.isSolved();
        }

        if(alertDTO.isSolved() != null){
            this.solved = alertDTO.isSolved();
        }

        if(alertDTO.getTrainingCompleted() != null){
            this.trainingCompleted = alertDTO.getTrainingCompleted();

            if(this.trainingCompleted){
                this.dateTrainingCompleted = new Date();
            }
        }
    }

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

    public Boolean getSolved() {
        return solved;
    }

    public void setSolved(Boolean solved) {
        this.solved = solved;
    }

    public Date getDateTrainingCompleted() {
        return dateTrainingCompleted;
    }

    public void setDateTrainingCompleted(Date dateTrainingCompleted) {
        this.dateTrainingCompleted = dateTrainingCompleted;
    }

    public Boolean getHasTraining() {
        return hasTraining;
    }

    public void setHasTraining(Boolean hasTraining) {
        this.hasTraining = hasTraining;
    }

    public Boolean getTrainingCompleted() {
        return trainingCompleted;
    }

    public void setTrainingCompleted(Boolean trainingCompleted) {
        this.trainingCompleted = trainingCompleted;
    }
    public String getInterveneContact() {
        return interveneContact;
    }
    public void setInterveneContact(String interveneContact) {
        this.interveneContact = interveneContact;
    }
    public String getInterveneAgent() {
        return interveneAgent;
    }
    public void setInterveneAgent(String interveneAgent) {
        this.interveneAgent = interveneAgent;
    }
    public String getOriginalRoutingProfile() {
        return originalRoutingProfile;
    }
    public void setOriginalRoutingProfile(String originalRoutingProfile) {
        this.originalRoutingProfile = originalRoutingProfile;
    }
    public String getDestinationRoutingProfile() {
        return destinationRoutingProfile;
    }
    public void setDestinationRoutingProfile(String destinationRoutingProfile) {
        this.destinationRoutingProfile = destinationRoutingProfile;
    }
    public String getTransferedAgent() {
        return transferedAgent;
    }
    public void setTransferedAgent(String transferedAgent) {
        this.transferedAgent = transferedAgent;
    }
}

