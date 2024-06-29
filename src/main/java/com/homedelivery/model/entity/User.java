package com.homedelivery.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 20)
    private String username;

    @Column(nullable = false)
    @Size(min = 3, max = 40)
    private String fullName;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    @Size(min = 7, max = 15)
    private String phoneNumber;

    @Column(nullable = false)
    @Size(min = 3, max = 40)
    private String address;

    @Column(nullable = false)
    @Size(min = 3, max = 20)
    private String password;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"}))
    private Set<Role> roles;

    @OneToMany(mappedBy = "client")
    private List<Order> orders;

    @OneToMany(mappedBy = "addedBy")
    private List<Comment> comments;

    public User() {
        this.roles = new HashSet<>();
        this.orders = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
