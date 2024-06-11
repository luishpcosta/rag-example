package br.com.lhpc.rag.example.infrastructure.port.inbound;

import br.com.lhpc.rag.example.application.BookRecommendationService;
import br.com.lhpc.rag.example.infrastructure.port.inbound.dto.AnswerDto;
import br.com.lhpc.rag.example.infrastructure.port.inbound.dto.QuestionDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/book/for-developers")
public record DevBookGuideController(BookRecommendationService bookRecommendationService) {

    @PostMapping
    public ResponseEntity<AnswerDto> generateRecommendation(@RequestBody @Valid QuestionDto question) {
        var listOfRecommendBook = bookRecommendationService.getRecomendationBySubjectAndSeniority(
                question.subject(),
                question.seniority().description()
        );
        return ResponseEntity.ok(AnswerDto.of(question.seniority(), listOfRecommendBook));
    }

}
