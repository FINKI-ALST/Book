package mk.ukim.finki.wp.exam.example.repository;
import mk.ukim.finki.wp.exam.example.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.jaas.JaasPasswordCallbackHandler;
import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
