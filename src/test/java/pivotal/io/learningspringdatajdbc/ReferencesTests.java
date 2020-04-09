package pivotal.io.learningspringdatajdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Component
public class ReferencesTests {

    @Autowired
    private AuthorRepository _authorRepo;

    @Autowired
    private BookRepository _bookRepo;

    @Test
    public void should_delete_books_without_deleting_authors() {

        Author author = new Author();
        author.name = "Greg L. Turnquist";

        author = _authorRepo.save(author);

        Book book = new Book();
        book.title = "Spring Boot";
        book.addAuthor(author);

        _bookRepo.save(book);

        _bookRepo.deleteAll();

        assertThat(_authorRepo.count()).isEqualTo(1);
    }

}
