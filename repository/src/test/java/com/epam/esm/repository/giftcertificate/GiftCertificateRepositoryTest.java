package com.epam.esm.repository.giftcertificate;

import com.epam.esm.TestEnvironment;
import com.epam.esm.builder.GiftCertificateBuilder;
import com.epam.esm.builder.TagBuilder;
import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.tag.Tag;
import com.epam.esm.TestRepositoryConfig;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.epam.esm.builder.GiftCertificateBuilder.*;



@SpringBootTest(classes = TestRepositoryConfig.class)
@TestPropertySource("classpath:application-test.properties")
public class GiftCertificateRepositoryTest {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    private final GiftCertificateBuilder builder = new GiftCertificateBuilder();
    private final TagBuilder tagBuilder = new TagBuilder();

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        Flyway flyway = TestEnvironment.getFlyway();
        flyway.clean();
        flyway.migrate();
    }

    @Nested
    class UpdateTest {
        private final Flyway flyway;

        private final GiftCertificateBuilder builder = new GiftCertificateBuilder();
        private final TagBuilder tagBuilder = new TagBuilder();

        public UpdateTest() throws IOException {
            this.flyway = TestEnvironment.getFlyway();
        }

        @BeforeEach
        public void flyWayMigrations() {
            flyway.clean();
            flyway.migrate();
        }

        @Test
        void successSave() {
            assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAll(Pageable.ofSize(ALL_CERTIFICATES_COUNT)).getTotalElements());

            assertEquals(EXIST_CERTIFICATE_ID, giftCertificateRepository.save(builder.build()).getId());

            assertEquals(EXIST_CERTIFICATE_ID, giftCertificateRepository.findById(EXIST_CERTIFICATE_ID).get(0).getId());
        }

        @Test
        void successSaveWithNotExistTag() {
            assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAll(Pageable.ofSize(ALL_CERTIFICATES_COUNT)).getTotalElements());

            List<Tag> notExistTags = new ArrayList<>();
            notExistTags.add(tagBuilder.withName(TagBuilder.NOT_EXIST_TAG_NAME).build());
            assertEquals(EXIST_CERTIFICATE_ID, giftCertificateRepository.save(builder.withTags(notExistTags).build()).getId());

            assertEquals(EXIST_CERTIFICATE_ID, giftCertificateRepository.findById(EXIST_CERTIFICATE_ID).get(0).getId());
        }

        @Test
        void successUpdate() {
            assertEquals(DEFAULT_CERTIFICATE_ID, giftCertificateRepository.save(builder.build()).getId());

            GiftCertificate originalCertificate = builder.withId(DEFAULT_CERTIFICATE_ID).build();
            assertEquals(1, giftCertificateRepository.findById(DEFAULT_CERTIFICATE_ID).size());
            GiftCertificate fetchedCertificate = giftCertificateRepository.findById(DEFAULT_CERTIFICATE_ID).get(0);

            assertCertificates(originalCertificate, fetchedCertificate);
        }

        @Test
        void successDeleteById() {
            GiftCertificate originalCertificate = builder.withId(6).build();
            giftCertificateRepository.deleteById(originalCertificate.getId());
            assertEquals(ALL_CERTIFICATES_COUNT - 1, (long) giftCertificateRepository.findAll(Pageable.unpaged()).getContent().size());
            assertFalse(giftCertificateRepository.findById(originalCertificate.getId()).isPresent());
        }

        @Test
        void successChangeName() {
            GiftCertificate originalCertificate = builder.build();
            String newName = "New Name to Certificate";

            assertEquals(originalCertificate.getName(), giftCertificateRepository.findById(DEFAULT_CERTIFICATE_ID).get(0).getName());
            giftCertificateRepository.changeNameById(originalCertificate.getId(), newName);
            GiftCertificate fetchedCertificate = giftCertificateRepository.findById(DEFAULT_CERTIFICATE_ID).get(0);

            assertEquals(newName, fetchedCertificate.getName());
            assertNotEquals(fetchedCertificate.getName(), originalCertificate.getName());
        }

    }

    @Test
    void successFindById() {
        GiftCertificate originalCertificate = builder.withId(EXIST_CERTIFICATE_ID).build();
        int originalCertificateId = originalCertificate.getId();

        assertEquals(1, giftCertificateRepository.findById(originalCertificateId).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findById(originalCertificateId).get(0);

        assertCertificates(originalCertificate, fetchedCertificate);
    }

    @Test
    void successFindByIdWithTags() {
        GiftCertificate originalCertificate = builder.withId(EXIST_CERTIFICATE_ID).build();
        int originalCertificateId = originalCertificate.getId();

        assertEquals(1, giftCertificateRepository.findById(originalCertificateId).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findById(originalCertificateId).get(0);

        assertCertificates(originalCertificate, fetchedCertificate);
        assertTags(originalCertificate, fetchedCertificate);
    }

    @Test
    void emptyFindById() {
        assertNull(giftCertificateRepository.findById(NOT_EXIST_CERTIFICATE_ID));
    }

    @Test
    void successFindAll() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAll(PageRequest.of(0, ALL_CERTIFICATES_COUNT, Sort.by(Sort.Direction.ASC, "name"))).getTotalElements());
    }

    @Test
    void successFindAllWithTags() {
        assertEquals(ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAll(Pageable.ofSize(ALL_CERTIFICATES_COUNT)).getTotalElements());
    }

    @Test
    void successFindAllWithTagsByTagName() {
        Tag originalTag = tagBuilder.withId(TagBuilder.EXIST_TAG_ID).withName(TagBuilder.EXIST_TAG_NAME).build();
        assertEquals(1, giftCertificateRepository.findAllByTagNames(new String[]{originalTag.getName()}, Pageable.ofSize(1)).getTotalElements());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findAllByTagNames(new String[]{originalTag.getName()}, Pageable.ofSize(1)).stream().findAny().orElse(null);
        assertNotNull(fetchedCertificate);

        Tag fetchedTag = fetchedCertificate.getTags()
                .stream()
                .filter((tag) -> tag.getName().equals(originalTag.getName()))
                .findFirst().get();

        assertEquals(originalTag.getId(), fetchedTag.getId());
    }

    @Test
    void emptyFindAllWithTagsByTagName() {
        Tag originalTag = tagBuilder.withName(TagBuilder.NOT_EXIST_TAG_NAME).build();
        assertEquals(0, giftCertificateRepository.findAllByTagNames(new String[]{originalTag.getName()}, Pageable.ofSize(1)).getTotalElements());
    }

    @Test
    void emptyFindAllWithTagsBySeveralTags() {
        Tag originalTag = tagBuilder.withName(TagBuilder.NOT_EXIST_TAG_NAME).build();
        assertEquals(0, giftCertificateRepository.findAllByTagNames(new String[]{originalTag.getName(), "second"}, Pageable.ofSize(1)).getTotalElements());
    }

    @Test
    void successFindAllByName() {
        GiftCertificate originalCertificate = builder.build();
        assertEquals(1, giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getName(), Pageable.ofSize(1)).getTotalElements());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getName(), Pageable.ofSize(1)).stream().findAny().orElse(null);
        assertNotNull(fetchedCertificate);

        assertCertificates(originalCertificate, fetchedCertificate);
        assertTags(originalCertificate, fetchedCertificate);
    }

    @Test
    void emptyFindAllByName() {
        GiftCertificate originalCertificate = builder.withName(NOT_EXIST_CERTIFICATE_NAME).build();
        assertEquals(0, giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getName(), Pageable.ofSize(1)).getTotalElements());
    }

    @Test
    void successFindAllByDescription() {
        GiftCertificate originalCertificate = builder.build();
        assertEquals(1, giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getDescription(), Pageable.ofSize(1)).getTotalElements());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getDescription(), Pageable.ofSize(1)).stream().findAny().orElse(null);
        assertNotNull(fetchedCertificate);

        assertCertificates(originalCertificate, fetchedCertificate);
        assertTags(originalCertificate, fetchedCertificate);
    }

    @Test
    void emptyFindAllByDescription() {
        GiftCertificate originalCertificate = builder.withDescription(NOT_EXIST_CERTIFICATE_DESCRIPTION).build();
        assertEquals(0, giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getDescription(), Pageable.ofSize(1)).getTotalElements());
    }

    @Test
    void successFindAllWithTagsByName() {
        GiftCertificate originalCertificate = builder.build();
        assertEquals(1, giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getName(), Pageable.ofSize(1)).getTotalElements());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getName(), Pageable.ofSize(1)).stream().findAny().orElse(null);
        assertNotNull(fetchedCertificate);

        assertCertificates(originalCertificate, fetchedCertificate);
        assertTags(originalCertificate, fetchedCertificate);
    }

    @Test
    void emptyFindAllAllWithTagsByName() {
        GiftCertificate originalCertificate = builder.withName(NOT_EXIST_CERTIFICATE_NAME).build();
        assertEquals(0, giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getName(), Pageable.ofSize(1)).getTotalElements());
    }

    @Test
    void successFindAllAllWithTagsByDescription() {
        GiftCertificate originalCertificate = builder.build();
        assertEquals(1, giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getDescription(), Pageable.ofSize(1)).getTotalElements());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getDescription(), Pageable.ofSize(1)).stream().findAny().orElse(null);
        assertNotNull(fetchedCertificate);

        assertCertificates(originalCertificate, fetchedCertificate);
        assertTags(originalCertificate, fetchedCertificate);
    }

    @Test
    void emptyFindAllAllWithTagsByDescription() {
        GiftCertificate originalCertificate = builder.withDescription(NOT_EXIST_CERTIFICATE_DESCRIPTION).build();
        assertEquals(0, giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getDescription(), Pageable.ofSize(1)).getTotalElements());
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
