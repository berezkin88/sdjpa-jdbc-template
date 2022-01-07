package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = {"guru/springframework/jdbc/dao"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookDaoImplTest {

    @Autowired
    BookDao bookDao;

    @Test
    void getById() {
        var book = bookDao.getById(1L);

        assertThat(book)
            .isNotNull();
    }

    @Test
    void getByTitle() {
        var title = "Clean Code";
        var book = bookDao.getByTitle(title);

        assertThat(book)
            .isNotNull()
            .extracting(Book::getTitle)
            .isEqualTo(title);
    }

    @Test
    void saveNewBook() {
        var book = new Book();
        book.setTitle("Test Book");
        book.setPublisher("Test publisher");
        book.setIsbn("123123");
        book.setAuthorId(1L);
        var savedBook = bookDao.saveNewBook(book);

        assertThat(savedBook)
            .isNotNull()
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(book);
    }

    @Test
    void updateBook() {
        var book = new Book();
        book.setTitle("T");
        book.setAuthorId(1L); // to avoid foreign constraint exception
        var savedBook = bookDao.saveNewBook(book);

        savedBook.setTitle("Test Book");
        var updatedBook = bookDao.updateBook(savedBook);

        assertThat(updatedBook.getTitle())
            .isEqualTo("Test Book");
    }

    @Test
    void deleteById() {
        var book = new Book();
        book.setTitle("Test Book");
        book.setPublisher("Test publisher");
        book.setIsbn("123123");
        book.setAuthorId(1L);
        var savedBookId = bookDao.saveNewBook(book).getId();

        bookDao.deleteById(savedBookId);

        assertThatThrownBy(() -> bookDao.getById(savedBookId))
            .isExactlyInstanceOf(EmptyResultDataAccessException.class);
    }
}