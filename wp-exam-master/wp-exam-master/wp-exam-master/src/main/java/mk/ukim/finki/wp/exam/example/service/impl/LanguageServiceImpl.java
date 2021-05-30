package mk.ukim.finki.wp.exam.example.service.impl;
import mk.ukim.finki.wp.exam.example.model.Language;
import mk.ukim.finki.wp.exam.example.repository.LanguageRepository;
import mk.ukim.finki.wp.exam.example.service.LanguageService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public List<Language> listAll() {
        return this.languageRepository.findAll();

    }
}
