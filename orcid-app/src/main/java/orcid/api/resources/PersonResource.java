package orcid.api.resources;

/**
 * Created by wim033 on 1/12/16.
 */

import orcid.api.core.ActionLog;
import orcid.api.core.Identifier;
import orcid.api.core.Person;
import orcid.api.db.ActionLogDAO;
import orcid.api.db.IdentifierDAO;
import orcid.api.db.PersonDAO;
import orcid.api.views.PersonView;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.UnitOfWork;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import javax.ws.rs.core.Context;

@Path("/seas/people/{personId}")
public class PersonResource {

    @Context
    HttpServletRequest request;

    private final PersonDAO personDAO;
    private final IdentifierDAO idDAO;
    private final ActionLogDAO actionLogDAO;

    public PersonResource(PersonDAO peopleDAO, IdentifierDAO idDAO, ActionLogDAO actionLogDAO) {
        this.personDAO = peopleDAO;
        this.idDAO = idDAO;
        this.actionLogDAO = actionLogDAO;
    }

    @GET
    @Path("/json")
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Person getPerson(@PathParam("personId") String personId) {
        return findSafelyPersonUuid(personId);
    }

    @GET
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    public PersonView getPersonViewFreemarkerUuid(@PathParam("personId") String personId) {
        return new PersonView(PersonView.Template.FREEMARKER, findSafelyPersonUuid(personId), getIdentifiers(personId));
    }

    @DELETE
    @UnitOfWork
    public void removePerson(@PathParam("personId") String personId) {
        Person person = findSafelyPersonUuid(personId);
        personDAO.delete(person);
        actionLogDAO.create(new ActionLog(personId, "system", "Removed person"));
    }

    @Path("/identifiers")
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public List<Identifier> listIdentifiersByPerson(@PathParam("personId") String personId) {
        return idDAO.findAllByPersonUuid(personId);
    }

    @Path("/identifiers/{identId}/state")
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Identifier claimIdentifier(@PathParam("personId") String personId, @PathParam("identId") String identId,
                                      @QueryParam("value") String state) {
        Identifier identifier = findSafelyIdentifierUuid(identId);
        identifier.setState(state);
        actionLogDAO.create(new ActionLog(personId, "system", "Identifier {" + identId + "} state set to: " + state));
        return identifier;
    }

    private Identifier findSafelyIdentifierUuid(String uuid) {
        final Optional<Identifier> identifier = idDAO.findByUuid(uuid);
        if (!identifier.isPresent()) {
            throw new NotFoundException("No such identifier.");
        }
        return identifier.get();
    }

    private Person findSafelyPersonUuid(String uuid) {
        final Optional<Person> person = personDAO.findByUuid(uuid);
        if (!person.isPresent()) {
            throw new NotFoundException("No such user.");
        }
        return person.get();
    }

    private List<Identifier> getIdentifiers(String personId) {
        return idDAO.findAllByPersonUuid(personId);
    }

}

