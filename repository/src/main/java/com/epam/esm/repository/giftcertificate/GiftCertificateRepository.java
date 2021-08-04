package com.epam.esm.repository.giftcertificate;

import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.repository.tag.TagRepository;
import org.springframework.dao.DataIntegrityViolationException;
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
public class GiftCertificateRepository {

    private final GiftCertificateResultSetExtractor resultSetExtractor = new GiftCertificateResultSetExtractor();

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final TransactionTemplate transactionTemplate;
    private final TagRepository tagRepository;

    public GiftCertificateRepository(JdbcTemplate jdbcTemplate,
                                     NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                     TransactionTemplate transactionTemplate,
                                     TagRepository tagRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.transactionTemplate = transactionTemplate;
        this.tagRepository = tagRepository;
    }

    public int save(MapSqlParameterSource params) {
        final String SQL = "INSERT INTO gifts.gift_certificate (name, description, price, duration, create_date, last_update_date) VALUES (:name, :description, :price, :duration, :create_date, :last_update_date)";
        return updateQuery(params, SQL);
    }

    public int update(Integer id, MapSqlParameterSource params) {
        final String SQL = "UPDATE gifts.gift_certificate SET name = :name, description = :description, price = :price, duration = :duration, last_update_date = :last_update_date WHERE id = :id";
        params.addValue("id", id);
        return updateQuery(params, SQL);
    }

    public void deleteById(int id) throws DataIntegrityViolationException {
        String SQL = "DELETE FROM gifts.gift_certificate WHERE id=?";
        this.jdbcTemplate.update(SQL, id);
    }

    public void changeName(int certificateId, String newName) {
        String SQL = "UPDATE gifts.gift_certificate SET name = ? WHERE id = ?";
        this.jdbcTemplate.update(SQL, newName, certificateId);
    }

    public long countAll() {
        String SQL = "SELECT COUNT(id) from gifts.gift_certificate";
        return this.jdbcTemplate.queryForObject(SQL, Long.class);
    }

    public List<GiftCertificate> findById(int id) {
        String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date from gifts.gift_certificate gc WHERE gc.id=?";
        return jdbcTemplate.query(SQL, this.resultSetExtractor, id);
    }

    public List<GiftCertificate> findByIdWithTags(int id) {
        String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc WHERE gc.id=?";
        return jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository).withTags(), id);
    }

    public Page<GiftCertificate> findAll(Pageable pageable) {
        StringBuilder builder = new StringBuilder("SELECT gc.* from gifts.gift_certificate gc");

        final String SQL = getPageableStatement(builder, pageable);
        long count = countAll();

        return new PageImpl<>(
                jdbcTemplate.query(SQL, this.resultSetExtractor),
                pageable,
                (int) count);
    }

    public Page<GiftCertificate> findAllWithTags(Pageable pageable) {
        StringBuilder builder = new StringBuilder("SELECT gc.* from gifts.gift_certificate gc");

        final String SQL = getPageableStatement(builder, pageable);
        long count = countAll();

        return new PageImpl<>(
                jdbcTemplate.query(SQL, this.resultSetExtractor),
                pageable,
                (int) count);
    }

    public Page<GiftCertificate> findAllWithTagsByTagNames(String[] tagNames, Pageable pageable) {
        StringBuilder SQL = new StringBuilder("SELECT gc.* FROM gifts.gift_certificate gc LEFT JOIN gifts.gift_certificate_tag gct ON gct.gift_certificate_id = gc.id LEFT JOIN gifts.tag t ON gct.tag_id = t.id WHERE t.name in ( ");

        appendIn(SQL, tagNames);

        String query = getPageableStatement(SQL, pageable);

        return new PageImpl<>(
                jdbcTemplate.query(query, new GiftCertificateResultSetExtractor(tagRepository).withTags(), (Object[]) tagNames),
                pageable,
                countTagsByTagNames(tagNames));
    }

    public int countTagsByTagNames(String[] tagNames) {
        StringBuilder SQL = new StringBuilder("SELECT count(gc.id) FROM gifts.gift_certificate gc LEFT JOIN gifts.gift_certificate_tag gct ON gct.gift_certificate_id = gc.id LEFT JOIN gifts.tag t ON gct.tag_id = t.id WHERE t.name in ( ");

        appendIn(SQL, tagNames);

        return jdbcTemplate.queryForObject(SQL.toString(), Integer.class, (Object[]) tagNames);
    }

    public Page<GiftCertificate> findAllByNameOrDescription(String text, Pageable pageable) {
        String likeText = this.prepareText(text);
        StringBuilder builder = new StringBuilder("SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc WHERE gc.name LIKE ? OR gc.description LIKE ? ");
        String SQL = getPageableStatement(builder, pageable);

        return new PageImpl<>(
                jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository).withTags(), likeText, likeText),
                pageable,
                countByNameOrDescription(likeText));
    }

    public Page<GiftCertificate> findAllWithTagsByNameOrDescription(String text, Pageable pageable) {
        String likeText = this.prepareText(text);
        StringBuilder builder = new StringBuilder("SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc WHERE gc.name LIKE ? OR gc.description LIKE ? ");
        String SQL = getPageableStatement(builder, pageable);

        return new PageImpl<>(
                jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository).withTags(), likeText, likeText),
                pageable,
                countByNameOrDescription(likeText));
    }

    public int countByNameOrDescription(String likeText) {
        String SQL = "SELECT count(gc.id) FROM gifts.gift_certificate gc WHERE gc.name LIKE ? OR gc.description LIKE ? ";

        return jdbcTemplate.queryForObject(SQL, Integer.class, likeText, likeText);
    }

    private int updateQuery(MapSqlParameterSource params, String SQL) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        transactionTemplate.execute(status -> {
            namedParameterJdbcTemplate.update(SQL, params, keyHolder, new String[]{"id"});
            return null;
        });

        return keyHolder.getKey().intValue();
    }

    private String prepareText(String text) {
        return "%" + text + "%";
    }

    private String getPageableStatement(StringBuilder SQL, Pageable pageable) {
        for (Sort.Order o : pageable.getSort()) {
            SQL.append(" ORDER BY ")
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

    private void appendIn(StringBuilder builder, String[] values) {
        if (values.length == 1) {
            builder.append("? ");
        } else {
            for (int i = 0; i < values.length; i++) {
                builder.append("?, ");
            }
            builder.deleteCharAt(builder.length() - 2);
        }
        builder.append(") ");
    }
}
