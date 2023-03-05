package ru.itmo.web.hw4.model;

public class User {
    private final long id;
    private final String handle;
    private final String name;

    public User(long id, String handle, String name) {
        this.id = id;
        this.handle = handle;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getHandle() {
        return handle;
    }

    public String getName() {
        return name;
    }
}
