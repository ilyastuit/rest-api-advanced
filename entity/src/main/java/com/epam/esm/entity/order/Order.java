package com.epam.esm.entity.order;

import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Order {

    private int id;
    private BigDecimal price;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private LocalDateTime lastUpdateDate;

    private User user;
    private GiftCertificate giftCertificate;

    public Order() {
    }

    public Order(int id, BigDecimal price, OrderStatus status, User user, GiftCertificate giftCertificate, LocalDateTime orderDate, LocalDateTime lastUpdateDate) {
        this.id = id;
        this.price = price;
        this.status = status;
        this.user = user;
        this.giftCertificate = giftCertificate;
        this.setOrderDate(orderDate);
        this.setLastUpdateDate(lastUpdateDate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        if (orderDate == null) {
            this.orderDate = LocalDateTime.now();
        } else {
            this.orderDate = orderDate;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && price.equals(order.price) && status == order.status && orderDate.equals(order.orderDate) && Objects.equals(lastUpdateDate, order.lastUpdateDate) && user.equals(order.user) && giftCertificate.equals(order.giftCertificate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, status, orderDate, lastUpdateDate, user, giftCertificate);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", price=" + price +
                ", status=" + status +
                ", orderDate=" + orderDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", user=" + user +
                ", giftCertificate=" + giftCertificate +
                '}';
    }
}
