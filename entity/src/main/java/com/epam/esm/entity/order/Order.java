package com.epam.esm.entity.order;

import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "gift_certificate", schema = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(precision = 19, scale = 4)
    private BigDecimal price;

    @Convert(converter = OrderStatusConvertor.class)
    private OrderStatus status;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @ManyToOne(cascade=CascadeType.MERGE)
    private User user;

    @ManyToOne(cascade=CascadeType.MERGE)
    private GiftCertificate giftCertificate;

    public Order(BigDecimal price, OrderStatus status, LocalDateTime orderDate, LocalDateTime lastUpdateDate, User user, GiftCertificate giftCertificate) {
        this.price = price;
        this.status = status;
        this.orderDate = orderDate;
        this.lastUpdateDate = lastUpdateDate;
        this.user = user;
        this.giftCertificate = giftCertificate;
    }
}
