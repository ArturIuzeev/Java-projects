package ru.itmo.wp.web.page;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.EventService;
import ru.itmo.wp.model.service.TalkService;
import ru.itmo.wp.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public abstract class Page {
    protected final UserService userService = new UserService();
    protected final EventService eventService = new EventService();

    protected final TalkService talkService = new TalkService();

    protected void before(HttpServletRequest request, Map<String, Object> view) {
        view.put("userCount", userService.findCount());
        putUser(request, view);
    }
    protected void after(HttpServletRequest request, Map<String, Object> view)
    {
        view.put("userCount", userService.findCount());
    }

    protected void putMessage(HttpServletRequest request, Map<String, Object> view) {
        String message = (String) request.getSession().getAttribute("message");
        if (!Strings.isNullOrEmpty(message)) {
            view.put("message", message);
            request.getSession().removeAttribute("message");
        }
    }

    protected void setMessage(HttpServletRequest request, Map<String, Object> view, String message) {
        if (!Strings.isNullOrEmpty(message)) {
            request.getSession().setAttribute("message", message);
        }
    }

    protected User getUser(HttpServletRequest request, Map<String, Object> view) {
        return (User) request.getSession().getAttribute("user");
    }

    protected User setUser(HttpServletRequest request, Map<String, Object> view) {
        User user = new User();
        user.setLoginOrEmail(request.getParameter("loginOrEmail"));
        user.setEmail(request.getParameter("email"));
        return user;
    }

    private void putUser(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            view.put("user", user);
        }
    }
}
