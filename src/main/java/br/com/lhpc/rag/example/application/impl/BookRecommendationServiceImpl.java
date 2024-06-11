package br.com.lhpc.rag.example.application.impl;

import br.com.lhpc.rag.example.application.BookRecommendationService;
import br.com.lhpc.rag.example.domain.assistant.DevBookGuideAssistant;
import br.com.lhpc.rag.example.domain.book.Book;
import br.com.lhpc.rag.example.domain.book.BookRepository;
import br.com.lhpc.rag.example.domain.book.RecommendedBook;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.lhpc.rag.example.domain.assistant.StructuredTemplate.BookGuidePrompt;

@Service
public record BookRecommendationServiceImpl(
        BookRepository bookRepository,
        DevBookGuideAssistant devBookGuideAssistant
) implements BookRecommendationService {

    @Override
    public List<RecommendedBook> getRecomendationBySubjectAndSeniority(String subject, String seniority) {

        List<Book> bookList = bookRepository.findBySubject(subject);
        var identifierList = bookList.stream().map(Book::id).toList();

        var bookGuidePrompt = new BookGuidePrompt(
                seniority,
                subject,
                identifierList
        );

        Prompt prompt = StructuredPromptProcessor.toPrompt(bookGuidePrompt);

        return devBookGuideAssistant.chat(prompt.text())
                .content()
                .recommendedBooks();
    }
}
