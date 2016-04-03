package orcid.api.auth.ams;

import io.dropwizard.Configuration;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bmckinney on 4/2/16.
 */
public class AmsConfiguration extends Configuration {

    @NotNull
    @JsonProperty("keyPath")
    private String keyPath;

    @NotNull
    @JsonProperty("secret")
    private String secret;

    @NotNull
    @JsonProperty("ldapContext")
    private String ldapContext;

    @NotNull
    @JsonProperty("ldapOuPeople")
    private String ldapOuPeople;

    @NotNull
    @JsonProperty("ldapTimeout")
    private String ldapTimeout;

    @NotNull
    @JsonProperty("ldapAuthentication")
    private String ldapAuthentication;

    @NotNull
    @JsonProperty("ldapProtocol")
    private String ldapProtocol;

    @NotNull
    @JsonProperty("ldapPrincipal")
    private String ldapPrincipal;

    @NotNull
    @JsonProperty("ldapUrl")
    private String ldapUrl;

    @NotNull
    @JsonProperty("ldapCredentials")
    private String ldapCredentials;

    @NotNull
    @JsonProperty("encryptedCookie")
    private String encryptedCookie;

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getLdapContext() {
        return ldapContext;
    }

    public void setLdapContext(String ldapContext) {
        this.ldapContext = ldapContext;
    }

    public String getLdapOuPeople() {
        return ldapOuPeople;
    }

    public void setLdapOuPeople(String ldapOuPeople) {
        this.ldapOuPeople = ldapOuPeople;
    }

    public String getLdapTimeout() {
        return ldapTimeout;
    }

    public void setLdapTimeout(String ldapTimeout) {
        this.ldapTimeout = ldapTimeout;
    }

    public String getLdapAuthentication() {
        return ldapAuthentication;
    }

    public void setLdapAuthentication(String ldapAuthentication) {
        this.ldapAuthentication = ldapAuthentication;
    }

    public String getLdapProtocol() {
        return ldapProtocol;
    }

    public void setLdapProtocol(String ldapProtocol) {
        this.ldapProtocol = ldapProtocol;
    }

    public String getLdapPrincipal() {
        return ldapPrincipal;
    }

    public void setLdapPrincipal(String ldapPrincipal) {
        this.ldapPrincipal = ldapPrincipal;
    }

    public String getLdapUrl() {
        return ldapUrl;
    }

    public void setLdapUrl(String ldapUrl) {
        this.ldapUrl = ldapUrl;
    }

    public String getLdapCredentials() {
        return ldapCredentials;
    }

    public void setLdapCredentials(String ldapCredentials) {
        this.ldapCredentials = ldapCredentials;
    }

    public String getEncryptedCookie() {
        return encryptedCookie;
    }

    public void setEncryptedCookie(String encryptedCookie) {
        this.encryptedCookie = encryptedCookie;
    }
}
