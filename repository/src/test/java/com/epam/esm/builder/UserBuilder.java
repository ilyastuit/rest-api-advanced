package com.epam.esm.builder;

import com.epam.esm.entity.order.Order;
import com.epam.esm.entity.user.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class UserBuilder implements Cloneable{

    public final static int EXIST_USER_ID = 1;
    public final static int NOT_EXIST_USER_ID = 6;
    public final static String EXIST_USER_EMAIL = "admin@mail.ru";
    public final static String NOT_EXIST_USER_EMAIL = "not_exist_email.ru";
    public final static String EXIST_USER_PASSWORD = "123";
    public final static String NOT_EXIST_USER_PASSWORD = "321";
    public final static LocalDateTime EXIST_CERTIFICATE_CREATE_DATE = Timestamp.valueOf("2020-10-09 10:48:23").toLocalDateTime();
    public final static LocalDateTime NOT_EXIST_CERTIFICATE_CREATE_DATE = Timestamp.valueOf("2012-10-09 10:48:23").toLocalDateTime();
    public final static LocalDateTime EXIST_CERTIFICATE_LAST_UPDATE_DATE = Timestamp.valueOf("2021-02-01 08:48:23").toLocalDateTime();
    public final static LocalDateTime NOT_EXIST_CERTIFICATE_LAST_UPDATE_DATE = Timestamp.valueOf("2011-02-01 08:48:23").toLocalDateTime();
    public final static int ALL_USERS_COUNT = 5;

    private int id;
    private String email;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    private List<Order> orders;

    public UserBuilder() {
        this.id = EXIST_USER_ID;
        this.email = EXIST_USER_EMAIL;
        this.password = EXIST_USER_PASSWORD;
        this.createDate = EXIST_CERTIFICATE_CREATE_DATE;
        this.lastUpdateDate = EXIST_CERTIFICATE_LAST_UPDATE_DATE;
    }

    public UserBuilder withId(int id) {
        UserBuilder clone = getClone();
        clone.id = id;
        return clone;
    }

    public UserBuilder withEmail(String email) {
        UserBuilder clone = getClone();
        clone.email = email;
        return clone;
    }

    public UserBuilder withPassword(String password) {
        UserBuilder clone = getClone();
        clone.password = password;
        return clone;
    }

    public UserBuilder withCreateDate(LocalDateTime createDate) {
        UserBuilder clone = getClone();
        clone.createDate = createDate;
        return clone;
    }

    public UserBuilder withLastUpdateDate(LocalDateTime lastUpdateDate) {
        UserBuilder clone = getClone();
        clone.lastUpdateDate = lastUpdateDate;
        return clone;
    }

    public UserBuilder withOrders(List<Order> orders) {
        UserBuilder clone = getClone();
        clone.orders = orders;
        return clone;
    }

    public User build() {
        return new User(
                this.id,
                this.email,
                this.password,
                this.createDate,
                this.lastUpdateDate,
                this.orders
        );
    }

    private UserBuilder getClone() {
        UserBuilder clone = null;
        try {
            clone = (UserBuilder) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
