package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TalkPage extends Page {


    private List<Talk> data = new ArrayList<>();

    private void action(HttpServletRequest request, Map<String, Object> view) {

    }

    @Override
    protected void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        view.put("users", userService.findAll());
        if (getUser(request, view) == null) {
            throw new RedirectException("/index");
        }
        request.getSession().setAttribute("user", getUser(request, view));
        data = talkService.findByUserId(getUser(request, view).getId());
        if (!data.contains(null)) {
            view.put("talks", data);
        }
    }

    @Override
    protected void after(HttpServletRequest request, Map<String, Object> view) {

    }

    private void send(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        String text = request.getParameter("text").trim();
        userService.findByLogin(request.getParameter("targetUser"));
        talkService.checkText(text);

        long sourceUserId = getUser(request, view).getId();
        long targetUserId = userService.findByLogin(request.getParameter("targetUser")).getId();
        if (!"".equals(text)) {
            Talk talk = new Talk();
            talk.setSourceUserId(sourceUserId);
            talk.setTargetUserId(targetUserId);
            talk.setText(text);
            talkService.save(talk);
        }

        throw new RedirectException("/talk");
    }
}
