package orcid.api.auth.ams;

/**
 * Created by wim033 on 1/25/16.
 */
public class HarvardCredentials {

    private final String token;

    public HarvardCredentials(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
