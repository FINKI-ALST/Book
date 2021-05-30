package mk.ukim.finki.wp.exam.example.web;
import mk.ukim.finki.wp.exam.example.model.Genre;
import mk.ukim.finki.wp.exam.example.model.Book;
import mk.ukim.finki.wp.exam.example.service.GenreService;
import mk.ukim.finki.wp.exam.example.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class BooksController {

    private final BookService service;
    private final GenreService genreService;

    public BooksController(BookService service, GenreService genreService) {
        this.service = service;
        this.genreService = genreService;
    }

    @GetMapping({"/", "/books"})
    public String showProducts(@RequestParam (required = false)String nameSearch, @RequestParam (required = false) Long genreId, Model model) {
        List <Book> books;
        List<Genre> genres = this.genreService.listAll();

        if (nameSearch == null && genreId == null) {
            books=this.service.listAllBooks();
        } else {
            books=this.service.listBooksByNameAndGenre(nameSearch, genreId);
        }
         model.addAttribute("books", books);
        model.addAttribute("genres", genres);

        return "list.html";
    }

    @GetMapping("/books/add")
    public String showAdd(Model model) {

        List<Genre> genres = this.genreService.listAll();
        model.addAttribute("genres", genres);
        return "form.html";
    }

    @GetMapping("/books/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model) {
       Book book = this.service.findById(id);

        List<Genre> genres = this.genreService.listAll();
        model.addAttribute("book", book);
        model.addAttribute("genres", genres);
        return "form.html";
    }


    @PostMapping("/books")
    public String create(@RequestParam String name, @RequestParam Double price,
                         @RequestParam Integer quantity, @RequestParam List<Long> genres) {
        this.service.create(name, price, quantity, genres);
        return "redirect:/books";
    }

    @PostMapping("/books/{id}")
    public String update(@PathVariable Long id, @RequestParam String name, @RequestParam Double price,
                         @RequestParam Integer quantity, @RequestParam List<Long> genres) {
        this.service.update(id, name, price, quantity, genres);
        return "redirect:/books";
    }

    @PostMapping("/books/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.service.delete(id);
        return "redirect:/books";
    }

   /* @RequestMapping(value = "/books/{url}")
    public String displayProductByUrl(@PathVariable("url") String url, Model model) {

        Book book = service.fingBooksByUrl(url);
        model.addAttribute("book",book);
        Review newReview = new Review();
        newReview.setBook(book);
        newReview.setRating(5);
        model.addAttribute("review",newReview);
        return "book";
    } */


}
