package tc3005b224.amazonconnectinsights.models_sql;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Connections")
public class ConnectionsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identifier")
    private Short id;

    @Column(name = "denomination", nullable = false, length = 50)
    private String denomination;

    @Column(name = "description", nullable = true, length = 100)
    private String description;

    @Column(name = "key", nullable = false, columnDefinition = "TINYTEXT")
    private String key;

    @Column(name = "date_joined", nullable = false)
    private Date dateJoined;

    @Column(name = "date_updated", nullable = false)
    private Date dateUpdated;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
}



