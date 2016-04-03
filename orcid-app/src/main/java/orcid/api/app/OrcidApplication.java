package orcid.api.app;

import orcid.api.cli.RenderCommand;
import orcid.api.core.*;
import orcid.api.db.ActionLogDAO;
import orcid.api.db.IdentifierDAO;
import orcid.api.db.PersonDAO;
import orcid.api.health.TemplateHealthCheck;
import orcid.api.resources.*;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

// CAS AUTH
//import orcid.api.auth.cas.UserFactory;
//import orcid.api.auth.cas.ShiroBundle;
//import orcid.api.auth.cas.ShiroConfiguration;
//import org.eclipse.jetty.server.session.SessionHandler;
//import org.secnod.shiro.jaxrs.ShiroExceptionMapper;

import java.util.Map;

public class OrcidApplication extends Application<OrcidConfiguration> {


    // CAS AUTH
    // see:
    // - https://github.com/silb/dropwizard-shiro and
    // - https://github.com/LatinumNetwork/dropwizard-shiro-cas-example
    //    private final ShiroBundle<OrcidConfiguration> shiro = new ShiroBundle<OrcidConfiguration>() {
    //
    //        @Override
    //        protected ShiroConfiguration narrow(OrcidConfiguration configuration) {
    //            return configuration.shiro;
    //        }
    //    };

    public static void main(String[] args) throws Exception {
        new OrcidApplication().run(args);
    }

    private final HibernateBundle<OrcidConfiguration> hibernateBundle =
            new HibernateBundle<OrcidConfiguration>(Person.class, Identifier.class, ActionLog.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(OrcidConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "orcid-api";
    }

    @Override
    public void initialize(Bootstrap<OrcidConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addCommand(new RenderCommand());
        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MigrationsBundle<OrcidConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(OrcidConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(new ViewBundle<OrcidConfiguration>() {
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(OrcidConfiguration configuration) {
                return configuration.getViewRendererConfiguration();
            }
        });
        // CAS AUTH
        //bootstrap.addBundle(shiro);
        //bootstrap.addBundle(ams);
    }

    @Override
    public void run(OrcidConfiguration configuration, Environment environment) {

        // java -jar target/orcid-api-1.0.0-SNAPSHOT.jar server orcid.yml

        // DAOs
        final PersonDAO personDAO = new PersonDAO(hibernateBundle.getSessionFactory());
        final IdentifierDAO identifierDAO = new IdentifierDAO(hibernateBundle.getSessionFactory());
        final ActionLogDAO actionLogDAO = new ActionLogDAO(hibernateBundle.getSessionFactory());

        // CAS AUTH
        //environment.jersey().register(new UserFactory());
        //environment.jersey().register(new ShiroExceptionMapper());
        //environment.getApplicationContext().setSessionHandler(new SessionHandler());

        // HEALTH CHECKS
        final Template template = configuration.buildTemplate();
        environment.healthChecks().register("template", new TemplateHealthCheck(template));

        // RESOURCES
        environment.jersey().register(new PersonResource(personDAO, identifierDAO, actionLogDAO));
        environment.jersey().register(new PeopleResource(personDAO, actionLogDAO));
        environment.jersey().register(new IdentifierResource(identifierDAO));
        environment.jersey().register(new LoginResource(personDAO, identifierDAO, actionLogDAO,
                configuration.getAmsConfiguration(), configuration));
        environment.jersey().register(new OrcidResource(personDAO, identifierDAO, actionLogDAO));

    }
}

