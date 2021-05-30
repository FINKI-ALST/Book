package mk.ukim.finki.wp.exam.example.repository;

import mk.ukim.finki.wp.exam.example.model.Book;
import mk.ukim.finki.wp.exam.example.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByNameLikeAndGenresContaining(String name, Genre Genre);
    List<Book> findAllByNameLike(String name);
    List<Book> findAllByGenresContaining(Genre genre);

}
