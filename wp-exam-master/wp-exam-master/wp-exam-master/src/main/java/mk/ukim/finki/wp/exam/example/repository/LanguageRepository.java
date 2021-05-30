package mk.ukim.finki.wp.exam.example.repository;
import mk.ukim.finki.wp.exam.example.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Integer> {

}
