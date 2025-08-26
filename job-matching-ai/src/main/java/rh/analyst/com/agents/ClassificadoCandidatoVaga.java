package rh.analyst.com.agents;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;
import rh.analyst.com.repository.CandidatoRepository;
import rh.analyst.com.repository.VagaRepository;

@RegisterAiService(
        chatMemoryProviderSupplier = RegisterAiService.BeanChatMemoryProviderSupplier.class,
        tools = {VagaRepository.class, CandidatoRepository.class}
)
public interface ClassificadoCandidatoVaga {
    
    @SystemMessage("""
        Você é um agente de recrutamento altamente especializado em análise de compatibilidade entre candidatos e vagas.
        Receberá um ID de vaga e um ID de candidato.
        Usando apenas as ferramentas disponíveis (ex: getDescricaoVaga, getResumoCandidato), você deve:

        1. Buscar informações completas da vaga: descrição, requisitos, qualificações, informações adicionais.
        2. Buscar informações completas do candidato: currículo, habilidades, experiência, histórico profissional e educacional.
        3. Avaliar objetivamente a compatibilidade entre o candidato e a vaga, considerando experiência, habilidades, qualificações e fit cultural.
        4. Gerar um score de compatibilidade entre 0 e 1.
        5. Fornecer uma explicação detalhada de como o score foi calculado, destacando pontos fortes e fracos do candidato em relação à vaga.

        Sempre baseie sua análise exclusivamente nos dados reais do candidato e da vaga. Não invente informações e seja conciso e claro na resposta.
    """)
    @UserMessage("Avalie a compatibilidade entre a vaga ID: {{vagaId}} e o candidato ID: {{candidatoId}}")
    String avaliarCompatibilidade(@V("vagaId") String vagaId, @V("candidatoId") String candidatoId);

}
