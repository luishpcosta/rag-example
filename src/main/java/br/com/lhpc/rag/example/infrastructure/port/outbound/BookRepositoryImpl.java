package br.com.lhpc.rag.example.infrastructure.port.outbound;

import br.com.lhpc.rag.example.domain.book.Book;
import br.com.lhpc.rag.example.domain.book.BookRepository;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class BookRepositoryImpl implements BookRepository {

    private final static int RETURN_LIMIT_NUMBER = 10;

    private final static String ID_FIELD = "id";

    private final static String SYNOPSIS_FIELD = "synopsis";

    private final Directory index = new MMapDirectory(Path.of("book-index"));

    private final Analyzer analyzer = new StandardAnalyzer();

    private final Supplier<IndexSearcher> searcherSupplier;

    private Map<String, Book> bookMap;

    public BookRepositoryImpl(Map<String, Book> bookMap) throws IOException {
        this.bookMap = bookMap;

        loadDocuments(bookMap);
        searcherSupplier = createSeacherIndexSuplier(index);
    }

    @Override
    public List<Book> findBySubject(String subject) {

        try {
            IndexSearcher searcher = searcherSupplier.get();
            var query = buildQuery(subject);
            var result = searcher.search(query, RETURN_LIMIT_NUMBER);

            if (result.totalHits.value == 0) {
                return List.of();
            }

            List<Book> books = new ArrayList<>(RETURN_LIMIT_NUMBER);

            for (ScoreDoc scoreDoc : result.scoreDocs) {
                Document document = searcher.doc(scoreDoc.doc);
                String id = document.get(ID_FIELD);
                books.add(bookMap.get(id));
            }

            searcher.getIndexReader().close();

            return books;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private Query buildQuery(String subject) throws ParseException {
        QueryParser parser = new QueryParser(SYNOPSIS_FIELD, analyzer);
        return parser.parse(subject);
    }

    private void loadDocuments(Map<String, Book> bookMap) throws IOException {

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(index, config);

        for (Book book : bookMap.values()) {
            Document doc = new Document();
            doc.add(new TextField(ID_FIELD, book.id(), Field.Store.YES));
            doc.add(new TextField(SYNOPSIS_FIELD, book.synopsis(), Field.Store.YES));
            writer.addDocument(doc);
        }
        writer.close();
    }

    private static Supplier<IndexSearcher> createSeacherIndexSuplier(Directory index) {
        return () -> {
            try {
                DirectoryReader reader = DirectoryReader.open(index);
                return new IndexSearcher(reader);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
