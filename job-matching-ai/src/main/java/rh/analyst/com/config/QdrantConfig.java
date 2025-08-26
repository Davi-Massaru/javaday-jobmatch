package rh.analyst.com.config;

import java.util.concurrent.ExecutionException;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import io.qdrant.client.grpc.Collections;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class QdrantConfig {

    @ConfigProperty(name = "qdrant.host", defaultValue = "localhost")
    String host;

    @ConfigProperty(name = "qdrant.grpc-port", defaultValue = "6334")
    int grpcPort;

    @ConfigProperty(name = "qdrant.collection", defaultValue = "resume-score-details")
    String collectionName;

    @Produces
    @ApplicationScoped
    public EmbeddingStore<TextSegment> embeddingStore() {
    try {
            // Cliente GRPC
            QdrantClient client = new QdrantClient(
                    QdrantGrpcClient.newBuilder(host, grpcPort, false).build()
            );

            // Cria coleção se não existir
            client.createCollectionAsync(
                    collectionName,
                    Collections.VectorParams.newBuilder()
                            .setSize(384) // dimensão do AllMiniLmL6V2
                            .setDistance(Collections.Distance.Cosine)
                            .build()
            ).get();

            // Retorna EmbeddingStore pronto para injeção
            return QdrantEmbeddingStore.builder()
                    .host(host)
                    .port(grpcPort)
                    .collectionName(collectionName)
                    .build();

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Erro ao configurar Qdrant", e);
        }
    }
}
