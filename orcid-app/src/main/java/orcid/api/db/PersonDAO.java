package orcid.api.db;

/**
 * Created by wim033 on 1/12/16.
 */

import orcid.api.core.Person;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class PersonDAO extends AbstractDAO<Person> {
    public PersonDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Person> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public Person create(Person person) {
        return persist(person);
    }

    public void update (Person person) {currentSession().update(person); };

    public void delete(Person person) {
        currentSession().delete(person);
    }

    public List<Person> findAll() {
        return list(namedQuery("orcid.api.core.Person.findAll"));
    }

    public Optional<Person> findByUuid(String uuid) {
        Person foundPerson = (Person) namedQuery("orcid.api.core.Person.findByUuid")
                .setParameter("uuid", uuid)
                .uniqueResult();
        return Optional.fromNullable(foundPerson);
    }

    public List<Person> findByEmail(String email) {

        return list(namedQuery("orcid.api.core.Person.findAllByEmail")
                .setParameter("email", email));

    }
}
