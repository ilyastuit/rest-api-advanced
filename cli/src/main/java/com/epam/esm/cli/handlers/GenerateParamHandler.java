package com.epam.esm.cli.handlers;

import com.epam.esm.cli.infrastructure.param.CommandData;
import com.epam.esm.cli.infrastructure.param.annotations.ParamMapper;
import com.epam.esm.cli.infrastructure.param.handler.Handler;
import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.user.User;
import com.epam.esm.service.faker.DbFakerService;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@ParamMapper(name = "generate")
public class GenerateParamHandler implements Handler {

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

        DbFakerService fakerService = new DbFakerService();

        List<GiftCertificate> certificates = fakerService.generateGiftsFakeData();
        List<Tag> tags = fakerService.generateTagsFakeData();
        List<User> users = fakerService.generateUsersFakeData();

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
