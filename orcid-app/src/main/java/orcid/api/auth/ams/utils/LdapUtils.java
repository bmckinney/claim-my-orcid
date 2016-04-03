package orcid.api.auth.ams.utils;

import orcid.api.auth.ams.AmsConfiguration;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;

/**
 */
public final class LdapUtils {

    public static SearchResult getLdapResults(String id, AmsConfiguration amsConfig) {


        SearchControls control = new SearchControls();
        try {

            control.setTimeLimit(Integer.valueOf(amsConfig.getLdapTimeout()));

            // Set up the environment for creating the initial context
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, amsConfig.getLdapContext());
            env.put(Context.SECURITY_AUTHENTICATION, amsConfig.getLdapAuthentication());
            env.put(Context.SECURITY_PROTOCOL, amsConfig.getLdapProtocol());
            env.put(Context.SECURITY_PRINCIPAL, amsConfig.getLdapPrincipal());
            env.put(Context.SECURITY_CREDENTIALS, amsConfig.getLdapCredentials());
            env.put(Context.PROVIDER_URL, amsConfig.getLdapUrl());

            // Create the initial context
            DirContext ctx = new InitialDirContext(env);
            NamingEnumeration resultSet = ctx.search(amsConfig.getLdapOuPeople(),
                    "(harvardeduidnumber="+id+")", control);

            if (resultSet.hasMore())
                return (SearchResult) resultSet.next();

            if(ctx != null)
                ctx.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
