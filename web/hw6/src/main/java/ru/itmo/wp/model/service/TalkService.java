package ru.itmo.wp.model.service;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.TalkRepository;
import ru.itmo.wp.model.repository.impl.TalkRepositoryImpl;

import java.util.List;

public class TalkService {
    private final TalkRepository talkRepository = new TalkRepositoryImpl();

    public void save(Talk talk) {
        talkRepository.save(talk);
    }

    public List<Talk> findByUserId(long id) {
        return talkRepository.findByUserId(id);
    }

    public void checkText(String text) throws ValidationException {
        if (text.length() > 256)
        {
            throw new ValidationException("Message size bigger than 255");
        }
    }
}
