package orcid.api.views;

import orcid.api.core.Identifier;
import orcid.api.core.Person;
import io.dropwizard.views.View;

import java.util.List;

public class PersonView extends View {

    private final Person person;
    private final List<Identifier> identifierList;

    public enum Template {

        FREEMARKER("freemarker/person.ftl");
        private String templateName;
        Template(String templateName) {
            this.templateName = templateName;
        }
        public String getTemplateName() {
            return templateName;
        }
    }

    public PersonView(PersonView.Template template, Person person, List<Identifier> identifierList) {
        super(template.getTemplateName());
        this.person = person;
        this.identifierList = identifierList;
    }

    public Person getPerson() {
        return person;
    }

    public List<Identifier> getIdentifierList() { return identifierList; }
}

