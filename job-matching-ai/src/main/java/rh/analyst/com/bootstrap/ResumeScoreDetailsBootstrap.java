package rh.analyst.com.bootstrap;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import rh.analyst.com.model.Candidato;
import rh.analyst.com.model.Documento;
import rh.analyst.com.model.Vaga;
import rh.analyst.com.repository.CandidatoRepository;
import rh.analyst.com.repository.ResumeScoreDetailsRepository;
import rh.analyst.com.repository.VagaRepository;

@ApplicationScoped
public class ResumeScoreDetailsBootstrap {

    @Inject
    ResumeScoreDetailsRepository resumeScoreDetailsRepository;

    @Inject
    VagaRepository vagaRepository;

    @Inject
    CandidatoRepository candidatoRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final Logger LOG = Logger.getLogger(ResumeScoreDetailsBootstrap.class);

    void onStart(@Observes StartupEvent ev) {
        LOG.info("[BOOTSTRAP] ResumeScoreDetailsBootstrap iniciado...");

        // Limpa coleções
        resumeScoreDetailsRepository.clear();
        vagaRepository.deleteAll();
        candidatoRepository.deleteAll();

        Path resourceDir = Paths.get("src/main/resources/resume-score-details-ptbr");
        if (!Files.exists(resourceDir)) {
            LOG.error("[ERRO] Diretório não encontrado: " + resourceDir.toAbsolutePath());
            return;
        }

        try {
            Files.list(resourceDir)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(this::processFile);
        } catch (IOException e) {
            LOG.error("Erro ao listar arquivos do diretório", e);
        }
    }

    private void processFile(Path path) {
        try (InputStream in = Files.newInputStream(path)) {
            Map<String, Object> jsonMap = mapper.readValue(in, new TypeReference<>() {});

            // Extraindo informações do JSON
            String jobDescription = (String) jsonMap.getOrDefault("job_description", "");
            String additionalInfo = (String) jsonMap.getOrDefault("additional_info", "");
            String resume = (String) jsonMap.getOrDefault("resume", "");
            Map<String, Object> detailsMap = (Map<String, Object>) jsonMap.get("details");

            // Salvar vaga no Mongo
            Vaga vaga = new Vaga();
            vaga.setJobDescription(jobDescription);
            vaga.setAdditionalInfo(additionalInfo);
            vagaRepository.persist(vaga);

            // Salvar candidato no Mongo
            if (detailsMap != null) {
                Candidato candidato = new Candidato();
                candidato.setResume(resume);
                candidato.setDetails(detailsMap);
                candidatoRepository.persist(candidato);
            }

            // Salvar no Qdrant (vetores)
            Documento vagaDoc = new Documento();
            vagaDoc.setConteudo(jobDescription + "\n" + resume);
            vagaDoc.setPayload(Map.of(
                "job_description", jobDescription,
                "resume", resume
            ));
            resumeScoreDetailsRepository.save(vagaDoc);

            LOG.info("[BOOTSTRAP] Arquivo processado: " + path.getFileName());

        } catch (Exception e) {
            LOG.error("[ERRO] Falha ao processar " + path.getFileName(), e);
        }
    }



}
