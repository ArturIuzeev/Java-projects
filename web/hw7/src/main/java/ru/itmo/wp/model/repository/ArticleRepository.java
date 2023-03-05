package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;

import java.util.List;


public interface ArticleRepository {
    void save(Article article, User user);

    List<Article> findAll();
}
