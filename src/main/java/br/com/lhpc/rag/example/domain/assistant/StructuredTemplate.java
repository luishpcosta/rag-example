package br.com.lhpc.rag.example.domain.assistant;

import dev.langchain4j.model.input.structured.StructuredPrompt;

import java.util.List;

public class StructuredTemplate {

    @StructuredPrompt({
            "Recommend books for {{seniority}} in the field of software development about {{subject}}",
            "Book List in recommended reading order and reason to read",
            "Base your answer on the following book identifiers: {{identifierList}}"
    })
    public record BookGuidePrompt(String seniority, String subject, List<String> identifierList) {
    }
}
