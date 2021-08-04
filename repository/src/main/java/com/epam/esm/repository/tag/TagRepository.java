package com.epam.esm.repository.tag;

import com.epam.esm.entity.tag.Tag;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final TagResultSetExtractor tagResultSetExtractor = new TagResultSetExtractor();

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public TagRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    public int save(Tag tag) throws DuplicateKeyException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String SQL = "INSERT INTO gifts.tag (name) VALUES (:name)";

        this.namedParameterJdbcTemplate.update(SQL, new MapSqlParameterSource().addValue("name",tag.getName()), keyHolder, new String[] {"id"});
        return keyHolder.getKey().intValue();
    }

    public void deleteById(int id) {
        final String SQL = "DELETE FROM gifts.tag WHERE id = ?";
        transactionTemplate.execute(status -> {
            this.jdbcTemplate.update(SQL, id);
            return null;
        });
    }

    public void assignTagToGiftCertificate(int certificateId, int tagId) {
        final String SQL = "INSERT INTO gifts.gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, ?)";
        transactionTemplate.execute(status -> {
            jdbcTemplate.update(SQL, certificateId, tagId);
            return null;
        });
    }

    public List<Tag> findById(int id) {
        return jdbcTemplate.query("SELECT t.id, t.name FROM gifts.tag t WHERE t.id=?", this.tagResultSetExtractor, id);
    }

    public Page<Tag> findAll(Pageable pageable) {
        StringBuilder builder = new StringBuilder("SELECT t.id, t.name FROM gifts.tag t ");

        final String SQL = getPageableStatement(builder, pageable);
        long count = countAll();

        return new PageImpl<>(
                jdbcTemplate.query(SQL, this.tagResultSetExtractor),
                pageable,
                (int) count);
    }

    public int countAll() {
        return this.jdbcTemplate.queryForObject("select count(t.id) from gifts.tag t", Integer.class);
    }

    public List<Tag> findByName(String name) {
        final String SQL = "SELECT t.id, t.name FROM gifts.tag t WHERE t.name = ?";
        return this.jdbcTemplate.query(SQL, this.tagResultSetExtractor, name);
    }

    public List<Tag> findAssignedTagToCertificate(int certificateId, int tagId) {
        final String SQL = "SELECT t.id as id, t.name as name FROM gifts.tag t LEFT JOIN gifts.gift_certificate_tag gct ON t.id = gct.tag_id WHERE gct.gift_certificate_id = ? AND gct.tag_id = ?";
        return this.jdbcTemplate.query(SQL, this.tagResultSetExtractor, certificateId, tagId);
    }

    public Page<Tag> findByGiftCertificateId(Integer certificateId, Pageable pageable) {
        StringBuilder builder = new StringBuilder("SELECT t.id, t.name FROM gifts.tag t LEFT JOIN gifts.gift_certificate_tag gct ON gct.tag_id = t.id LEFT JOIN gifts.gift_certificate gc ON gct.gift_certificate_id = gc.id WHERE gc.id = ? ");

        final String SQL = getPageableStatement(builder, pageable);
        long count = countAllByCertificate(certificateId);

        return new PageImpl<>(
                jdbcTemplate.query(SQL, this.tagResultSetExtractor, certificateId),
                pageable,
                (int) count);
    }

    public int countAllByCertificate(Integer certificateId) {
        return this.jdbcTemplate.queryForObject("SELECT count(t.id) FROM gifts.tag t LEFT JOIN gifts.gift_certificate_tag gct ON gct.tag_id = t.id LEFT JOIN gifts.gift_certificate gc ON gct.gift_certificate_id = gc.id WHERE gc.id = ? " ,Integer.class, certificateId);
    }

    public Page<Tag> findAllOrderedWithHighestPrice(Pageable pageable) {
        StringBuilder builder = new StringBuilder("SELECT distinct t.* FROM gifts.tag t left join gifts.gift_certificate_tag gct on gct.tag_id = t.id left join orders.gift_certificate ogc on ogc.gift_certificate_id = gct.gift_certificate_id WHERE ogc.price = (select MAX(ogct.price) from gifts.tag t left join gifts.gift_certificate_tag gct on gct.tag_id = t.id left join orders.gift_certificate ogct on ogct.gift_certificate_id = gct.id having MAX(ogct.price) is not null) ");

        final String SQL = getPageableStatement(builder, pageable);
        long count = countAllOrderedWithHighestPrice();

        return new PageImpl<>(
                jdbcTemplate.query(SQL, this.tagResultSetExtractor),
                pageable,
                (int) count);
    }

    public int countAllOrderedWithHighestPrice() {
        return jdbcTemplate.queryForObject("SELECT count(distinct t.*) FROM gifts.tag t left join gifts.gift_certificate_tag gct on gct.tag_id = t.id left join orders.gift_certificate ogc on ogc.gift_certificate_id = gct.gift_certificate_id WHERE ogc.price = (select MAX(ogct.price) from gifts.tag t left join gifts.gift_certificate_tag gct on gct.tag_id = t.id left join orders.gift_certificate ogct on ogct.gift_certificate_id = gct.id having MAX(ogct.price) is not null)", Integer.class);
    }

    public Page<Tag> findAllMostOrdered(Pageable pageable) {
        final String preparedQuery = "SELECT distinct t.* FROM gifts.tag t\n" +
                "         inner join gifts.gift_certificate_tag gct on gct.tag_id = t.id\n" +
                "         inner join orders.gift_certificate ogc on ogc.gift_certificate_id = gct.gift_certificate_id WHERE t.id in (select t.id from gifts.tag t\n" +
                "                        inner join gifts.gift_certificate_tag gct on gct.tag_id = t.id\n" +
                "                        inner join orders.gift_certificate ogct on ogct.gift_certificate_id = gct.gift_certificate_id GROUP BY t.id HAVING COUNT(t.id) = (select MAX(all_max.frequency) from (select COUNT(t.id) as frequency from gifts.tag t\n" +
                "                                                    inner join gifts.gift_certificate_tag gct on gct.tag_id = t.id\n" +
                "                                                    inner join orders.gift_certificate ogct on ogct.gift_certificate_id = gct.gift_certificate_id GROUP BY t.id) as all_max)) ";
        StringBuilder builder = new StringBuilder(preparedQuery);

        final String SQL = getPageableStatement(builder, pageable);
        long count = countAllMostOrdered();

        return new PageImpl<>(
                jdbcTemplate.query(SQL, this.tagResultSetExtractor),
                pageable,
                (int) count);
    }

    public int countAllMostOrdered() {
        return jdbcTemplate.queryForObject(new StringBuilder()
                .append("SELECT count(distinct t.*) FROM gifts.tag t ")
                .append(" inner join gifts.gift_certificate_tag gct on gct.tag_id = t.id ")
                .append(" inner join orders.gift_certificate ogc on ogc.gift_certificate_id = gct.gift_certificate_id WHERE t.id in (select t.id from gifts.tag t")
                .append(" inner join gifts.gift_certificate_tag gct on gct.tag_id = t.id")
                .append(" inner join orders.gift_certificate ogct on ogct.gift_certificate_id = gct.gift_certificate_id GROUP BY t.id HAVING COUNT(t.id) = (select MAX(all_max.frequency) from (select COUNT(t.id) as frequency from gifts.tag t ")
                .append(" inner join gifts.gift_certificate_tag gct on gct.tag_id = t.id ")
                .append(" inner join orders.gift_certificate ogct on ogct.gift_certificate_id = gct.gift_certificate_id GROUP BY t.id) as all_max)) ").toString(), Integer.class);
    }

    private String getPageableStatement(StringBuilder SQL, Pageable pageable) {
        for (Sort.Order o : pageable.getSort()) {
            SQL.append(" ORDER BY t.")
                    .append(o.getProperty())
                    .append(" ")
                    .append(o.getDirection()).append(" ");
        }
        int offset = Math.max(pageable.getPageNumber() - 1, 0);
        SQL.append(" OFFSET ")
                .append(offset * pageable.getPageSize())
                .append("ROWS ")
                .append("FETCH NEXT ")
                .append(pageable.getPageSize())
                .append("ROWS ONLY");

        return SQL.toString();
    }
}
