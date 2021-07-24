package com.epam.esm.repository.tag;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.esm.builder.TagBuilder;
import com.epam.esm.TestEnvironment;
import com.epam.esm.entity.tag.Tag;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;

public class TagRepositoryUpdateTest {

    private final TagBuilder tagBuilder = new TagBuilder();

    private static TagRepository tagRepository;
    private static Flyway flyway;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        tagRepository = TestEnvironment.getTagRepository();
        flyway = TestEnvironment.getFlyway();
    }

    @BeforeEach
    public void flyWayMigrations() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void successSave() throws DuplicateKeyException {
        Tag originalTag = tagBuilder.withId(TagBuilder.NOT_EXIST_TAG_ID).withName(TagBuilder.NOT_EXIST_TAG_NAME).build();
        assertEquals(TagBuilder.ALL_TAGS_COUNT + 1, tagRepository.save(originalTag));

        assertEquals(TagBuilder.ALL_TAGS_COUNT + 1, tagRepository.findAll().size());
        Tag fetchedTag = tagRepository.findById(TagBuilder.ALL_TAGS_COUNT + 1).get(0);
        assertTags(originalTag, fetchedTag);
    }

    @Test
    void failSave() {
        assertThrows(DuplicateKeyException.class, () -> {
            tagRepository.save(tagBuilder.withName(TagBuilder.EXIST_TAG_NAME).build());
        });
    }

    @Test
    void successDeleteById() {
        assertEquals(1, tagRepository.findAssignedTagToCertificate(TagBuilder.CERTIFICATE_ID_WITH_TAG, TagBuilder.TAG_ID_WITH_CERTIFICATE).size());
        assertEquals(1, tagRepository.findById(TagBuilder.TAG_ID_WITH_CERTIFICATE).size());
        tagRepository.deleteById(1);
        assertEquals(0, tagRepository.findAssignedTagToCertificate(TagBuilder.CERTIFICATE_ID_WITH_TAG, TagBuilder.TAG_ID_WITH_CERTIFICATE).size());
        assertEquals(0, tagRepository.findById(TagBuilder.TAG_ID_WITH_CERTIFICATE).size());
    }

    private void assertTags(Tag originalTag, Tag fetchedTag) {
        assertEquals(originalTag.getId(), fetchedTag.getId());
        assertEquals(originalTag.getName(), fetchedTag.getName());
    }
}
