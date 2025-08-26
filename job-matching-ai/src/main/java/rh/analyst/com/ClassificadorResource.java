package rh.analyst.com;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import rh.analyst.com.agents.ClassificadoCandidatoVaga;

@Path("/classificador")
public class ClassificadorResource {

    @Inject
    ClassificadoCandidatoVaga classificadoCandidatoVaga;

    @GET
    @Path("/avaliar")
    @Produces(MediaType.TEXT_PLAIN)
    public String analyzeMedicationRisks( @QueryParam("vagaId") String vagaId,
            @QueryParam("candidatoId") String candidatoId) {

        return classificadoCandidatoVaga.avaliarCompatibilidade(vagaId, candidatoId);
    }

}
