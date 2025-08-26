package rh.analyst.com.repository;

import org.bson.types.ObjectId;

import dev.langchain4j.agent.tool.Tool;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import rh.analyst.com.model.Vaga;

@ApplicationScoped
public class VagaRepository implements PanacheMongoRepository<Vaga> {

    @Tool(name = "getDescricaoVaga",
     value =  """
        Recupera as informações detalhadas de uma vaga a partir do ID fornecido.
        Retorna:
        - Descrição da vaga
        - Informações adicionais
        Use esta ferramenta sempre que precisar analisar os requisitos da vaga para avaliar a compatibilidade com um candidato.
    """)
    public String descricaoVaga(String vagaId) {
        Vaga vaga = findById(new ObjectId(vagaId));

        StringBuilder sb = new StringBuilder();
        sb.append("Descrição Vaga: ").append(vaga.getJobDescription()).append("\n");
        sb.append("Informações: ").append(vaga.getAdditionalInfo());

        return sb.toString();
    }

}
