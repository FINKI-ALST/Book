package mk.ukim.finki.wp.exam.example.service.impl;

import mk.ukim.finki.wp.exam.example.model.Genre;
import mk.ukim.finki.wp.exam.example.model.exceptions.InvalidGenreIdException;
import mk.ukim.finki.wp.exam.example.repository.GenreRepository;
import mk.ukim.finki.wp.exam.example.service.GenreService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre findById(Long id) {
        return this.genreRepository.findById(id).orElseThrow(InvalidGenreIdException::new);
    }

    @Override
    public List<Genre> listAll() {
        return this.genreRepository.findAll();
    }

    @Override
    public Genre create(String name) {
        Genre genre=new Genre(name);
        return this.genreRepository.save(genre);
    }
}
