package com.epam.esm.entity.order;

import com.epam.esm.entity.giftcertificate.GiftCertificateDTO;
import com.epam.esm.entity.user.UserDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class OrderDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal price;

    private OrderStatus status;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime orderDate;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime lastUpdateDate;

    private UserDTO user;
    private GiftCertificateDTO giftCertificate;

    public OrderDTO() {
    }

    public OrderDTO(int id,
                    BigDecimal price,
                    OrderStatus status,
                    LocalDateTime orderDate,
                    LocalDateTime lastUpdateDate,
                    UserDTO user,
                    GiftCertificateDTO giftCertificate) {
        this.id = id;
        this.price = price;
        this.status = status;
        this.orderDate = orderDate;
        this.lastUpdateDate = lastUpdateDate;
        this.user = user;
        this.giftCertificate = giftCertificate;
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
        this.orderDate = orderDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public GiftCertificateDTO getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificateDTO giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return id == orderDTO.id && price.equals(orderDTO.price) && status == orderDTO.status && orderDate.equals(orderDTO.orderDate) && lastUpdateDate.equals(orderDTO.lastUpdateDate) && user.equals(orderDTO.user) && giftCertificate.equals(orderDTO.giftCertificate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, status, orderDate, lastUpdateDate, user, giftCertificate);
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
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
