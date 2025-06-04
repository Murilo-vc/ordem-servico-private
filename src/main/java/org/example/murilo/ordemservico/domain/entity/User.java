package org.example.murilo.ordemservico.domain.entity;


import org.example.murilo.ordemservico.enumeration.UserRoleEnum;

public class User {

    private Integer id;

    private final String username;

    private final String name;

    private final String password;

    private final UserRoleEnum role;

    public User(final int id,
                final String username,
                final String name,
                final String password,
                final UserRoleEnum role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public User(final String username,
                final String name,
                final String password,
                final UserRoleEnum role) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public UserRoleEnum getRole() {
        return role;
    }
}
