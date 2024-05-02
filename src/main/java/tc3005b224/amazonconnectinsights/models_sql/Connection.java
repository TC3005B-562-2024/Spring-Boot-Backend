package tc3005b224.amazonconnectinsights.models_sql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import tc3005b224.amazonconnectinsights.dto.connections.ConnectionDTO;
import tc3005b224.amazonconnectinsights.dto.insights.InsightDTO;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "connection")
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identifier")
    private int identifier;

    @Column(name = "denomination", nullable = false, length = 100)
    private String denomination;

    @Column(name = "description", nullable = false, length = 8)
    private String description;

    @Column(name = "date_joined", nullable = false)
    private Date dateJoined;

    @Column(name = "date_updated", nullable = false)
    private Date dateUpdated;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @OneToMany(mappedBy="connection", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Alert> alerts;

    // Constructors, getters and setters
    public Connection(){
        ;
    }
    public Connection(ConnectionDTO connectionDTO) {
        this.denomination = connectionDTO.getDenomination();
        this.description = connectionDTO.getDescription();
        this.dateJoined = new Date();
        this.dateUpdated = new Date();
        this.isActive = connectionDTO.getActive();
    }

    public void updateFromDTO(ConnectionDTO connectionDTO){
        this.dateUpdated = new Date();

        if(connectionDTO.getDenomination() != null){
            this.denomination = connectionDTO.getDenomination();
        }

        if(connectionDTO.getDescription() != null){
            this.description = connectionDTO.getDescription();
        }

        if(connectionDTO.getActive() != null){
            this.isActive = connectionDTO.getActive();
        }
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }
}



