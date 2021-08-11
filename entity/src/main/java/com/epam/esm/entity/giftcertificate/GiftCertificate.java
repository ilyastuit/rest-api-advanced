package com.epam.esm.entity.giftcertificate;

import com.epam.esm.entity.order.Order;
import com.epam.esm.entity.tag.Tag;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "gift_certificate", schema = "gifts")
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(precision = 19, scale = 4)
    private BigDecimal price;

    private Integer duration;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(
            schema = "gifts",
            name = "gift_certificate_tag",
            joinColumns = { @JoinColumn(name = "gift_certificate_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    private List<Tag> tags;

    @OneToMany(mappedBy = "giftCertificate", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private List<Order> orders;

    public GiftCertificate(Integer id) {
        this.id = id;
    }


}
