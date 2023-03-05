package ru.itmo.wp.service;

import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.Notice;
import ru.itmo.wp.form.NoticeCredentials;
import ru.itmo.wp.repository.NoticeRepository;

import java.util.List;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }


    public List<Notice> findAll() {
        return noticeRepository.findAllByOrderByIdDesc();
    }

    public Notice saveNotice(NoticeCredentials noticeForm) {
        Notice notice = new Notice();
        notice.setContent(noticeForm.getContent());
        noticeRepository.save(notice);
        return notice;
    }
}
