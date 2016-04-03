package orcid.api.views;

import io.dropwizard.views.View;
import orcid.api.app.OrcidConfiguration;


public class LoginView extends View {

    private OrcidConfiguration config;

    public enum Template {
        FREEMARKER("freemarker/login.ftl");

        private String templateName;

        Template(String templateName) {
            this.templateName = templateName;
        }

        public String getTemplateName() {
            return templateName;
        }
    }

    public LoginView(LoginView.Template template, OrcidConfiguration config) {
        super(template.getTemplateName());
        this.config = config;
    }

    public OrcidConfiguration getConfig() { return config; }

}

