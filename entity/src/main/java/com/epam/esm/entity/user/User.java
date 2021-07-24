package com.epam.esm.entity.user;

import com.epam.esm.entity.order.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private int id;
    private String email;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    private List<Order> orders;

    public User() {
    }

    public User(int id, String email, String password, LocalDateTime createDate, LocalDateTime lastUpdateDate, List<Order> orders) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.setCreateDate(createDate);
        this.setLastUpdateDate(lastUpdateDate);
        this.orders = orders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        if (createDate == null) {
            this.createDate = LocalDateTime.now();
        } else {
            this.createDate = createDate;
        }
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        if (lastUpdateDate == null) {
            this.lastUpdateDate = LocalDateTime.now();
        } else {
            this.lastUpdateDate = lastUpdateDate;
        }
    }

    public List<Order> getOrders() {
        if (this.orders == null) {
            this.orders = new ArrayList<>();
        }
        return this.orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addTag(Order order) {
        if (this.orders == null) {
            this.orders = new ArrayList<>();
        }
        this.orders.add(order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && email.equals(user.email) && password.equals(user.password) && createDate.equals(user.createDate) && lastUpdateDate.equals(user.lastUpdateDate) && orders.equals(user.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, createDate, lastUpdateDate, orders);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", orders=" + orders +
                '}';
    }
}
