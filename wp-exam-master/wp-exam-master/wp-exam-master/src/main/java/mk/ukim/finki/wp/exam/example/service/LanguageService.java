package mk.ukim.finki.wp.exam.example.service;
import mk.ukim.finki.wp.exam.example.model.Language;
import org.springframework.stereotype.Service;

import java.util.List;

public interface LanguageService {

    List<Language> listAll();

}
