package br.com.lhpc.rag.example.application;

import br.com.lhpc.rag.example.domain.book.RecommendedBook;

import java.util.List;

public interface BookRecommendationService {

    List<RecommendedBook> getRecomendationBySubjectAndSeniority(String subject, String seniority);
}
