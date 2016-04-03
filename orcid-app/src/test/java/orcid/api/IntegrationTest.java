package orcid.api;

import orcid.api.app.OrcidApplication;
import orcid.api.app.OrcidConfiguration;
import orcid.api.core.Identifier;
import orcid.api.core.Person;
import orcid.api.core.Saying;
import com.google.common.base.Optional;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by wim033 on 1/21/16.
 */
public class IntegrationTest {

    private static final String TMP_FILE = createTempFile();
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-orcid.yml");

    @ClassRule
    public static final DropwizardAppRule<OrcidConfiguration> RULE = new DropwizardAppRule<>(
            OrcidApplication.class, CONFIG_PATH,
            ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));

    private Client client;

    @BeforeClass
    public static void migrateDb() throws Exception {
        RULE.getApplication().run("db", "migrate", CONFIG_PATH);
    }

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    private static String createTempFile() {
        try {
            return File.createTempFile("test-example", null).getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

//    @Test
//    public void testHelloWorld() throws Exception {
//        final Optional<String> name = Optional.fromNullable("Dr. IntegrationTest");
//        final Saying saying = client.target("http://localhost:" + RULE.getLocalPort() + "/hello-world")
//                .queryParam("name", name.get())
//                .request()
//                .get(Saying.class);
//        assertThat(saying.getContent()).isEqualTo(RULE.getConfiguration().buildTemplate().render(name));
//    }

    @Test
    public void testPostPerson() throws Exception {
        final Person person = new Person("Homer", "Simpson", "homer@test.com");
        final Person newPerson = client.target("http://localhost:" + RULE.getLocalPort() + "/people")
                .request()
                .post(Entity.entity(person, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Person.class);
        assertThat(newPerson.getId()).isNotNull();
        assertThat(newPerson.getUuid()).isNotNull();
        assertThat(newPerson.getFirstName()).isEqualTo(person.getFirstName());
        assertThat(newPerson.getLastName()).isEqualTo(person.getLastName());
        assertThat(newPerson.getEmail()).isEqualTo(person.getEmail());
    }

    @Test
    public void testPostIdentifier() throws Exception {
        final Identifier id = new Identifier("scopus", "6603019922", "70c040d7-7244-4ac4-a8a1-cdb196b6d032");
        final Identifier newId = client.target("http://localhost:" + RULE.getLocalPort() + "/identifiers")
                .request()
                .post(Entity.entity(id, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Identifier.class);
        assertThat(newId.getId()).isNotNull();
        assertThat(newId.getPersonUuid()).isNotNull();
        assertThat(newId.getType()).isEqualTo(id.getType());
        assertThat(newId.getValue()).isEqualTo(id.getValue());
    }

    @Test
    public void testIdentifierStatus() throws Exception {

        final Person person = new Person("Homer", "Simpson", "homer@test.com");
        final Person newPerson = client.target("http://localhost:" + RULE.getLocalPort() + "/people")
                .request()
                .post(Entity.entity(person, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Person.class);

        final Identifier id = new Identifier("scopus", "6603019922", newPerson.getUuid());
        final Identifier newId = client.target("http://localhost:" + RULE.getLocalPort() + "/identifiers")
                .request()
                .post(Entity.entity(id, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Identifier.class);

        final Identifier statusId = client.target("http://localhost:" + RULE.getLocalPort() + "/people/" +
                        newPerson.getUuid() + "/identifiers/" +
                newId.getUuid() + "/state")
                .queryParam("value", "claimed")
                .request()
                .get()
                .readEntity(Identifier.class);

        assertThat(statusId.getState().equalsIgnoreCase("claimed"));
    }


}
