package br.com.lhpc.rag.example.domain.assistant;

import br.com.lhpc.rag.example.domain.book.ListRecommendedBook;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;

public interface DevBookGuideAssistant {

    @SystemMessage("You are an assistant specialized in recommending books for developers")
    Result<ListRecommendedBook> chat(String userMessage);
}