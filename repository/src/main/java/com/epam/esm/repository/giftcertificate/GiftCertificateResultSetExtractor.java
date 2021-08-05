package com.epam.esm.repository.giftcertificate;

import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.repository.tag.TagRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GiftCertificateResultSetExtractor implements ResultSetExtractor<List<GiftCertificate>> {

    private boolean withTags = false;
    private TagRepository tagRepository;

    public GiftCertificateResultSetExtractor() {
    }

    public GiftCertificateResultSetExtractor(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<GiftCertificate> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<GiftCertificate> giftList = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            GiftCertificate giftCertificate = new GiftCertificate(id);
            extractRow(giftCertificate, resultSet);

            if (this.withTags) {
                int pageSize = tagRepository.countAllByCertificate(giftCertificate.getId()) <= 0 ? 1 : tagRepository.countAllByCertificate(giftCertificate.getId());
                giftCertificate.setTags(
                        tagRepository.findByGiftCertificateId(
                                giftCertificate.getId(),
                                Pageable.ofSize(
                                        pageSize
                                )).getContent());
            }

            giftList.add(giftCertificate);
        }

        return giftList;
    }

    public GiftCertificateResultSetExtractor withTags() {
        this.withTags = true;
        return this;
    }

    private void extractRow(GiftCertificate giftCertificate, ResultSet resultSet) throws SQLException {
        giftCertificate.setName(resultSet.getString("name"));
        giftCertificate.setDescription(resultSet.getString("description"));
        giftCertificate.setPrice(resultSet.getBigDecimal("price"));
        giftCertificate.setDuration(resultSet.getInt("duration"));
        giftCertificate.setCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime());
        giftCertificate.setLastUpdateDate(resultSet.getTimestamp("last_update_date").toLocalDateTime());
    }
}
