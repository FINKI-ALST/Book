package mk.ukim.finki.wp.exam.example.model;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "book")

public class Book {

    public Book() {
    }

    public Book(String name, Double price, Integer quantity, List<Genre> genres) {

        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.genres = genres;
    }


    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToMany
    private List<Genre> genres;

    @ManyToMany
    private List<Language> languages;

   // @ManyToOne
   // private Author author;

    @ManyToOne
    private User creator;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }






}
