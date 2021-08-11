package com.epam.esm.repository.tag;

import com.epam.esm.TestEnvironment;
import com.epam.esm.TestRepositoryConfig;
import com.epam.esm.builder.TagBuilder;
import com.epam.esm.entity.tag.Tag;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static com.epam.esm.builder.TagBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestRepositoryConfig.class)
@TestPropertySource("classpath:application-test.properties")
public class TagRepositoryTest {

    private final TagBuilder builder = new TagBuilder();

    @Autowired
    private TagRepository tagRepository;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        Flyway flyway = TestEnvironment.getFlyway();
        flyway.clean();
        flyway.migrate();
    }

    @Nested
    class UpdateTest {

        private final TagBuilder builder = new TagBuilder();

        private final Flyway flyway;

        public UpdateTest() throws IOException {
            flyway = TestEnvironment.getFlyway();
        }

        @BeforeEach
        public void flyWayMigrations() {
            flyway.clean();
            flyway.migrate();
        }

        @Test
        void successSave() throws DuplicateKeyException {
            Tag originalTag = builder.withId(NOT_EXIST_TAG_ID).withName(NOT_EXIST_TAG_NAME).build();
            assertEquals(ALL_TAGS_COUNT + 1, tagRepository.save(originalTag).getId());

            assertEquals(ALL_TAGS_COUNT + 1, tagRepository.findAll(Pageable.ofSize(ALL_TAGS_COUNT + 1)).stream().count());
            Tag fetchedTag = tagRepository.findById(ALL_TAGS_COUNT + 1).get(0);
            assertTags(originalTag, fetchedTag);
        }

        @Test
        void successAssignTagToCertificate() {
            assertEquals(0, tagRepository.findAssignedTagToCertificate(CERTIFICATE_ID_WITHOUT_TAG, TAG_ID_WITHOUT_CERTIFICATE).size());
            tagRepository.assignTagToGiftCertificate(CERTIFICATE_ID_WITHOUT_TAG, TAG_ID_WITHOUT_CERTIFICATE);
            assertEquals(1, tagRepository.findAssignedTagToCertificate(CERTIFICATE_ID_WITHOUT_TAG, TAG_ID_WITHOUT_CERTIFICATE).size());
        }

        @Test
        void successDeleteById() {
            assertEquals(1, tagRepository.findById(10).size());
            tagRepository.deleteById(10);
            assertNull(tagRepository.findById(10));
        }

    }

    @Test
    void successFindById() {
        Tag originalTag = builder.build();
        assertEquals(1, tagRepository.findById(1).size());
        Tag fetchedTag = tagRepository.findById(1).get(0);
        assertTags(originalTag, fetchedTag);
    }

    @Test
    void emptyFindOne() {
        assertNull(tagRepository.findById(NOT_EXIST_TAG_ID));
    }

    @Test
    void successFindAll() {
        assertEquals(ALL_TAGS_COUNT, tagRepository.findAll(Pageable.ofSize(ALL_TAGS_COUNT)).stream().count());
    }

    @Test
    void successFindAllMostOrdered() {
        Page<Tag> name = tagRepository.findAllMostOrdered(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name")));
        for (Tag tag: name.getContent()) {
            System.out.println(tag);
        }
        assertEquals(1, tagRepository.findAllMostOrdered(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"))).stream().count());
    }

    @Test
    void successFindAllOrderedWithHighestPrice() {
        assertEquals(2, tagRepository.findAllOrderedWithHighestPrice(PageRequest.ofSize(10)).getContent().size());
    }

    @Test
    void successFindByName() {
        Tag originalTag = builder.build();
        final String tagName = originalTag.getName();
        assertEquals(1, tagRepository.findByName(tagName).size());
        Tag fetchedTag = tagRepository.findByName(tagName).get(0);
        assertTags(originalTag, fetchedTag);
    }

    @Test
    void emptyFindByName() {
        assertEquals(0, tagRepository.findByName(NOT_EXIST_TAG_NAME).size());
    }

    @Test
    void successFindAssignedTagToCertificate() {
        assertEquals(TAG_ID_WITH_CERTIFICATE, tagRepository.findAssignedTagToCertificate(CERTIFICATE_ID_WITH_TAG, TAG_ID_WITH_CERTIFICATE).get(0).getId());
    }

    @Test
    void emptyFindAssignedTagToCertificate() {
        assertEquals(0, tagRepository.findAssignedTagToCertificate(CERTIFICATE_ID_WITHOUT_TAG, TAG_ID_WITHOUT_CERTIFICATE).size());
    }

    @Test
    void successFindByGiftCertificateId() {
        assertEquals(CERTIFICATE_ID_WITH_TAG, tagRepository.findByGiftCertificateId(CERTIFICATE_ID_WITH_TAG, Pageable.ofSize(1)).stream().findFirst().get().getId());
    }

    @Test
    void emptyFindByGiftCertificateId() {
        assertEquals(0, tagRepository.findByGiftCertificateId(CERTIFICATE_ID_WITHOUT_TAG, Pageable.ofSize(1)).stream().count());
    }

    private void assertTags(Tag originalTag, Tag fetchedTag) {
        assertEquals(originalTag.getId(), fetchedTag.getId());
        assertEquals(originalTag.getName(), fetchedTag.getName());
    }
}
