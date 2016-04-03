package orcid.api.core;

/**
 * Created by wim033 on 1/12/16.
 */

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "actionlog")
@NamedQueries({
        @NamedQuery(
                name = "orcid.api.core.ActionLog.findAll",
                query = "SELECT l FROM ActionLog l"
        ),
        @NamedQuery(
                name = "orcid.api.core.ActionLog.findAllByPersonUuid",
                query = "SELECT l FROM ActionLog l where l.personUuid = :uuid"
        )
})
public class ActionLog implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "person_uuid", nullable = false)
    private String personUuid;

    @Column(name = "user_uuid", nullable = false)
    private String userUuid;

    @Column(name = "entry", nullable = false)
    private String entry;

    @Column(name = "action_date", nullable = true)
    private Date actionDate;

    public ActionLog() {
    }

    public ActionLog(String person, String user, String entry) {
        this.personUuid = person;
        this.userUuid = user;
        this.entry = entry;
        java.util.Date date= new java.util.Date();
        this.actionDate = new Timestamp(date.getTime());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPersonUuid() {
        return personUuid;
    }

    public void setPersonUuid(String personUuid) {
        this.personUuid = personUuid;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }

        final ActionLog that = (ActionLog) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.entry, that.entry) &&
                Objects.equals(this.actionDate, that.actionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entry);
    }

}
