package orcid.api.db;

/**
 * Created by wim033 on 1/12/16.
 */

import orcid.api.core.Identifier;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class IdentifierDAO extends AbstractDAO<Identifier> {

    public IdentifierDAO(SessionFactory factory) {
        super(factory);
    }

    public Identifier create(Identifier identifier) {
        return persist(identifier);
    }

    public void delete(Identifier identifier) { delete(identifier); }

    public void claim(Identifier identifier) {
        identifier.setState("claimed");
    }

    public List<Identifier> findAll() {
        return list(namedQuery("orcid.api.core.Identifier.findAll"));
    }

    public Optional<Identifier> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public List<Identifier> findAllByPersonUuid(String uuid) {
        return list(namedQuery("orcid.api.core.Identifier.findAllByPersonUuid")
                .setParameter("uuid", uuid));
    }

    public Optional<Identifier> findByUuid(String uuid) {
        Identifier foundIdent = (Identifier) namedQuery("orcid.api.core.Identifier.findByUuid")
                .setParameter("uuid", uuid)
                .uniqueResult();
        return Optional.fromNullable(foundIdent);
    }

}
