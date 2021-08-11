package com.epam.esm.repository.giftcertificate;

import com.epam.esm.entity.giftcertificate.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GiftCertificateRepository extends PagingAndSortingRepository<GiftCertificate, Integer> {

    @Transactional
    public GiftCertificate save(GiftCertificate giftCertificate);

    @Transactional
    public void deleteById(int id);

    @Modifying
    @Transactional
    @Query("UPDATE GiftCertificate SET name = :name WHERE id = :certificateId")
    public void changeNameById(int certificateId, String name);

    public List<GiftCertificate> findById(int id);

    public Page<GiftCertificate> findAll(Pageable pageable);

    @Query("SELECT gc from GiftCertificate gc left join gc.tags t WHERE t.name in (:tagNames)")
    public Page<GiftCertificate> findAllByTagNames(String[] tagNames, Pageable pageable);

    @Query("SELECT distinct gc from GiftCertificate gc WHERE gc.name like %:text% or gc.description like %:text%")
    public Page<GiftCertificate> findAllByNameOrDescription(String text, Pageable pageable);
}
