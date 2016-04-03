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
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.impl.client.*;
import org.apache.http.protocol.HttpContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/seas/author/claim")
public class OrcidResource {

    @Context
    HttpServletRequest request;

    private final PersonDAO personDAO;
    private final IdentifierDAO identifierDAO;
    private final ActionLogDAO actionLogDAO;

    public OrcidResource(PersonDAO personDAO, IdentifierDAO identifierDAO, ActionLogDAO actionLogDAO) {
        this.personDAO = personDAO;
        this.identifierDAO = identifierDAO;
        this.actionLogDAO = actionLogDAO;
    }

    @GET
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    public Object claim(@Context HttpHeaders headers) {

        String code = request.getParameter("code");
        String uuid = request.getParameter("state");
        Person person;
        Identifier orcidId;
        String token = null;
        String orcid = null;
        URI uri = null;

        if (code != null && uuid != null) {

            uri = UriBuilder.fromUri("/seas/people/" + uuid).build();

            person = personDAO.findByUuid(uuid).get();
            if (person != null) {

                String tokenResponse = getOrcidToken(code);
                if (tokenResponse.length() > 0) {

                    try {

                        JSONParser parser = new JSONParser();
                        Object obj = parser.parse(tokenResponse);
                        JSONObject jsonObject = (JSONObject) obj;
                        token = (String) jsonObject.get("access_token");
                        orcid = (String) jsonObject.get("orcid");

                    } catch(Exception e) {

                        orcidId = new Identifier("orcid",orcid,uuid);
                        orcidId.setLabel(person.getFullName());
                        orcidId.setState("failed token");
                        orcidId.setStateChangedBy(person.getEmail());
                        orcidId.setStateChangedDate(new Date());
                        orcidId.setOrcidAuthCode(code);
                        identifierDAO.create(orcidId);

                        actionLogDAO.create(new ActionLog(person.getUuid(), person.getEmail(),
                                "ERROR: failed to get ORCID and auth token"));

                        return Response.seeOther(uri).build();
                    }
                }

                if (token != null && orcid != null) {

                    orcidId = new Identifier("orcid",orcid,person.getUuid());
                    orcidId.setLabel(person.getFullName());
                    orcidId.setState("claimed");
                    orcidId.setStateChangedBy(person.getEmail());
                    orcidId.setStateChangedDate(new Date());
                    orcidId.setOrcidAuthCode(code);
                    orcidId.setOrcidAccessToken(token);
                    orcidId.setUrl("http://orcid.org/" + orcid);
                    identifierDAO.create(orcidId);

                    actionLogDAO.create(new ActionLog(person.getUuid(), person.getEmail(),
                            "Created and claimed ORCID identifier: " + orcid));

                    person.setOrcidStatus("claimed");
                    personDAO.update(person);

                    return Response.seeOther(uri).build();

                }
            }
        }

        return Response.seeOther(uri).build();
    }


    private static String getOrcidToken(String code) {

        // exchange auth code for token:
        // curl -i -L -H 'Accept: application/json' --data 'client_id=APP-674MCQQR985VZZQ2&client_secret=5f63d1c5-3f08-4fa5-b066-fd985ffd0df7&grant_type=authorization_code&code=eUeiz2&redirect_uri=https://developers.google.com/oauthplayground' 'https://api.sandbox.orcid.org/oauth/token'
        // response
        // {"access_token":"5005eb18-be6b-4ac0-b084-0443289b3378","token_type":"bearer","expires_in":631138518,"scope":"/authenticate","orcid":"0000-0003-1495-7122","name":"ORCID Test "}

        StringBuilder jsonResult = new StringBuilder();

        try {

            // silly override to handle redirection of post requests without changing the method to get
            DefaultHttpClient client = new DefaultHttpClient();
            client.setRedirectHandler(new DefaultRedirectHandler() {
                @Override
                public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
                    boolean isRedirect = super.isRedirectRequested(response, context);
                    if (!isRedirect) {
                        int responseCode = response.getStatusLine().getStatusCode();
                        if (responseCode == 301 || responseCode == 302) {
                            return true;
                        }
                    }
                    return isRedirect;
                }
            });

            String url = "https://api.sandbox.orcid.org/oauth/token";

            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("client_id", "APP-E35X3FEDCM0LPKT6"));
            nvps.add(new BasicNameValuePair("client_secret", "13657ceb-a50b-4f65-a152-7d3cf1ae3c3a"));
            nvps.add(new BasicNameValuePair("grant_type", "authorization_code"));
            nvps.add(new BasicNameValuePair("code", code));
            nvps.add(new BasicNameValuePair("redirect_uri", "http://eaton.hul.harvard.edu:9000/seas/author/claim"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            httpPost.setHeader("Accept","application/json");
            HttpResponse response = client.execute(httpPost);

            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("RESPONSE: " + response.toString());
                System.out.println("Response code not 200: " + response.getStatusLine().getStatusCode() + " - " +
                        response.getStatusLine().getReasonPhrase());
                return "";
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            String output;
            while ((output = br.readLine()) != null) {
                jsonResult.append(output);
            }
            client.getConnectionManager().shutdown();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return jsonResult.toString();
    }

}

