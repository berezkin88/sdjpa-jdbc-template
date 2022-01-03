package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = {"guru/springframework/jdbc/dao"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorDaoImplTest {

    @Autowired
    AuthorDao authorDao;

    @Test
    void testGetAuthorById() {
        var author = authorDao.getById(1L);

        assertThat(author).isNotNull();
    }

    @Test
    void authorDao_findByFirstAndLastNames() {
        var firstName = "Craig";
        var lastName = "Walls";
        var author = authorDao.getByFirstAndLastName(firstName, lastName);

        assertThat(author)
            .isNotNull();
    }

    @Test
    void authorDao_saveNewAuthor() {
        var author = new Author();
        author.setLastName("Berezkin");
        author.setFirstName("Oleksandr");
        var savedAuthor = authorDao.saveNewAuthor(author);

        assertThat(savedAuthor).isNotNull();
    }

    @Test
    void authorDao_updateAuthor() {
        var author = new Author();
        author.setLastName("B");
        author.setFirstName("Oleksandr");
        var savedAuthor = authorDao.saveNewAuthor(author);

        savedAuthor.setLastName("Berezkin");
        var updatedAuthor = authorDao.updateAuthor(savedAuthor);

        assertThat(updatedAuthor.getLastName()).isEqualTo("Berezkin");
    }

    @Test
    void authorDao_deleteAuthorById() {
        var author = new Author();
        author.setLastName("B");
        author.setFirstName("Oleksandr");
        var savedAuthorId = authorDao.saveNewAuthor(author).getId();

        authorDao.deleteAuthorById(savedAuthorId);

        assertThatThrownBy(() -> authorDao.getById(savedAuthorId))
            .isExactlyInstanceOf(EmptyResultDataAccessException.class);
    }

}