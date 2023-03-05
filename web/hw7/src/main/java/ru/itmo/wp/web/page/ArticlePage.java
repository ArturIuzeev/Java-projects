package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ArticlePage {
    private final ArticleService articleService = new ArticleService();
    private final UserService userService = new UserService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        if (request.getSession().getAttribute("user") == null)
        {
            throw new RedirectException("/index");
        }
        // No operations.
    }

    private void save(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        String title = request.getParameter("title");
        String text = request.getParameter("text");
        User user = (User) request.getSession().getAttribute("user");

        articleService.validateSaveText(title);
        articleService.validateSaveText(text);
        userService.find(user.getId());

        Article article = new Article();
        article.setUserId(user.getId());
        article.setTitle(title);
        article.setText(text);


        articleService.save(article, user);

        request.getSession().setAttribute("article", article);

        throw new RedirectException("/article");
    }



//    private void findArticle(HttpServletRequest request, Map<String, Object> view) {
//        view.put("article",
//                userService.find(Long.parseLong(request.getParameter("userId"))));
//    }
}
