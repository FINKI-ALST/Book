package mk.ukim.finki.wp.exam.example.service.impl;

import mk.ukim.finki.wp.exam.example.model.Genre;
import mk.ukim.finki.wp.exam.example.model.Book;
import mk.ukim.finki.wp.exam.example.model.exceptions.InvalidBookIdException;
import mk.ukim.finki.wp.exam.example.repository.GenreRepository;
import mk.ukim.finki.wp.exam.example.repository.BookRepository;
import mk.ukim.finki.wp.exam.example.service.BookService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    public BookServiceImpl(BookRepository bookRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Book> listAllBooks() {
        return this.bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) {
        return this.bookRepository.findById(id).orElseThrow(InvalidBookIdException::new);
    }

    @Override
    public Book create(String name, Double price, Integer quantity, List<Long> genreIds) {
        List<Genre> genres = this.genreRepository.findAllById(genreIds);
        Book book = new Book(name, price, quantity, genres);


        return this.bookRepository.save(book);
    }

    @Override
    public Book update(Long id, String name, Double price, Integer quantity, List<Long> genreIds) {
        Book book=this.findById(id);
        book.setName(name);
        book.setPrice(price);
        book.setQuantity(quantity);
        List<Genre> genres = this.genreRepository.findAllById(genreIds);

        book.setGenres(genres);
        return this.bookRepository.save(book);
    }

    @Override
    public Book delete(Long id) {
        Book book = this.findById(id);
        this.bookRepository.delete(book);
        return book;
    }

    @Override
    public List<Book> listBooksByNameAndGenre(String name, Long genreId) {

        Genre genre = genreId!=null? this.genreRepository.findById(genreId).orElse((Genre) null):null;

        String nameLike="%" +name+ "%";
        if(name!=null && genre!=null){
            return this.bookRepository.findAllByNameLikeAndGenresContaining(nameLike, genre);
        }
        else if(name!=null){
            return this.bookRepository.findAllByNameLike(nameLike);
        }
        else if (genre!=null) {
            return this.bookRepository.findAllByGenresContaining(genre);
        }
        else {

            return this.bookRepository.findAll();
        }
        }

  


    }

