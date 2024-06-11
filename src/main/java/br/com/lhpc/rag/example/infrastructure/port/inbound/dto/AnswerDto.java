package br.com.lhpc.rag.example.infrastructure.port.inbound.dto;

import br.com.lhpc.rag.example.domain.book.RecommendedBook;

import java.util.List;

public record AnswerDto(Seniority seniority, List<BookDto> recommendBooks) {

    public static AnswerDto of(Seniority seniority, List<RecommendedBook> recommendBooks) {
        return new AnswerDto(seniority, recommendBooks.stream().map(
                it -> new BookDto(
                        it.book().name(),
                        it.book().author(),
                        it.book().synopsis(),
                        it.reasonToRead()                )
        ).toList());
    }

}
