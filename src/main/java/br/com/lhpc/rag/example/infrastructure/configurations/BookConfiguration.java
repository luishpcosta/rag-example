package br.com.lhpc.rag.example.infrastructure.configurations;

import br.com.lhpc.rag.example.domain.book.Book;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
public class BookConfiguration {

    private final ResourceLoader resourceLoader;

    public BookConfiguration(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public Map<String, Book> loadBooks() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:books/books.json");
        var objectMapper = new ObjectMapper();

        var books = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Book>>() {
        });

        return books.stream()
                .collect(Collectors
                        .toMap(Book::id, book -> book));
    }

}
