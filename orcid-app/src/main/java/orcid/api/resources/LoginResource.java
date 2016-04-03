package orcid.api.resources;

/**
 * Created by wim033 on 1/12/16.
 */

import com.google.common.base.Optional;
import com.sun.org.apache.xpath.internal.operations.Or;
import edu.harvard.hul.ois.access.HPCEncryptedCookie;
import orcid.api.app.OrcidConfiguration;
import orcid.api.auth.ams.AmsConfiguration;
import orcid.api.auth.ams.utils.LdapUtils;
import orcid.api.auth.ams.utils.PinUtils;
import orcid.api.core.ActionLog;
import orcid.api.core.Identifier;
import orcid.api.core.Person;
import orcid.api.db.ActionLogDAO;
import orcid.api.db.IdentifierDAO;
import orcid.api.db.PersonDAO;
import orcid.api.views.LoginView;
import io.dropwizard.hibernate.UnitOfWork;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Date;
import java.util.List;

@Path("/seas/login")
public class LoginResource {

    @Context
    HttpServletRequest request;

    private final PersonDAO personDAO;
    private final IdentifierDAO identifierDAO;
    private final ActionLogDAO actionLogDAO;
    private final AmsConfiguration amsConfig;
    private final OrcidConfiguration orcidConfig;

    public LoginResource(PersonDAO personDAO, IdentifierDAO identifierDAO, ActionLogDAO actionLogDAO,
                         AmsConfiguration amsConfig, OrcidConfiguration orcidConfig) {
        this.personDAO = personDAO;
        this.identifierDAO = identifierDAO;
        this.actionLogDAO = actionLogDAO;
        this.amsConfig = amsConfig;
        this.orcidConfig = orcidConfig;
    }

    @GET
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    public Object doLogin(@Context HttpHeaders headers) {

        Cookie encryptCookie = headers.getCookies().get(amsConfig.getEncryptedCookie());

        // parse HUL personal cookie
        HPCEncryptedCookie hpc = null;
        String  keyPath = amsConfig.getKeyPath();
        try {

            if (encryptCookie != null) {
                hpc = HPCEncryptedCookie.parseCookie(encryptCookie.getValue(), keyPath);
                if (hpc.getEmail() != null && hpc.getFirstName() != null && hpc.getLastName() != null) {

                    List<Person> foundPerson = personDAO.findByEmail(hpc.getEmail());

                    // does this person already exist?
                    if (foundPerson.size() > 0) {

                        actionLogDAO.create(new ActionLog(foundPerson.get(0).getUuid(), foundPerson.get(0).getEmail(),
                                "Logged in to existing profile"));

                        URI uri = UriBuilder.fromUri("/seas/people/" + foundPerson.get(0).getUuid()).build();
                        return Response.seeOther(uri).build();

                    // no, create a new person record
                    } else {

                        Person person = new Person(hpc.getFirstName(), hpc.getLastName(), hpc.getEmail());
                        String uuid = person.getUuid();

                        // additional LDAP properties
                        if (hpc.getHuid() != null) {
                            SearchResult sr = LdapUtils.getLdapResults(hpc.getHuid(), amsConfig);
                            if (null != sr) {
                                try {

                                    String dashAuthorId = PinUtils.makeNetid(hpc.getHuid(), amsConfig.getSecret());

                                    Identifier ldapId = new Identifier("ldap", "", uuid);
                                    Identifier dashId = new Identifier("dash", dashAuthorId, uuid);

                                    Attributes attrs = sr.getAttributes();
                                    for (NamingEnumeration ae = attrs.getAll(); ae.hasMore(); ) {
                                        Attribute attr = (Attribute) ae.next();

                                        System.out.println(attr.getID() + " = " + attr.get(0).toString());

                                        // fullname
                                        if (attr.getID().equalsIgnoreCase("displayName")) {
                                            person.setFullName(attr.get(0).toString());
                                        }
                                        // title
                                        if (attr.getID().equalsIgnoreCase("title")) {
                                            person.setJobTitle(attr.get(0).toString());
                                        }
                                        // department
                                        if (attr.getID().equalsIgnoreCase("harvardEduHRDepartmentShortDescription")) {
                                            person.setDepartment(attr.get(0).toString());
                                        }

                                        // set LDAP id value
                                        if (attr.getID().equalsIgnoreCase("uid")) {
                                            ldapId.setValue(attr.get(0).toString());
                                        }

                                    }

                                    // complete and commit LDAP ID
                                    ldapId.setLabel(person.getFullName());
                                    ldapId.setState("claimed");
                                    ldapId.setStateChangedBy(person.getEmail());
                                    ldapId.setStateChangedDate(new Date());
                                    identifierDAO.create(ldapId);

                                    actionLogDAO.create(new ActionLog(person.getUuid(), person.getEmail(),
                                            "Created and claimed LDAP identifier: " + ldapId));

                                    // complete and commit DASH ID
                                    dashId.setLabel(person.getFullName());
                                    dashId.setState("claimed");
                                    dashId.setStateChangedBy(person.getEmail());
                                    dashId.setStateChangedDate(new Date());
                                    dashId.setUrl("https://dash.harvard.edu/browse?type=harvardAuthor&authority=" +
                                            dashAuthorId);
                                    identifierDAO.create(dashId);

                                    actionLogDAO.create(new ActionLog(person.getUuid(), person.getEmail(),
                                            "Created and claimed DASH identifier: " + dashAuthorId));

                                } catch (NamingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        personDAO.create(person);
                        final Optional<Person> savedPerson = personDAO.findByUuid(uuid);

                        actionLogDAO.create(new ActionLog(savedPerson.get().getUuid(), savedPerson.get().getEmail(),
                                "Logged in to new profile"));

                        if (savedPerson.isPresent()) {
                            URI uri = UriBuilder.fromUri("/seas/people/" + uuid).build();
                            return Response.seeOther(uri).build();
                        }

                    }

                }

            }

        } catch (Exception e) {
            //Logger.error("Error trying to get encrypted cookie: " + ae.getMessage());
        }

        return new LoginView(LoginView.Template.FREEMARKER, orcidConfig);
    }

}

