package tc3005b224.amazonconnectinsights.models_sql;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import tc3005b224.amazonconnectinsights.dto.connections.ConnectionDTO;

@Entity
@Table(name = "connection")
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identifier")
    private int identifier;

    @Column(name = "uid")
    private String uid;

    @Column(name = "supervisor")
    private String supervisor;

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
        this.uid = connectionDTO.getUid();
        this.supervisor = connectionDTO.getSupervisor();
        this.denomination = connectionDTO.getDenomination();
        this.description = connectionDTO.getDescription();
        this.dateJoined = new Date();
        this.dateUpdated = new Date();
        this.isActive = connectionDTO.getActive();
    }

    public void updateFromDTO(ConnectionDTO connectionDTO){
        this.dateUpdated = new Date();

        if(connectionDTO.getUid() != null){
            this.uid = connectionDTO.getUid();
        }

        if(connectionDTO.getSupervisor() != null){
            this.supervisor = connectionDTO.getSupervisor();
        }

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
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



