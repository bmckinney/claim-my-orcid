package orcid.api.auth.ams;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class HarvardPrincipal implements Principal{

    private String token;
    private List<String> roles = new ArrayList<>();

    public HarvardPrincipal(String token) {
        this.token = token;
    }

    public List<String> getRoles() {
        return roles;
    }

    public boolean isUserInRole(String roleToCheck) {
        return roles.contains(roleToCheck);
    }

    public String getName() {
        return token;
    }
}
