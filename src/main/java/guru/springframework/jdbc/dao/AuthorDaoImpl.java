package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorDaoImpl implements AuthorDao {

    private final JdbcTemplate jdbcTemplate;

    public AuthorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Author getById(Long id) {
        var sql = "select author.id as id, first_name, last_name, book.id, book.isbn, book.publisher, book.title from author " +
            "left outer join book on author.id = book.author_id where author.id = ?";

        return jdbcTemplate.query(sql, new AuthorExtractor(), id);
    }

    @Override
    public Author getByFirstAndLastName(String firstName, String lastName) {
        return jdbcTemplate.queryForObject("select * from author where first_name = ? and last_name = ?",
            getRowMapper(),
            firstName,
            lastName);
    }

    @Override
    public Author saveNewAuthor(Author author) {
        jdbcTemplate.update("insert into author (first_name, last_name) values (?, ?)",
            author.getFirstName(),
            author.getLastName());

        var lastInsertedId = jdbcTemplate.queryForObject("select LAST_INSERT_ID()", Long.class);

        return this.getById(lastInsertedId);
    }

    @Override
    public Author updateAuthor(Author author) {
        jdbcTemplate.update("update author set first_name = ?, last_name = ? where id = ?",
            author.getFirstName(),
            author.getLastName(),
            author.getId());

        return this.getById(author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        jdbcTemplate.update("delete from author where id = ?", id);
    }

    private RowMapper<Author> getRowMapper() {
        return new AuthorMapper();
    }
}
