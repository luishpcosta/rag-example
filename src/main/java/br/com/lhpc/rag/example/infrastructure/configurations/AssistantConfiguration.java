package br.com.lhpc.rag.example.infrastructure.configurations;

import br.com.lhpc.rag.example.domain.assistant.DevBookGuideAssistant;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.bge.small.en.v15.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.transformer.CompressingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.file.Path;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Configuration
public class AssistantConfiguration {

    private final static String BOOKS_PATH = "classpath:books/books.json";

    private final ResourceLoader resourceLoader;

    private final ChatLanguageModel chatModel;


    public AssistantConfiguration(ResourceLoader resourceLoader, ChatLanguageModel chatModel) {
        this.resourceLoader = resourceLoader;
        this.chatModel = chatModel;
    }


    @Bean
    public DevBookGuideAssistant createAssistant() throws IOException {
        Document document = loadDocument(getDocumentPath(), new TextDocumentParser());
        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        ingestDocument(document, embeddingModel, embeddingStore);

        ContentRetriever contentRetriever = createContentRetriever(embeddingModel, embeddingStore);
        RetrievalAugmentor retrievalAugmentor = createRetrievalAugmentor(contentRetriever);

        return AiServices.builder(DevBookGuideAssistant.class)
                .chatLanguageModel(chatModel)
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }

    private Path getDocumentPath() throws IOException {
        Resource resource = resourceLoader.getResource(BOOKS_PATH);
        return Path.of(resource.getURI());
    }

    private void ingestDocument(Document document, EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300, 0))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        ingestor.ingest(document);
    }

    private ContentRetriever createContentRetriever(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(10)
                .minScore(0.6)
                .build();
    }

    private RetrievalAugmentor createRetrievalAugmentor(ContentRetriever contentRetriever) {
        QueryTransformer queryTransformer = new CompressingQueryTransformer(chatModel);
        return DefaultRetrievalAugmentor.builder()
                .queryTransformer(queryTransformer)
                .contentRetriever(contentRetriever)
                .build();
    }

}
