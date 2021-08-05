package com.epam.esm.entity.giftcertificate;

import com.epam.esm.entity.tag.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GiftCertificate {

    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;

    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    private List<Tag> tags;

    public GiftCertificate() {
    }

    public GiftCertificate(Integer id) {
        this.id = id;
    }

    public GiftCertificate(Integer id, String name, String description, BigDecimal price, Integer duration, LocalDateTime createDate, LocalDateTime lastUpdateDate, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.setCreateDate(createDate);
        this.setLastUpdateDate(lastUpdateDate);
        this.tags = tags;
    }

    public GiftCertificate(String name, String description, BigDecimal price, Integer duration, LocalDateTime createDate, LocalDateTime lastUpdateDate, List<Tag> tags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.setCreateDate(createDate);
        this.setLastUpdateDate(lastUpdateDate);
        this.tags = tags;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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

    public List<Tag> getTags() {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        return this.tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificate that = (GiftCertificate) o;
        return id.equals(that.id) && duration == that.duration && name.equals(that.name) && description.equals(that.description) && price.equals(that.price) && createDate.equals(that.createDate) && lastUpdateDate.equals(that.lastUpdateDate) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, createDate, lastUpdateDate, tags);
    }

    @Override
    public String toString() {
        return "GiftCertificate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", tags=" + tags +
                '}';
    }
}
