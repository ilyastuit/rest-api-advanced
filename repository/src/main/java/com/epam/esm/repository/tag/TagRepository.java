package com.epam.esm.repository.tag;

import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.tag.TagDTO;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Repository
public class TagRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public TagRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    public List<Tag> findById(int id) {
        return jdbcTemplate.query("SELECT t.id, t.name FROM gifts.tag t WHERE t.id=?", new TagResultSetExtractor(), id);
    }

    public List<Tag> findAll() {
        return jdbcTemplate.query("SELECT t.id, t.name FROM gifts.tag t", new TagResultSetExtractor());
    }

    public int save(Tag tag) throws DuplicateKeyException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String SQL = "INSERT INTO gifts.tag (name) VALUES (:name)";

        this.namedParameterJdbcTemplate.update(SQL, new MapSqlParameterSource().addValue("name",tag.getName()), keyHolder, new String[] {"id"});
        return keyHolder.getKey().intValue();
    }

    public List<Tag> findByName(String name) {
        final String SQL = "SELECT t.id, t.name FROM gifts.tag t WHERE t.name = ?";
        return this.jdbcTemplate.query(SQL, new TagResultSetExtractor(), name);
    }

    public List<Tag> findAssignedTagToCertificate(int certificateId, int tagId) {
        final String SQL = "SELECT t.id as id, t.name as name FROM gifts.tag t LEFT JOIN gifts.gift_certificate_tag gct ON t.id = gct.tag_id WHERE gct.gift_certificate_id = ? AND gct.tag_id = ?";
        return this.jdbcTemplate.query(SQL, new TagResultSetExtractor(), certificateId, tagId);
    }

    public void assignTagToGiftCertificate(int certificateId, int tagId) {
        final String SQL = "INSERT INTO gifts.gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, ?)";
        transactionTemplate.execute(status -> {
            jdbcTemplate.update(SQL, certificateId, tagId);
            return null;
        });
    }

    public List<Tag> findByGiftCertificateId(Integer certificateId) {
        final String SQL = "SELECT t.id, t.name FROM gifts.tag t LEFT JOIN gifts.gift_certificate_tag gct ON gct.tag_id = t.id LEFT JOIN gifts.gift_certificate gc ON gct.gift_certificate_id = gc.id WHERE gc.id = ?";
        return this.jdbcTemplate.query(SQL, new TagResultSetExtractor(), certificateId);
    }

    public void deleteById(int id) {
        final String SQL = "DELETE FROM gifts.tag WHERE id = ?";
        transactionTemplate.execute(status -> {
            this.jdbcTemplate.update(SQL, id);
            return null;
        });
    }

    public List<Tag> findAllOrderedWithHighestPrice() {
        final String SQL = "SELECT distinct t.* FROM gifts.tag t left join gifts.gift_certificate_tag gct on gct.tag_id = t.id left join orders.gift_certificate ogc on ogc.gift_certificate_id = gct.gift_certificate_id WHERE ogc.price = (select MAX(ogct.price) from gifts.tag t left join gifts.gift_certificate_tag gct on gct.tag_id = t.id left join orders.gift_certificate ogct on ogct.gift_certificate_id = gct.id having MAX(ogct.price) is not null)";
        return jdbcTemplate.query(SQL, new TagResultSetExtractor());
    }

    public List<Tag> findAllMostOrdered() {
        final String SQL = "SELECT distinct t.* FROM gifts.tag t\n" +
                "         inner join gifts.gift_certificate_tag gct on gct.tag_id = t.id\n" +
                "         inner join orders.gift_certificate ogc on ogc.gift_certificate_id = gct.gift_certificate_id WHERE t.id in (select t.id from gifts.tag t\n" +
                "                        inner join gifts.gift_certificate_tag gct on gct.tag_id = t.id\n" +
                "                        inner join orders.gift_certificate ogct on ogct.gift_certificate_id = gct.gift_certificate_id GROUP BY t.id HAVING COUNT(t.id) = (select MAX(all_max.frequency) from (select COUNT(t.id) as frequency from gifts.tag t\n" +
                "                                                    inner join gifts.gift_certificate_tag gct on gct.tag_id = t.id\n" +
                "                                                    inner join orders.gift_certificate ogct on ogct.gift_certificate_id = gct.gift_certificate_id GROUP BY t.id) as all_max))";
        return jdbcTemplate.query(SQL, new TagResultSetExtractor());
    }
}
