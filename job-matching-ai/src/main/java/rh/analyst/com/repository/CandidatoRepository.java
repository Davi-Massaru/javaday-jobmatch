package rh.analyst.com.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import dev.langchain4j.agent.tool.Tool;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import rh.analyst.com.model.Candidato;

@ApplicationScoped
public class CandidatoRepository implements PanacheMongoRepository<Candidato> {
   @Tool(
        name = "getResumoCandidato",
        value = """
            Recupera as informações detalhadas de um candidato a partir do ID fornecido.
            Retorna de forma estruturada:
            - Resume (texto completo)
            - Skills (lista de habilidades)
            - Experiência profissional (títulos de cargos e empresas)
            Use esta ferramenta para analisar o perfil do candidato e avaliar a compatibilidade com uma vaga.
        """
    )
    public String resumoCandidato(String candidatoId) {
        try {
            ObjectId oid = new ObjectId(candidatoId);
            Candidato candidato = findById(oid);

            StringBuilder sb = new StringBuilder();
            sb.append("Resume:\n").append(candidato.getResume()).append("\n\n");

            Map<String, Object> details = candidato.getDetails();
            if (details != null) {
                // Skills
                List<String> skills = (List<String>) details.getOrDefault("skills", List.of());
                sb.append("Skills:\n");
                if (!skills.isEmpty()) {
                    sb.append(String.join(", ", skills));
                } else {
                    sb.append("Nenhuma skill registrada");
                }
                sb.append("\n\n");

                // Experiência
                List<Map<String, Object>> employmentHistory = (List<Map<String, Object>>) details.getOrDefault("employment_history", List.of());
                sb.append("Experiência Profissional:\n");
                if (!employmentHistory.isEmpty()) {
                    String experienceSummary = employmentHistory.stream()
                            .map(e -> e.getOrDefault("job_title", "") + " @ " + e.getOrDefault("company_name", ""))
                            .collect(Collectors.joining("\n"));
                    sb.append(experienceSummary);
                } else {
                    sb.append("Nenhuma experiência registrada");
                }
            }

            return sb.toString();

        } catch (IllegalArgumentException e) {
            return "ID do candidato inválido: " + candidatoId;
        } catch (Exception e) {
            return "Erro ao recuperar o candidato: " + e.getMessage();
        }
    }
}