package mk.ukim.finki.wp.exam.example.service;
import mk.ukim.finki.wp.exam.example.model.Genre;
import java.util.List;
import java.util.Optional;


public interface GenreService {

    Genre findById(Long id);

    List<Genre> listAll();

    Genre create(String name);
}
