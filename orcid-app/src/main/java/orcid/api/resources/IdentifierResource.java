package orcid.api.resources;

import orcid.api.core.Identifier;
import orcid.api.db.IdentifierDAO;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/seas/identifiers")
@Produces(MediaType.APPLICATION_JSON)
public class IdentifierResource {

    private final IdentifierDAO dao;

    public IdentifierResource(IdentifierDAO dao) {
        this.dao = dao;
    }

    @POST
    @UnitOfWork
    public Identifier createIdentifier(Identifier identifier) {
        return dao.create(identifier);
    }

    @GET
    @UnitOfWork
    public List<Identifier> listIdentifiers() {
        return dao.findAll();
    }


}
