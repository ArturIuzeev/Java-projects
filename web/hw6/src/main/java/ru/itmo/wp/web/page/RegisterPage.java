package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused"})
public class RegisterPage extends Page{

    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    private void register(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        User user = setUser(request, view);
        String password = request.getParameter("password");
        String passwordConfirmation = request.getParameter("passwordConfirmation");

        if (passwordConfirmation.equals(password))
        {
            userService.validateRegistration(user, password);
            userService.register(user, password);
            request.getSession().setAttribute("message", "You are successfully registered!");
            throw new RedirectException("/index");
        } else {
            throw new ValidationException("passwordConfirmation not equals password");
        }
    }
}
