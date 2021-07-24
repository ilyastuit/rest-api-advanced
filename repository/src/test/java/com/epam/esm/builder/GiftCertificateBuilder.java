package com.epam.esm.builder;

import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.tag.Tag;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GiftCertificateBuilder implements Cloneable {

    public final static int EXIST_CERTIFICATE_ID = 1;
    public final static int NOT_EXIST_CERTIFICATE_ID = 7;
    public final static int ID_WITHOUT_ORDER = 4;
    public final static String EXIST_CERTIFICATE_NAME = "Android Development";
    public final static String NOT_EXIST_CERTIFICATE_NAME = "New Certificate";
    public final static String EXIST_CERTIFICATE_DESCRIPTION = "Android Development";
    public final static String NOT_EXIST_CERTIFICATE_DESCRIPTION = "New Certificate";
    public final static BigDecimal EXIST_CERTIFICATE_PRICE = new BigDecimal("200.21");
    public final static BigDecimal NOT_EXIST_CERTIFICATE_PRICE = new BigDecimal("99.999");
    public final static int EXIST_CERTIFICATE_DURATION = 1000;
    public final static int NOT_EXIST_CERTIFICATE_DURATION = 99;
    public final static LocalDateTime EXIST_CERTIFICATE_CREATE_DATE = Timestamp.valueOf("2021-06-24 11:48:23").toLocalDateTime();
    public final static LocalDateTime NOT_EXIST_CERTIFICATE_CREATE_DATE = Timestamp.valueOf("2019-06-24 11:48:23").toLocalDateTime();
    public final static LocalDateTime EXIST_CERTIFICATE_LAST_UPDATE_DATE = Timestamp.valueOf("2021-06-25 23:48:23").toLocalDateTime();
    public final static LocalDateTime NOT_EXIST_CERTIFICATE_LAST_UPDATE_DATE = Timestamp.valueOf("2019-06-24 11:48:23").toLocalDateTime();
    public final static int ALL_CERTIFICATES_COUNT = 6;
    public final static int DEFAULT_CERTIFICATE_ID = 1;
    public final static String ORDER_BY_ASC = "ASC";
    public final static String ORDER_BY_DESC = "DESC";

    public final static int ORDER_BY_DATE_ASC_FIRST_ID = 3;
    public final static int ORDER_BY_DATE_ASC_LAST_ID = 1;
    public final static int ORDER_BY_DATE_DESC_FIRST_ID = 1;
    public final static int ORDER_BY_DATE_DESC_LAST_ID = 3;

    public final static int ORDER_BY_NAME_ASC_FIRST_ID = 1;
    public final static int ORDER_BY_NAME_ASC_LAST_ID = 6;
    public final static int ORDER_BY_NAME_DESC_FIRST_ID = 6;
    public final static int ORDER_BY_NAME_DESC_LAST_ID = 1;

    public final static int ORDER_BY_DATE_AND_NAME_ASC_FIRST_ID = 3;
    public final static int ORDER_BY_DATE_AND_NAME_ASC_LAST_ID = 1;
    public final static int ORDER_BY_DATE_AND_NAME_DESC_FIRST_ID = 1;
    public final static int ORDER_BY_DATE_AND_NAME_DESC_LAST_ID = 3;

    public final static String SEARCH_TEXT = "e";

    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<Tag> tags;

    public GiftCertificateBuilder() {
        this.id = EXIST_CERTIFICATE_ID;
        this.name = EXIST_CERTIFICATE_NAME;
        this.description = EXIST_CERTIFICATE_DESCRIPTION;
        this.price = EXIST_CERTIFICATE_PRICE;
        this.duration = EXIST_CERTIFICATE_DURATION;
        this.createDate = EXIST_CERTIFICATE_CREATE_DATE;
        this.lastUpdateDate = EXIST_CERTIFICATE_LAST_UPDATE_DATE;
        this.tags = getTags();
    }

    public GiftCertificateBuilder withId(Integer id) {
        GiftCertificateBuilder clone = getClone();
        clone.id = id;
        return clone;
    }

    public GiftCertificateBuilder withName(String name) {
        GiftCertificateBuilder clone = getClone();
        clone.name = name;
        return clone;
    }

    public GiftCertificateBuilder withDescription(String description) {
        GiftCertificateBuilder clone = getClone();
        clone.description = description;
        return clone;
    }

    public GiftCertificateBuilder withPrice(BigDecimal price) {
        GiftCertificateBuilder clone = getClone();
        clone.price = price;
        return clone;
    }

    public GiftCertificateBuilder withDuration(int duration) {
        GiftCertificateBuilder clone = getClone();
        clone.duration = duration;
        return clone;
    }

    public GiftCertificateBuilder withCreateDate(LocalDateTime createDate) {
        GiftCertificateBuilder clone = getClone();
        clone.createDate = createDate;
        return clone;
    }

    public GiftCertificateBuilder withLastUpdateDate(LocalDateTime lastUpdateDate) {
        GiftCertificateBuilder clone = getClone();
        clone.lastUpdateDate = lastUpdateDate;
        return clone;
    }

    public GiftCertificateBuilder withTags(List<Tag> tags) {
        GiftCertificateBuilder clone = getClone();
        clone.tags = tags;
        return clone;
    }

    public GiftCertificate build() {
        return new GiftCertificate(
                this.id,
                this.name,
                this.description,
                this.price,
                this.duration,
                this.createDate,
                this.lastUpdateDate,
                this.tags
        );
    }

    public MapSqlParameterSource getPreparedParams() {
        GiftCertificate giftCertificate = build();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", giftCertificate.getName());
        params.addValue("description", giftCertificate.getDescription());
        params.addValue("price", giftCertificate.getPrice());
        params.addValue("duration", giftCertificate.getDuration());
        params.addValue("create_date", Timestamp.valueOf(giftCertificate.getCreateDate()));
        params.addValue("last_update_date", Timestamp.valueOf(giftCertificate.getLastUpdateDate()));

        if (giftCertificate.getTags() != null && !giftCertificate.getTags().isEmpty()) {
            params.addValue("tags", giftCertificate.getTags());
        }

        return params;
    }

    private List<Tag> getTags() {
        List<Tag> tags = new ArrayList<>(3);
        tags.add(new Tag(1, "android"));
        tags.add(new Tag(3, "mobile"));
        tags.add(new Tag(5, "programming"));
        return tags;
    }

    private GiftCertificateBuilder getClone() {
        GiftCertificateBuilder clone = null;
        try {
            clone = (GiftCertificateBuilder) this.clone();
        } catch (CloneNotSupportedException ignored) {
        }
        return clone;
    }
}
