package com.epam.esm.repository.tag;

import com.epam.esm.entity.tag.Tag;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Repository
public interface TagRepository extends PagingAndSortingRepository<Tag, Integer> {

    @Transactional
    public Tag save(Tag tag);

    @Transactional
    public void deleteById(int id);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO gifts.gift_certificate_tag (gift_certificate_id, tag_id) VALUES (:certificateId, :tagId)", nativeQuery = true)
    public void assignTagToGiftCertificate(int certificateId, int tagId);

    public List<Tag> findById(int id);

    public Page<Tag> findAll(Pageable pageable);

    public List<Tag> findByName(String name);

    @Query("SELECT t FROM Tag t LEFT JOIN t.certificates gc WHERE gc.id = :certificateId and t.id = :tagId")
    public List<Tag> findAssignedTagToCertificate(int certificateId, int tagId);

    @Query("SELECT t FROM Tag t LEFT JOIN t.certificates gc WHERE gc.id = :certificateId")
    public Page<Tag> findByGiftCertificateId(Integer certificateId, Pageable pageable);

    @Query(value = "SELECT distinct t.* FROM gifts.tag t left join gifts.gift_certificate_tag gct on gct.tag_id = t.id left join orders.gift_certificate ogc on ogc.gift_certificate_id = gct.gift_certificate_id WHERE ogc.price = (select MAX(ogct.price) from gifts.tag t left join gifts.gift_certificate_tag gct on gct.tag_id = t.id left join orders.gift_certificate ogct on ogct.gift_certificate_id = gct.id having MAX(ogct.price) is not null) ", nativeQuery = true)
    public Page<Tag> findAllOrderedWithHighestPrice(Pageable pageable);

    @Query(value = "SELECT distinct t.* FROM gifts.tag t\n" +
            "         inner join gifts.gift_certificate_tag gct on gct.tag_id = t.id\n" +
            "         inner join orders.gift_certificate ogc on ogc.gift_certificate_id = gct.gift_certificate_id WHERE t.id in (select t.id from gifts.tag t\n" +
            "                        inner join gifts.gift_certificate_tag gct on gct.tag_id = t.id\n" +
            "                        inner join orders.gift_certificate ogct on ogct.gift_certificate_id = gct.gift_certificate_id GROUP BY t.id HAVING COUNT(t.id) = (select MAX(all_max.frequency) from (select COUNT(t.id) as frequency from gifts.tag t\n" +
            "                                                    inner join gifts.gift_certificate_tag gct on gct.tag_id = t.id\n" +
            "                                                    inner join orders.gift_certificate ogct on ogct.gift_certificate_id = gct.gift_certificate_id GROUP BY t.id) as all_max)) ", nativeQuery = true)
    public Page<Tag> findAllMostOrdered(Pageable pageable);
}
