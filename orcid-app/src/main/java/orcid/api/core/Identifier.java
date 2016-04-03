package orcid.api.core;

/**
 * Created by wim033 on 1/12/16.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "identifiers")
@NamedQueries({
        @NamedQuery(
                name = "orcid.api.core.Identifier.findAll",
                query = "SELECT i FROM Identifier i"
        ),
        @NamedQuery(
                name = "orcid.api.core.Identifier.findByUuid",
                query = "SELECT i FROM Identifier i where i.uuid = :uuid"
        ),
        @NamedQuery(
                name = "orcid.api.core.Identifier.findAllByPersonUuid",
                query = "SELECT i FROM Identifier i where i.personUuid = :uuid"
        )
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Identifier implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uuid", nullable = true)
    private String uuid = UUID.randomUUID().toString();

    @Column(name = "person_uuid", nullable = false)
    private String personUuid;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "value", nullable = true)
    private String value;

    @Column(name = "label", nullable = true)
    private String label;

    @Column(name = "url", nullable = true)
    private String url;

    @Column(name = "state_changed_date", nullable = true)
    private Date stateChangedDate;

    @Column(name = "state_changed_by", nullable = true)
    private String stateChangedBy;

    @Column(name = "state", nullable = false)
    private String state = "unclaimed";

    @Column(name = "orcid_access_token", nullable = true)
    private String orcidAccessToken;

    @Column(name = "orcid_auth_code", nullable = true)
    private String orcidAuthCode;


    public Identifier() {
    }

    public Identifier(String type, String value, String personUuid) {
        this.type = type;
        this.value = value;
        this.personUuid = personUuid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPersonUuid() {
        return personUuid;
    }

    public void setPersonUuid(String personUuid) {
        this.personUuid = personUuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getStateChangedDate() {
        return stateChangedDate;
    }

    public void setStateChangedDate(Date stateChangedDate) {
        this.stateChangedDate = stateChangedDate;
    }

    public String getStateChangedBy() {
        return stateChangedBy;
    }

    public void setStateChangedBy(String stateChangedBy) {
        this.stateChangedBy = stateChangedBy;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrcidAccessToken() {
        return orcidAccessToken;
    }

    public void setOrcidAccessToken(String orcidAccessToken) {
        this.orcidAccessToken = orcidAccessToken;
    }

    public String getOrcidAuthCode() {
        return orcidAuthCode;
    }

    public void setOrcidAuthCode(String orcidAuthCode) {
        this.orcidAuthCode = orcidAuthCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }

        final Identifier that = (Identifier) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.type, that.type) &&
                Objects.equals(this.value, that.value) &&
                Objects.equals(this.personUuid, that.personUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, value, personUuid);
    }

}
