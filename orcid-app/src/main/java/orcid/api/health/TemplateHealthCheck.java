package orcid.api.health;

import com.codahale.metrics.health.HealthCheck;
import orcid.api.core.Template;
import com.google.common.base.Optional;

public class TemplateHealthCheck extends HealthCheck {
    private final Template template;

    public TemplateHealthCheck(Template template) {
        this.template = template;
    }

    // http://localhost:8081/healthcheck
    @Override
    protected Result check() throws Exception {
        template.render(Optional.of("woo"));
        template.render(Optional.<String>absent());
        return Result.healthy();
    }
}