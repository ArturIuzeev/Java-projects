package ru.itmo.wp.form;

import javax.validation.constraints.*;

public class NoticeCredentials {
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 255)
    @NotBlank
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
