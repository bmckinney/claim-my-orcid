package orcid.api.resources;

import orcid.api.core.Person;
import orcid.api.db.ActionLogDAO;
import orcid.api.db.PersonDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link orcid.api.resources.PeopleResource}.
 */
@RunWith(MockitoJUnitRunner.class)
public class PeopleResourceTest {

    private static final PersonDAO personDAO = mock(PersonDAO.class);
    private static final ActionLogDAO actionLogDAO = mock(ActionLogDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new PeopleResource(personDAO, actionLogDAO))
            .build();
    @Captor
    private ArgumentCaptor<Person> personCaptor;
    private Person person;

    @Before
    public void setUp() {
        person = new Person();
        person.setFirstName("Homer");
        person.setLastName("Simpson");
        person.setEmail("homer@simpsons.com");
        person.setFullName("Homer Simpson");
        person.setJobTitle("Nuclear Engineer");
    }

    @After
    public void tearDown() {
        reset(personDAO);
    }

    @Test
    public void createPerson() throws JsonProcessingException {
        when(personDAO.create(any(Person.class))).thenReturn(person);
        final Response response = resources.client().target("/people")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(person, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        verify(personDAO).create(personCaptor.capture());
        assertThat(personCaptor.getValue()).isEqualTo(person);
    }

    @Test
    public void listPeople() throws Exception {
        final ImmutableList<Person> people = ImmutableList.of(person);
        when(personDAO.findAll()).thenReturn(people);

        final List<Person> response = resources.client().target("/people")
                .request().get(new GenericType<List<Person>>() {});

        verify(personDAO).findAll();
        assertThat(response).containsAll(people);
    }
}
