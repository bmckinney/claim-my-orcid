package orcid.api.auth.ams;

import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import orcid.api.auth.ams.HarvardCredentials;
import orcid.api.auth.ams.HarvardPrincipal;

public class HarvardAuthenticator implements Authenticator<HarvardCredentials, HarvardPrincipal> {
    @Override
    public Optional<HarvardPrincipal> authenticate(HarvardCredentials credentials)
            throws AuthenticationException {

        // Note: this is horrible authentication. Normally we'd use some
        // service to identify the password from the user name.
        if (!"pass".equals(credentials.getToken())) {
            throw new AuthenticationException("HarvardAuthenticator Exception");
        }

        // from some user service get the roles for this user
        // I am explicitly setting it just for simplicity
        HarvardPrincipal prince = new HarvardPrincipal(credentials.getToken());
        prince.getRoles().add(Roles.ADMIN);

        return Optional.fromNullable(prince);
    }
}
