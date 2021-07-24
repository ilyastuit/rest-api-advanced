package com.epam.esm.repository.tag;

import com.epam.esm.TestEnvironment;
import com.epam.esm.builder.TagBuilder;
import com.epam.esm.entity.tag.Tag;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagRepositoryIdempotentTest {

    private final TagBuilder tagBuilder = new TagBuilder();

    private static TagRepository tagRepository;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        tagRepository = TestEnvironment.getTagRepository();
        Flyway flyway = TestEnvironment.getFlyway();
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void successFindById() {
        Tag originalTag = tagBuilder.build();
        assertEquals(1, tagRepository.findById(1).size());
        Tag fetchedTag = tagRepository.findById(1).get(0);
        assertTags(originalTag, fetchedTag);
    }

    @Test
    void emptyFindOne() {
        assertEquals(0, tagRepository.findById(TagBuilder.NOT_EXIST_TAG_ID).size());
    }

    @Test
    void successFindAll() {
        assertEquals(TagBuilder.ALL_TAGS_COUNT, tagRepository.findAll().size());
    }

    @Test
    void successFindByName() {
        Tag originalTag = tagBuilder.build();
        final String tagName = originalTag.getName();
        assertEquals(1, tagRepository.findByName(tagName).size());
        Tag fetchedTag = tagRepository.findByName(tagName).get(0);
        assertTags(originalTag, fetchedTag);
    }

    @Test
    void emptyFindByName() {
        assertEquals(0, tagRepository.findByName(TagBuilder.NOT_EXIST_TAG_NAME).size());
    }

    @Test
    void successFindAssignedTagToCertificate() {
        assertEquals(TagBuilder.TAG_ID_WITH_CERTIFICATE, tagRepository.findAssignedTagToCertificate(TagBuilder.CERTIFICATE_ID_WITH_TAG, TagBuilder.TAG_ID_WITH_CERTIFICATE).get(0).getId());
    }

    @Test
    void emptyFindAssignedTagToCertificate() {
        assertEquals(0, tagRepository.findAssignedTagToCertificate(TagBuilder.CERTIFICATE_ID_WITHOUT_TAG, TagBuilder.TAG_ID_WITHOUT_CERTIFICATE).size());
    }

    @Test
    void successFindByGiftCertificateId() {
        assertEquals(TagBuilder.CERTIFICATE_ID_WITH_TAG, tagRepository.findByGiftCertificateId(TagBuilder.CERTIFICATE_ID_WITH_TAG).get(0).getId());
    }

    @Test
    void emptyFindByGiftCertificateId() {
        assertEquals(0, tagRepository.findByGiftCertificateId(TagBuilder.CERTIFICATE_ID_WITHOUT_TAG).size());
    }

    private void assertTags(Tag originalTag, Tag fetchedTag) {
        assertEquals(originalTag.getId(), fetchedTag.getId());
        assertEquals(originalTag.getName(), fetchedTag.getName());
    }
}
