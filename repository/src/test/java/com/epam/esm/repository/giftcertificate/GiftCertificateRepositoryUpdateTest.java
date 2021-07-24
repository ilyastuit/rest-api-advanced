package com.epam.esm.repository.giftcertificate;

import com.epam.esm.builder.GiftCertificateBuilder;
import com.epam.esm.builder.TagBuilder;
import com.epam.esm.TestEnvironment;
import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.tag.Tag;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GiftCertificateRepositoryUpdateTest {

    private static GiftCertificateRepository giftCertificateRepository;
    private static Flyway flyway;

    private final GiftCertificateBuilder builder = new GiftCertificateBuilder();
    private final TagBuilder tagBuilder = new TagBuilder();

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        giftCertificateRepository = TestEnvironment.getGiftCertificateRepository();
        flyway = TestEnvironment.getFlyway();
    }

    @BeforeEach
    public void flyWayMigrations() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void successSave() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAll().size());

        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT + 1, giftCertificateRepository.save(builder.getPreparedParams()));

        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT + 1, giftCertificateRepository.findById(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT + 1).get(0).getId());
    }

    @Test
    void successSaveWithNotExistTag() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAll().size());

        List<Tag> notExistTags = new ArrayList<>();
        notExistTags.add(tagBuilder.withName(TagBuilder.NOT_EXIST_TAG_NAME).build());
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT + 1, giftCertificateRepository.save(builder.withTags(notExistTags).getPreparedParams()));

        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT + 1, giftCertificateRepository.findById(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT + 1).get(0).getId());
    }

    @Test
    void successUpdate() {
        assertEquals(GiftCertificateBuilder.DEFAULT_CERTIFICATE_ID, giftCertificateRepository.update(GiftCertificateBuilder.DEFAULT_CERTIFICATE_ID, builder.getPreparedParams()));

        GiftCertificate originalCertificate = builder.withId(GiftCertificateBuilder.DEFAULT_CERTIFICATE_ID).build();
        assertEquals(1, giftCertificateRepository.findById(GiftCertificateBuilder.DEFAULT_CERTIFICATE_ID).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findById(GiftCertificateBuilder.DEFAULT_CERTIFICATE_ID).get(0);

        assertCertificates(originalCertificate, fetchedCertificate);
    }

    @Test
    void successDeleteById() {
        GiftCertificate originalCertificate = builder.withId(GiftCertificateBuilder.ID_WITHOUT_ORDER).build();
        giftCertificateRepository.deleteById(originalCertificate.getId());
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1, giftCertificateRepository.findAll().size());
        assertEquals(0, giftCertificateRepository.findById(originalCertificate.getId()).size());
    }

    @Test
    void failDeleteById() {
        GiftCertificate originalCertificate = builder.build();
        assertThrows(DataIntegrityViolationException.class, () -> {
            giftCertificateRepository.deleteById(originalCertificate.getId());
        });
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAll().size());
        assertEquals(1, giftCertificateRepository.findById(originalCertificate.getId()).size());
    }

    @Test
    void successChangeName() {
        GiftCertificate originalCertificate = builder.build();
        String newName = "New Name to Certificate";

        assertEquals(originalCertificate.getName(), giftCertificateRepository.findById(GiftCertificateBuilder.DEFAULT_CERTIFICATE_ID).get(0).getName());
        giftCertificateRepository.changeName(originalCertificate.getId(), newName);
        GiftCertificate fetchedCertificate = giftCertificateRepository.findById(GiftCertificateBuilder.DEFAULT_CERTIFICATE_ID).get(0);

        assertEquals(newName, fetchedCertificate.getName());
        assertNotEquals(fetchedCertificate.getName(), originalCertificate.getName());
    }

    private void assertCertificates(GiftCertificate originalCertificate, GiftCertificate fetchedCertificate) {
        assertEquals(originalCertificate.getId(), fetchedCertificate.getId());
        assertEquals(originalCertificate.getName(), fetchedCertificate.getName());
        assertEquals(originalCertificate.getDescription(), fetchedCertificate.getDescription());
        assertEquals(originalCertificate.getPrice(), fetchedCertificate.getPrice());
        assertEquals(originalCertificate.getDuration(), fetchedCertificate.getDuration());
        assertEquals(originalCertificate.getCreateDate(), fetchedCertificate.getCreateDate());
        //assertEquals(originalCertificate.getLastUpdateDate(), fetchedCertificate.getLastUpdateDate());
    }

    private void assertTags(GiftCertificate originalCertificate, GiftCertificate fetchedCertificate) {
        List<Tag> originalTags = originalCertificate.getTags();
        List<Tag> fetchedTags = fetchedCertificate.getTags();
        assertEquals(originalTags.size(), fetchedTags.size());

        for (int i = 0; i < originalTags.size(); i++) {
            Tag originalTag = originalTags.get(i);
            Tag fetchedTag = fetchedTags.get(i);
            assertEquals(originalTag.getId(), fetchedTag.getId());
            assertEquals(originalTag.getName(), fetchedTag.getName());
        }
    }
}
