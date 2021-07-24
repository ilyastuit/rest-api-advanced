package com.epam.esm.repository.giftcertificate;

import com.epam.esm.TestEnvironment;
import com.epam.esm.builder.GiftCertificateBuilder;
import com.epam.esm.builder.TagBuilder;
import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.tag.Tag;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GiftCertificateRepositoryIdempotentTest {

    private static GiftCertificateRepository giftCertificateRepository;

    private final GiftCertificateBuilder builder = new GiftCertificateBuilder();
    private final TagBuilder tagBuilder = new TagBuilder();

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        giftCertificateRepository = TestEnvironment.getGiftCertificateRepository();
        Flyway flyway = TestEnvironment.getFlyway();
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void successFindById() {
        GiftCertificate originalCertificate = builder.withId(GiftCertificateBuilder.EXIST_CERTIFICATE_ID).build();
        int originalCertificateId = originalCertificate.getId();

        assertEquals(1, giftCertificateRepository.findById(originalCertificateId).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findById(originalCertificateId).get(0);

        assertCertificates(originalCertificate, fetchedCertificate);
    }

    @Test
    void successFindByIdWithTags() {
        GiftCertificate originalCertificate = builder.withId(GiftCertificateBuilder.EXIST_CERTIFICATE_ID).build();
        int originalCertificateId = originalCertificate.getId();

        assertEquals(1, giftCertificateRepository.findByIdWithTags(originalCertificateId).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findByIdWithTags(originalCertificateId).get(0);

        assertCertificates(originalCertificate, fetchedCertificate);
        assertTags(originalCertificate, fetchedCertificate);
    }

    @Test
    void emptyFindById() {
        assertEquals(0, giftCertificateRepository.findById(GiftCertificateBuilder.NOT_EXIST_CERTIFICATE_ID).size());
    }

    @Test
    void successFindAll() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAll().size());
    }

    @Test
    void successFindAllWithTags() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllWithTags().size());
    }

    @Test
    void successFindAllWithTagsByTagName() {
        Tag originalTag = tagBuilder.withId(TagBuilder.EXIST_TAG_ID).withName(TagBuilder.EXIST_TAG_NAME).build();
        assertEquals(1, giftCertificateRepository.findAllWithTagsByTagNames(new String[] {originalTag.getName()}).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findAllWithTagsByTagNames(new String[] {originalTag.getName()}).get(0);

        Tag fetchedTag = fetchedCertificate.getTags()
                .stream()
                .filter((tag) -> tag.getName().equals(originalTag.getName()))
                .findFirst().get();

        assertEquals(originalTag.getId(), fetchedTag.getId());
    }

    @Test
    void emptyFindAllWithTagsByTagName() {
        Tag originalTag = tagBuilder.withName(TagBuilder.NOT_EXIST_TAG_NAME).build();
        assertEquals(0, giftCertificateRepository.findAllWithTagsByTagNames(new String[]{originalTag.getName()}).size());
    }

    @Test
    void successFindAllByName() {
        GiftCertificate originalCertificate = builder.build();
        assertEquals(1, giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getName()).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getName()).get(0);

        assertCertificates(originalCertificate, fetchedCertificate);
        assertTags(originalCertificate, fetchedCertificate);
    }

    @Test
    void emptyFindAllByName() {
        GiftCertificate originalCertificate = builder.withName(GiftCertificateBuilder.NOT_EXIST_CERTIFICATE_NAME).build();
        assertEquals(0, giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getName()).size());
    }

    @Test
    void successFindAllByDescription() {
        GiftCertificate originalCertificate = builder.build();
        assertEquals(1, giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getDescription()).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getDescription()).get(0);

        assertCertificates(originalCertificate, fetchedCertificate);
        assertTags(originalCertificate, fetchedCertificate);
    }

    @Test
    void emptyFindAllByDescription() {
        GiftCertificate originalCertificate = builder.withDescription(GiftCertificateBuilder.NOT_EXIST_CERTIFICATE_DESCRIPTION).build();
        assertEquals(0, giftCertificateRepository.findAllByNameOrDescription(originalCertificate.getDescription()).size());
    }

    @Test
    void successFindAllWithTagsByName() {
        GiftCertificate originalCertificate = builder.build();
        assertEquals(1, giftCertificateRepository.findAllWithTagsByNameOrDescription(originalCertificate.getName()).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findAllWithTagsByNameOrDescription(originalCertificate.getName()).get(0);

        assertCertificates(originalCertificate, fetchedCertificate);
        assertTags(originalCertificate, fetchedCertificate);
    }

    @Test
    void emptyFindAllAllWithTagsByName() {
        GiftCertificate originalCertificate = builder.withName(GiftCertificateBuilder.NOT_EXIST_CERTIFICATE_NAME).build();
        assertEquals(0, giftCertificateRepository.findAllWithTagsByNameOrDescription(originalCertificate.getName()).size());
    }

    @Test
    void successFindAllAllWithTagsByDescription() {
        GiftCertificate originalCertificate = builder.build();
        assertEquals(1, giftCertificateRepository.findAllWithTagsByNameOrDescription(originalCertificate.getDescription()).size());
        GiftCertificate fetchedCertificate = giftCertificateRepository.findAllWithTagsByNameOrDescription(originalCertificate.getDescription()).get(0);

        assertCertificates(originalCertificate, fetchedCertificate);
        assertTags(originalCertificate, fetchedCertificate);
    }

    @Test
    void emptyFindAllAllWithTagsByDescription() {
        GiftCertificate originalCertificate = builder.withDescription(GiftCertificateBuilder.NOT_EXIST_CERTIFICATE_DESCRIPTION).build();
        assertEquals(0, giftCertificateRepository.findAllWithTagsByNameOrDescription(originalCertificate.getDescription()).size());
    }

    @Test
    void successFindAllWithTagsOrderByDateASC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllWithTagsOrderByDate(GiftCertificateBuilder.ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllWithTagsOrderByDate(GiftCertificateBuilder.ORDER_BY_ASC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_ASC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllWithTagsOrderByDateDESC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllWithTagsOrderByDate(GiftCertificateBuilder.ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllWithTagsOrderByDate(GiftCertificateBuilder.ORDER_BY_DESC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_DESC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllOrderByDateASC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllOrderByDate(GiftCertificateBuilder.ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllOrderByDate(GiftCertificateBuilder.ORDER_BY_ASC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_ASC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllOrderByDateDESC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllOrderByDate(GiftCertificateBuilder.ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllOrderByDate(GiftCertificateBuilder.ORDER_BY_DESC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_DESC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllWithTagsOrderByNameASC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllWithTagsOrderByName(GiftCertificateBuilder.ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllWithTagsOrderByName(GiftCertificateBuilder.ORDER_BY_ASC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_NAME_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_NAME_ASC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllWithTagsOrderByNameDESC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllWithTagsOrderByName(GiftCertificateBuilder.ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllWithTagsOrderByName(GiftCertificateBuilder.ORDER_BY_DESC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_NAME_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_NAME_DESC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllOrderByNameASC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllOrderByName(GiftCertificateBuilder.ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllOrderByName(GiftCertificateBuilder.ORDER_BY_ASC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_NAME_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_NAME_ASC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllOrderByNameDESC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllOrderByName(GiftCertificateBuilder.ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllOrderByName(GiftCertificateBuilder.ORDER_BY_DESC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_NAME_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_NAME_DESC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllWithTagsOrderByDateASCAndByNameASC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllWithTagsOrderByDateAndByName(GiftCertificateBuilder.ORDER_BY_ASC, GiftCertificateBuilder.ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllWithTagsOrderByDateAndByName(GiftCertificateBuilder.ORDER_BY_ASC, GiftCertificateBuilder.ORDER_BY_ASC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_AND_NAME_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_AND_NAME_ASC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllWithTagsOrderByDateDESCAndByNameDESC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllWithTagsOrderByDateAndByName(GiftCertificateBuilder.ORDER_BY_DESC, GiftCertificateBuilder.ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllWithTagsOrderByDateAndByName(GiftCertificateBuilder.ORDER_BY_DESC, GiftCertificateBuilder.ORDER_BY_DESC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_AND_NAME_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_AND_NAME_DESC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllOrderByDateASCAndByNameASC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllOrderByDateAndByName(GiftCertificateBuilder.ORDER_BY_ASC, GiftCertificateBuilder.ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllOrderByDateAndByName(GiftCertificateBuilder.ORDER_BY_ASC, GiftCertificateBuilder.ORDER_BY_ASC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_AND_NAME_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_AND_NAME_ASC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllOrderByDateDESCAndByNameDESC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllOrderByDateAndByName(GiftCertificateBuilder.ORDER_BY_DESC, GiftCertificateBuilder.ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllOrderByDateAndByName(GiftCertificateBuilder.ORDER_BY_DESC, GiftCertificateBuilder.ORDER_BY_DESC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_AND_NAME_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_AND_NAME_DESC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllByNameOrDescriptionOrderByDateASC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllByNameOrDescriptionOrderByDate(GiftCertificateBuilder.SEARCH_TEXT, GiftCertificateBuilder.ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllByNameOrDescriptionOrderByDate(GiftCertificateBuilder.SEARCH_TEXT, GiftCertificateBuilder.ORDER_BY_ASC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_ASC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllByNameOrDescriptionOrderByDateDESC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllByNameOrDescriptionOrderByDate(GiftCertificateBuilder.SEARCH_TEXT, GiftCertificateBuilder.ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllByNameOrDescriptionOrderByDate(GiftCertificateBuilder.SEARCH_TEXT, GiftCertificateBuilder.ORDER_BY_DESC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_DESC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllByNameOrDescriptionOrderByDateASCAndByNameASC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllByNameOrDescriptionOrderByDateAndName(GiftCertificateBuilder.SEARCH_TEXT, GiftCertificateBuilder.ORDER_BY_ASC, GiftCertificateBuilder.ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllByNameOrDescriptionOrderByDateAndName(GiftCertificateBuilder.SEARCH_TEXT, GiftCertificateBuilder.ORDER_BY_ASC, GiftCertificateBuilder.ORDER_BY_ASC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_AND_NAME_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_AND_NAME_ASC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllByNameOrDescriptionOrderByDateDESCAndByNameDESC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllByNameOrDescriptionOrderByDateAndName(GiftCertificateBuilder.SEARCH_TEXT, GiftCertificateBuilder.ORDER_BY_DESC, GiftCertificateBuilder.ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllByNameOrDescriptionOrderByDateAndName(GiftCertificateBuilder.SEARCH_TEXT, GiftCertificateBuilder.ORDER_BY_DESC, GiftCertificateBuilder.ORDER_BY_DESC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_AND_NAME_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_DATE_AND_NAME_DESC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllByNameOrDescriptionOrderByNameASC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllByNameOrDescriptionOrderByName(GiftCertificateBuilder.SEARCH_TEXT, GiftCertificateBuilder.ORDER_BY_ASC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllByNameOrDescriptionOrderByName(GiftCertificateBuilder.SEARCH_TEXT, GiftCertificateBuilder.ORDER_BY_ASC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_NAME_ASC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_NAME_ASC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
    }

    @Test
    void successFindAllByNameOrDescriptionOrderByNameDESC() {
        assertEquals(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT, giftCertificateRepository.findAllByNameOrDescriptionOrderByName(GiftCertificateBuilder.SEARCH_TEXT, GiftCertificateBuilder.ORDER_BY_DESC).size());
        List<GiftCertificate> originalCertificate = giftCertificateRepository.findAllByNameOrDescriptionOrderByName(GiftCertificateBuilder.SEARCH_TEXT, GiftCertificateBuilder.ORDER_BY_DESC);

        assertEquals(GiftCertificateBuilder.ORDER_BY_NAME_DESC_FIRST_ID, originalCertificate.get(0).getId());
        assertEquals(GiftCertificateBuilder.ORDER_BY_NAME_DESC_LAST_ID, originalCertificate.get(GiftCertificateBuilder.ALL_CERTIFICATES_COUNT - 1).getId());
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
