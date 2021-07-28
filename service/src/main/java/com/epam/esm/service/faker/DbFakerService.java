package com.epam.esm.service.faker;

import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.user.User;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class DbFakerService {

    private final Faker faker = new Faker(new Locale("en-US"));

    public final static int GIFTS_COUNT = 10_000;
    public final static int TAGS_COUNT = 1000;
    public final static int USERS_COUNT = 1000;

    public List<GiftCertificate> generateGiftsFakeData() {
        List<GiftCertificate> certificates = new ArrayList<>(GIFTS_COUNT);

        for (int i = 1; i <= GIFTS_COUNT; i++) {
            int id = i;
            String name = faker.gameOfThrones().character();
            String description = faker.gameOfThrones().quote();
            BigDecimal price = BigDecimal.valueOf(faker.random().nextDouble() * 100.0).setScale(2,4);
            int duration = faker.random().nextInt(100, 999);
            LocalDateTime createDate = LocalDateTime.now();
            LocalDateTime lastUpdateDate = LocalDateTime.now();

            GiftCertificate giftCertificate = new GiftCertificate(
                    id,
                    name,
                    description,
                    price,
                    duration,
                    createDate,
                    lastUpdateDate,
                    null
            );

            certificates.add(giftCertificate);
        }

        return certificates;
    }

    public List<Tag> generateTagsFakeData() {
        List<Tag> tags = new ArrayList<>(TAGS_COUNT);

        for (int i = 1; i <= TAGS_COUNT ; i++) {
            int id = i;
            String name = faker.funnyName().name() + i;

            Tag tag = new Tag(id, name);

            tags.add(tag);
        }

        return tags;
    }

    public List<User> generateUsersFakeData() {
        List<User> users = new ArrayList<>(USERS_COUNT);

        for (int i = 1; i <= USERS_COUNT ; i++) {
            int id = i;
            String email = faker.internet().emailAddress() + i;
            String password = faker.internet().password();
            LocalDateTime createDate = LocalDateTime.now();
            LocalDateTime lastUpdateDate = LocalDateTime.now();

            User user = new User(
                    id,
                    email,
                    password,
                    createDate,
                    lastUpdateDate,
                    null
            );

            users.add(user);
        }

        return users;
    }

}
