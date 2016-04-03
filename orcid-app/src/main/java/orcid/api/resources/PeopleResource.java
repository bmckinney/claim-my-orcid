package orcid.api.resources;

import orcid.api.core.ActionLog;
import orcid.api.core.Person;
import orcid.api.db.ActionLogDAO;
import orcid.api.db.PersonDAO;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/seas/people")
@Produces(MediaType.APPLICATION_JSON)
public class PeopleResource {

    private final PersonDAO peopleDAO;
    private final ActionLogDAO actionLogDAO;

    public PeopleResource(PersonDAO peopleDAO, ActionLogDAO actionLogDAO) {

        this.peopleDAO = peopleDAO;
        this.actionLogDAO = actionLogDAO;
    }

    @POST
    @UnitOfWork
    public Person createPerson(Person person) {
        Person p = peopleDAO.create(person);
        actionLogDAO.create(new ActionLog(p.getUuid(), "system",
                "Created person: " + p.getFirstName() + " " + p.getLastName()));
        return p;
    }

    @GET
    @UnitOfWork
    public List<Person> listPeople() {
        return peopleDAO.findAll();
    }

}
