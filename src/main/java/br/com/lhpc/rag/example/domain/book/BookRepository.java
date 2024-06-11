package br.com.lhpc.rag.example.domain.book;

import java.util.List;

public interface BookRepository {

   List<Book> findBySubject(String subject);

}
