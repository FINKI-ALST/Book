package mk.ukim.finki.wp.exam.example.service;
import mk.ukim.finki.wp.exam.example.model.Book;
import mk.ukim.finki.wp.exam.example.model.Genre;
import mk.ukim.finki.wp.exam.example.model.User;

import java.util.List;
import java.util.Optional;


public interface BookService {

    List<Book> listAllBooks();


    Book findById(Long id);


    Book create(String name, Double price, Integer quantity, List<Long> genres);


    Book update(Long id, String name, Double price, Integer quantity, List<Long> genres);


    Book delete(Long id);


    List<Book> listBooksByNameAndGenre(String name, Long genreId);



}
