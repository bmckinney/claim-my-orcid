package orcid.api.core;

/**
 * Created by wim033 on 1/12/16.
 */

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "people")
@NamedQueries({
    @NamedQuery(
        name = "orcid.api.core.Person.findAll",
        query = "SELECT p FROM Person p"
    ),
    @NamedQuery(
        name = "orcid.api.core.Person.findByUuid",
        query = "SELECT p FROM Person p where p.uuid = :uuid"
    ),
    @NamedQuery(
            name = "orcid.api.core.Person.findAllByEmail",
            query = "SELECT p FROM Person p where p.email = :email"
    )
})
public class Person implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uuid", nullable = true)
    private String uuid = UUID.randomUUID().toString();

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name", nullable = true)
    private String middleName;

    @Column(name = "full_name", nullable = true)
    private String fullName;

    @Column(name = "school", nullable = true)
    private String school;

    @Column(name = "department", nullable = true)
    private String department;

    @Column(name = "job_title", nullable = true)
    private String jobTitle;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "picture_url", nullable = true)
    private String pictureUrl;

    @Column(name = "orcid_status", nullable = true)
    private String orcidStatus;


    public Person() {
    }

    public Person(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getOrcidStatus() {
        return orcidStatus;
    }

    public void setOrcidStatus(String orcidStatus) {
        this.orcidStatus = orcidStatus;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }

        final Person that = (Person) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.firstName, that.firstName) &&
                Objects.equals(this.lastName, that.lastName) &&
                Objects.equals(this.email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email);
    }

}
