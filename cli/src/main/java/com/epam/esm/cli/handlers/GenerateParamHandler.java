package com.epam.esm.cli.handlers;

import com.epam.esm.cli.infrastructure.param.CommandData;
import com.epam.esm.cli.infrastructure.param.annotations.ParamMapper;
import com.epam.esm.cli.infrastructure.param.handler.Handler;
import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.user.User;
import com.github.javafaker.Faker;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ParamMapper(name = "generate")
public class GenerateParamHandler implements Handler {

    private final Faker faker = new Faker(new Locale("en-US"));

    public final static int GIFTS_COUNT = 10_000;
    public final static int TAGS_COUNT = 1000;
    public final static int USERS_COUNT = 1000;

    private final CommandData data;
    private final JdbcTemplate jdbcTemplate;

    public GenerateParamHandler(CommandData data, JdbcTemplate jdbcTemplate) {
        this.data = data;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void handle() {
        if (data.getValue().equals("true")) {
            batchInsertFakeData();
            System.out.println("GENERATING IS DONE!");
        }
    }

    private void batchInsertFakeData() {
        List<GiftCertificate> certificates = generateGiftsFakeData();
        List<Tag> tags = generateTagsFakeData();
        List<User> users = generateUsersFakeData();

        batchInsertCertificates(certificates);
        batchInsertTags(tags);
        batchInsertUsers(users);
    }

    private List<GiftCertificate> generateGiftsFakeData() {
        List<GiftCertificate> certificates = new ArrayList<>(GIFTS_COUNT);

        for (int i = 1; i <= GIFTS_COUNT; i++) {
            String name = faker.gameOfThrones().character();
            String description = faker.gameOfThrones().quote();
            BigDecimal price = BigDecimal.valueOf(faker.random().nextDouble() * 100.0).setScale(2,4);
            int duration = faker.random().nextInt(100, 999);
            LocalDateTime createDate = LocalDateTime.now();
            LocalDateTime lastUpdateDate = LocalDateTime.now();

            GiftCertificate giftCertificate = new GiftCertificate(
                    null,
                    name,
                    description,
                    price,
                    duration,
                    createDate,
                    lastUpdateDate,
                    null,
                    null
            );

            certificates.add(giftCertificate);
        }

        return certificates;
    }

    private List<Tag> generateTagsFakeData() {
        List<Tag> tags = new ArrayList<>(TAGS_COUNT);

        for (int i = 1; i <= TAGS_COUNT ; i++) {
            String name = faker.funnyName().name() + i;

            Tag tag = new Tag(name);

            tags.add(tag);
        }

        return tags;
    }

    private List<User> generateUsersFakeData() {
        List<User> users = new ArrayList<>(USERS_COUNT);

        for (int i = 1; i <= USERS_COUNT ; i++) {
            String email = faker.internet().emailAddress() + i;
            String password = faker.internet().password();
            LocalDateTime createDate = LocalDateTime.now();
            LocalDateTime lastUpdateDate = LocalDateTime.now();

            User user = new User(
                    null,
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

    private void batchInsertCertificates(List<GiftCertificate> certificates) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO gifts.gift_certificate (name, description, price, duration, create_date, last_update_date) VALUES (?,?,?,?,?,?)",
                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, certificates.get(i).getName());
                        ps.setString(2, certificates.get(i).getDescription());
                        ps.setBigDecimal(3, certificates.get(i).getPrice());
                        ps.setInt(4, certificates.get(i).getDuration());
                        ps.setTimestamp(5, Timestamp.valueOf(certificates.get(i).getCreateDate()));
                        ps.setTimestamp(6, Timestamp.valueOf(certificates.get(i).getLastUpdateDate()));
                    }

                    public int getBatchSize() {
                        return certificates.size();
                    }

                });
    }

    private void batchInsertTags(List<Tag> tags) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO gifts.tag (name) VALUES (?)",
                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, tags.get(i).getName());
                    }

                    public int getBatchSize() {
                        return tags.size();
                    }

                });
    }

    private void batchInsertUsers(List<User> users) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO users.list (email, password, create_date, last_update_date) VALUES (?,?,?,?)",
                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, users.get(i).getEmail());
                        ps.setString(2, users.get(i).getPassword());
                        ps.setTimestamp(3, Timestamp.valueOf(users.get(i).getCreateDate()));
                        ps.setTimestamp(4, Timestamp.valueOf(users.get(i).getLastUpdateDate()));
                    }

                    public int getBatchSize() {
                        return users.size();
                    }

                });
    }
}
