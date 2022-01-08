package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.domain.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuthorMapper implements RowMapper<Author> {

    @Override
    public Author mapRow(ResultSet resultSet, int i) throws SQLException {
        resultSet.next();

        var author = new Author();
        author.setId(resultSet.getLong("id"));
        author.setFirstName(resultSet.getString("first_name"));
        author.setLastName(resultSet.getString("last_name"));

        if (resultSet.getString("isbn") != null) {
            author.setBooks(new ArrayList<>());
            author.getBooks().add(mapBook(resultSet));
        }

        while (resultSet.next()) {
            author.getBooks().add(mapBook(resultSet));
        }

        return author;
    }

    private Book mapBook(ResultSet resultSet) throws SQLException {
        var book = new Book();
        book.setId(resultSet.getLong(4));
        book.setIsbn(resultSet.getString(5));
        book.setPublisher(resultSet.getString(6));
        book.setTitle(resultSet.getString(7));
        book.setAuthorId(resultSet.getLong(1));

        return book;
    }
}
