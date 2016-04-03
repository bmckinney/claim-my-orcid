package orcid.api.resources;

import orcid.api.core.Person;
import orcid.api.db.ActionLogDAO;
import orcid.api.db.IdentifierDAO;
import orcid.api.db.PersonDAO;
import com.google.common.base.Optional;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link orcid.api.resources.PersonResource}.
 */
public class PersonResourceTest {

    private static final PersonDAO dao = mock(PersonDAO.class);
    private static final IdentifierDAO idDao = mock(IdentifierDAO.class);
    private static final ActionLogDAO actionLogDAO = mock(ActionLogDAO.class);

    @ClassRule
    public static final ResourceTestRule RULE = ResourceTestRule.builder()
            .addResource(new PersonResource(dao, idDao, actionLogDAO))
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .build();
    private Person person;

    @Before
    public void setup() {
        person = new Person();
        person.setId(1L);
        person.setUuid("0223b6c6-6c5b-4a37-a6f4-ee5a4defc31a");
    }

    @After
    public void tearDown() {
        reset(dao);
    }

    @Test
    public void getPersonSuccess() {
        when(dao.findByUuid("0223b6c6-6c5b-4a37-a6f4-ee5a4defc31a")).thenReturn(Optional.of(person));
        Person found = RULE.getJerseyTest().target("/people/0223b6c6-6c5b-4a37-a6f4-ee5a4defc31a/json").request().get(Person.class);

        assertThat(found.getId()).isEqualTo(person.getId());
    }

    @Test
    public void getPersonNotFound() {
        when(dao.findByUuid("fc54fe75-56df-4980-af5d-5cffdf00d30e")).thenReturn(Optional.<Person>absent());
        final Response response = RULE.getJerseyTest().target("/people/fc54fe75-56df-4980-af5d-5cffdf00d30e").request().get();

        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        verify(dao).findByUuid("fc54fe75-56df-4980-af5d-5cffdf00d30e");
    }
}

