package com.example.egzamin.dto;

import com.example.egzamin.entity.Role;
import com.example.egzamin.entity.User;

public class UserDto {

    private Long id;
    private String username;
    private Role role;

    public UserDto() {
    }

    public UserDto(Long id, String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public static UserDto from(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getRole());
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}