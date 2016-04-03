package orcid.api.db;

/**
 * Created by wim033 on 1/12/16.
 */

import orcid.api.core.ActionLog;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class ActionLogDAO extends AbstractDAO<ActionLog> {
    public ActionLogDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<ActionLog> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public ActionLog create(ActionLog actionLog) {
        return persist(actionLog);
    }

    public List<ActionLog> findAll() {
        return list(namedQuery("orcid.api.core.ActionLog.findAll"));
    }

    public Optional<ActionLog> findByPersonUuid(String uuid) {
        ActionLog foundLog = (ActionLog) namedQuery("orcid.api.core.ActionLog.findByPersonUuid")
                .setParameter("uuid", uuid);
        return Optional.fromNullable(foundLog);
    }
}
