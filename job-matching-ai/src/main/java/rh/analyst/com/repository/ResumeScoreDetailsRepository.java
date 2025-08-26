package rh.analyst.com.repository;

import java.util.List;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import rh.analyst.com.model.Documento;

@ApplicationScoped
public class ResumeScoreDetailsRepository {

    @Inject
    EmbeddingStore<TextSegment> embeddingStore;

    @Inject
    EmbeddingModel embeddingModel;
    /**
     * Salva um Documento no Qdrant, convertendo payload para Metadata.
     */
    public void save(Documento doc) {
        // Converter payload em Metadata
        Metadata metadata = new Metadata();
        if (doc.getPayload() != null) {
            doc.getPayload().forEach((k, v) -> metadata.put(k, String.valueOf(v)));
        }

        // Criar TextSegment
        TextSegment segment = new TextSegment(doc.getConteudo(), metadata);

        // Gerar embedding
        Embedding embedding = embeddingModel.embed(segment).content();

        // Salvar no Qdrant
        embeddingStore.add(embedding, segment);
    }

    /**
     * Busca documentos relevantes com base em uma consulta de texto.
     */
    public List<EmbeddingMatch<TextSegment>> search(String query, int maxResults) {
        Embedding queryEmbedding = embeddingModel.embed(query).content();

        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(maxResults)
                .build();

        return embeddingStore.search(request).matches();
    }

    public void clear() {
        embeddingStore.removeAll();
    }
}
