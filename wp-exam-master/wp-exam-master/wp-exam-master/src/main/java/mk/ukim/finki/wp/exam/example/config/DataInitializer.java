package mk.ukim.finki.wp.exam.example.config;

import mk.ukim.finki.wp.exam.example.model.Role;
import mk.ukim.finki.wp.exam.example.model.User;
import mk.ukim.finki.wp.exam.example.service.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DataInitializer {

    public static final String ADMIN = "admin";

    private final UserService userService;

    private final GenreService genreService;

    private final BookService bookService;

    private final LanguageService languageService;



    public DataInitializer(UserService userService, GenreService genreService, BookService bookService, LanguageService languageService) {
        this.userService = userService;
        this.genreService = genreService;
        this.bookService = bookService;
        this.languageService = languageService;

    }

    @PostConstruct
    public void initData() {
        User admin = this.userService.create(ADMIN, ADMIN, Role.ROLE_ADMIN);

        for (int i = 1; i < 6; i++) {
            this.genreService.create("Genre " + i);
        }

        for (int i = 1; i < 11; i++) {
            this.bookService.create("Book " + i, 20.9 * i, i, Stream.of(1L, i % 5L + 1).collect(Collectors.toList()));
        }
    }
}
