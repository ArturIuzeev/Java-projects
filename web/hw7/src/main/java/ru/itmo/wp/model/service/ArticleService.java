package ru.itmo.wp.model.service;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.ArticleRepository;

import ru.itmo.wp.model.repository.impl.ArticleRepositoryImpl;

import java.util.List;

public class ArticleService {
    private final ArticleRepository articleService = new ArticleRepositoryImpl();

    public void validateSaveText(String text) throws ValidationException {
        if (text == null)
        {
            throw new ValidationException("Text is empty");
        }
        if (Strings.isNullOrEmpty(text.trim())) {
            throw new ValidationException("Text is empty");
        }
        if (text.length() > 255)
        {
            throw new ValidationException("Text has big size");
        }
    }

    public void save(Article article , User user) {
        articleService.save(article, user);
    }

    public List<Article> findAll() {
        return articleService.findAll();
    }
}
