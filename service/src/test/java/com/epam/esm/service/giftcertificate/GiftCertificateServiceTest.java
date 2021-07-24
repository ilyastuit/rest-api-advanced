package com.epam.esm.service.giftcertificate;

import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.giftcertificate.GiftCertificateDTO;
import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.tag.TagDTO;
import com.epam.esm.repository.giftcertificate.GiftCertificateRepository;
import com.epam.esm.service.exceptions.GiftCertificateDeleteRestriction;
import com.epam.esm.service.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.service.exceptions.TagNameAlreadyExistException;
import com.epam.esm.service.tag.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceTest {

    @Mock
    private GiftCertificateRepository repository;
    @Mock
    private TagService tagService;

    private GiftCertificateService service;

    @BeforeEach
    public void createMocks() {
        MockitoAnnotations.openMocks(this);
        service = new GiftCertificateService(repository, tagService, new GiftCertificateDTOMapperImpl());
    }

    @Test
    void testSave() throws TagNameAlreadyExistException {
        int ID = 1;

        when(
                repository.save(
                        any(MapSqlParameterSource.class)
                )
        ).thenReturn(ID);

        assertEquals(ID, service.save(getCertificateDTO()));
        verify(repository, times(1)).save(any(MapSqlParameterSource.class));
        verify(tagService, times(1)).updateTags(anyInt(), anyList());
    }

    @Test
    void testSaveThrowTagAlreadyExist() throws TagNameAlreadyExistException {
        doThrow(TagNameAlreadyExistException.class).when(tagService).updateTags(anyInt(), anyList());

        Assertions.assertThrows(TagNameAlreadyExistException.class, () -> {
            service.save(getCertificateDTO());
        });

        verify(tagService, times(1)).updateTags(anyInt(), anyList());
    }

    @Test
    void testUpdate() throws TagNameAlreadyExistException {
        int ID = 1;

        when(
                repository.update(
                        anyInt(),
                        any(MapSqlParameterSource.class)
                )
        ).thenReturn(ID);

        assertEquals(ID, service.update(ID, getCertificateDTO()));
        verify(tagService, times(1)).updateTags(anyInt(), anyList());
        verify(repository, times(1)).update(anyInt(), any(MapSqlParameterSource.class));
    }

    @Test
    void testUpdateThrowTagAlreadyExist() throws TagNameAlreadyExistException {
        int ID = 1;

        doThrow(TagNameAlreadyExistException.class).when(tagService).updateTags(anyInt(), anyList());

        Assertions.assertThrows(TagNameAlreadyExistException.class, () -> {
            service.update(ID, getCertificateDTO());
        });

        verify(tagService, times(1)).updateTags(anyInt(), anyList());
    }

    @Test
    void testExistById() {
        int ID = 1;

        when(
                repository.findById(ID)
        ).thenReturn(new ArrayList<>());

        assertFalse(service.isExistById(ID));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void testGetOneSuccessWithTags() throws GiftCertificateNotFoundException {
        int ID = 1;
        GiftCertificate giftCertificate = getCertificate();
        giftCertificate.setTags(getTags());
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findByIdWithTags(ID)
        ).thenReturn(list);

        GiftCertificateDTO dto = service.getOne(ID, true);

        assertEquals(giftCertificate.getId(), dto.getId());
        assertEquals(giftCertificate.getName(), dto.getName());
        assertEquals(giftCertificate.getDescription(), dto.getDescription());
        assertEquals(giftCertificate.getPrice(), dto.getPrice());
        assertEquals(giftCertificate.getDuration(), dto.getDuration());
        assertEquals(giftCertificate.getCreateDate(), dto.getCreateDate());
        assertEquals(giftCertificate.getLastUpdateDate(), dto.getLastUpdateDate());
        assertEquals(giftCertificate.getTags().size(), dto.getTags().size());

        verify(repository, times(1)).findByIdWithTags(ID);
    }

    @Test
    void testGetOneSuccessWithoutTags() throws GiftCertificateNotFoundException {
        int ID = 1;
        GiftCertificate giftCertificate = getCertificate();
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findById(ID)
        ).thenReturn(list);

        GiftCertificateDTO dto = service.getOne(ID, false);

        assertEquals(giftCertificate.getId(), dto.getId());
        assertEquals(giftCertificate.getName(), dto.getName());
        assertEquals(giftCertificate.getDescription(), dto.getDescription());
        assertEquals(giftCertificate.getPrice(), dto.getPrice());
        assertEquals(giftCertificate.getDuration(), dto.getDuration());
        assertEquals(giftCertificate.getCreateDate(), dto.getCreateDate());
        assertEquals(giftCertificate.getLastUpdateDate(), dto.getLastUpdateDate());

        verify(repository, times(1)).findById(ID);
    }

    @Test
    void testGetOneThrowNotFound() {
        int ID = 1;

        when(
                repository.findByIdWithTags(ID)
        ).thenReturn(new ArrayList<>());

        Assertions.assertThrows(GiftCertificateNotFoundException.class, () -> {
            service.getOne(ID, true);
        });

        verify(repository, times(1)).findByIdWithTags(ID);
    }

    @Test
    void testGetOneWithTagsSuccess() throws GiftCertificateNotFoundException {
        int ID = 1;
        GiftCertificate giftCertificate = getCertificate();
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findByIdWithTags(ID)
        ).thenReturn(list);

        GiftCertificateDTO dto = service.getOneWithTags(ID);

        assertEquals(giftCertificate.getId(), dto.getId());
        assertEquals(giftCertificate.getName(), dto.getName());
        assertEquals(giftCertificate.getDescription(), dto.getDescription());
        assertEquals(giftCertificate.getPrice(), dto.getPrice());
        assertEquals(giftCertificate.getDuration(), dto.getDuration());
        assertEquals(giftCertificate.getCreateDate(), dto.getCreateDate());
        assertEquals(giftCertificate.getLastUpdateDate(), dto.getLastUpdateDate());

        verify(repository, times(1)).findByIdWithTags(ID);
    }

    @Test
    void testGetOneWithTagsThrowNotFound() {
        int ID = 1;

        when(
                repository.findByIdWithTags(ID)
        ).thenReturn(new ArrayList<>());

        Assertions.assertThrows(GiftCertificateNotFoundException.class, () -> {
            service.getOneWithTags(ID);
        });

        verify(repository, times(1)).findByIdWithTags(ID);
    }

    @Test
    void testGetAllWithTagsWithoutSort() {
        GiftCertificate giftCertificate = getCertificate();
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findAllWithTags()
        ).thenReturn(list);

        List<GiftCertificateDTO> fetchedList = service.getAll(Optional.of("true"), Optional.of("wrong sorting"), Optional.of("wrong sorting"));

        assertEquals(list.size(), fetchedList.size());
        verify(repository, times(1)).findAllWithTags();
    }

    @Test
    void testGetAllWithoutTagsWithoutSort() {
        GiftCertificate giftCertificate = getCertificate();
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findAll()
        ).thenReturn(list);

        List<GiftCertificateDTO> fetchedList = service.getAll(Optional.of("false"), Optional.of("wrong sorting"), Optional.of("wrong sorting"));

        assertEquals(list.size(), fetchedList.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllWithTagsWithSortDate() {
        GiftCertificate giftCertificate = getCertificate();
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findAllWithTagsOrderByDate("ASC")
        ).thenReturn(list);

        List<GiftCertificateDTO> fetchedList = service.getAll(Optional.of("true"), Optional.of("ASC"), Optional.of("wrong sorting"));

        assertEquals(list.size(), fetchedList.size());
        verify(repository, times(1)).findAllWithTagsOrderByDate("ASC");
    }

    @Test
    void testGetAllWithoutTagsWithSortDate() {
        GiftCertificate giftCertificate = getCertificate();
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findAllOrderByDate("ASC")
        ).thenReturn(list);

        List<GiftCertificateDTO> fetchedList = service.getAll(Optional.of("false"), Optional.of("ASC"), Optional.of("wrong sorting"));

        assertEquals(list.size(), fetchedList.size());
        verify(repository, times(1)).findAllOrderByDate("ASC");
    }

    @Test
    void testGetAllWithTagsWithSortName() {
        GiftCertificate giftCertificate = getCertificate();
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findAllWithTagsOrderByName("ASC")
        ).thenReturn(list);

        List<GiftCertificateDTO> fetchedList = service.getAll(Optional.of("true"), Optional.of("wrong sorting"), Optional.of("ASC"));

        assertEquals(list.size(), fetchedList.size());
        verify(repository, times(1)).findAllWithTagsOrderByName("ASC");
    }

    @Test
    void testGetAllWithoutTagsWithSortName() {
        GiftCertificate giftCertificate = getCertificate();
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findAllOrderByName("ASC")
        ).thenReturn(list);

        List<GiftCertificateDTO> fetchedList = service.getAll(Optional.of("false"), Optional.of("wrong sorting"), Optional.of("ASC"));

        assertEquals(list.size(), fetchedList.size());
        verify(repository, times(1)).findAllOrderByName("ASC");
    }

    @Test
    void testGetAllWithTagsWithSortDateAndName() {
        GiftCertificate giftCertificate = getCertificate();
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findAllWithTagsOrderByDateAndByName("ASC", "ASC")
        ).thenReturn(list);

        List<GiftCertificateDTO> fetchedList = service.getAll(Optional.of("true"), Optional.of("ASC"), Optional.of("ASC"));

        assertEquals(list.size(), fetchedList.size());
        verify(repository, times(1)).findAllWithTagsOrderByDateAndByName("ASC", "ASC");
    }

    @Test
    void testGetAllWithoutTagsWithSortDateAndName() {
        GiftCertificate giftCertificate = getCertificate();
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findAllOrderByDateAndByName("ASC", "ASC")
        ).thenReturn(list);

        List<GiftCertificateDTO> fetchedList = service.getAll(Optional.of("false"), Optional.of("ASC"), Optional.of("ASC"));

        assertEquals(list.size(), fetchedList.size());
        verify(repository, times(1)).findAllOrderByDateAndByName("ASC", "ASC");
    }

    @Test
    void testGetAllByTagName() {
        GiftCertificate giftCertificate = getCertificate();
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findAllWithTagsByTagNames(new String[] {"tag name"})
        ).thenReturn(list);

        List<GiftCertificateDTO> fetchedList = service.getAllByTagNames(new String[] {"tag name"});

        assertEquals(list.size(), fetchedList.size());
        verify(repository, times(1)).findAllWithTagsByTagNames(new String[] {"tag name"});
    }

    @Test
    void testGetAllByNameOrDescriptionWithoutSort() {
        String name = "name";
        GiftCertificate giftCertificate = getCertificate();
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findAllByNameOrDescription(name)
        ).thenReturn(list);

        List<GiftCertificateDTO> fetchedList = service.getAllByNameOrDescription(name, Optional.of("wrong sort"), Optional.of("wrong sort"));

        assertEquals(list.size(), fetchedList.size());
        verify(repository, times(1)).findAllByNameOrDescription(name);
    }

    @Test
    void testGetAllByNameOrDescriptionWithDateSort() {
        String name = "name";
        GiftCertificate giftCertificate = getCertificate();
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findAllByNameOrDescriptionOrderByDate(name, "ASC")
        ).thenReturn(list);

        List<GiftCertificateDTO> fetchedList = service.getAllByNameOrDescription(name, Optional.of("ASC"), Optional.of("wrong sort"));

        assertEquals(list.size(), fetchedList.size());
        verify(repository, times(1)).findAllByNameOrDescriptionOrderByDate(name, "ASC");
    }

    @Test
    void testGetAllByNameOrDescriptionWithNameSort() {
        String name = "name";
        GiftCertificate giftCertificate = getCertificate();
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findAllByNameOrDescriptionOrderByName(name, "ASC")
        ).thenReturn(list);

        List<GiftCertificateDTO> fetchedList = service.getAllByNameOrDescription(name, Optional.of("wrong sort"), Optional.of("ASC"));

        assertEquals(list.size(), fetchedList.size());
        verify(repository, times(1)).findAllByNameOrDescriptionOrderByName(name, "ASC");
    }

    @Test
    void testGetAllByNameOrDescriptionWithDateAndNameSort() {
        String name = "name";
        GiftCertificate giftCertificate = getCertificate();
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findAllByNameOrDescriptionOrderByDateAndName(name, "ASC", "ASC")
        ).thenReturn(list);

        List<GiftCertificateDTO> fetchedList = service.getAllByNameOrDescription(name, Optional.of("ASC"), Optional.of("ASC"));

        assertEquals(list.size(), fetchedList.size());
        verify(repository, times(1)).findAllByNameOrDescriptionOrderByDateAndName(name, "ASC", "ASC");
    }

    @Test
    void testGetAllByNameOrDescriptionWithTags() {
        String name = "name";
        GiftCertificate giftCertificate = getCertificate();
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findAllWithTagsByNameOrDescription(name)
        ).thenReturn(list);

        List<GiftCertificateDTO> fetchedList = service.getAllByNameOrDescriptionWithTags(name);

        assertEquals(list.size(), fetchedList.size());
        verify(repository, times(1)).findAllWithTagsByNameOrDescription(name);
    }

    @Test
    void testAssignTagToCertificateNotAlreadyAssigned() {
        when(tagService.isTagAlreadyAssignedToGiftCertificate(anyInt(), anyInt())).thenReturn(false);
        doNothing().when(tagService).assignTagToGiftCertificate(anyInt(), anyInt());

        service.assignTagToCertificate(anyInt(), anyInt());

        verify(tagService, times(1)).isTagAlreadyAssignedToGiftCertificate(anyInt(), anyInt());
        verify(tagService, times(1)).assignTagToGiftCertificate(anyInt(), anyInt());
    }

    @Test
    void testAssignTagToCertificateAlreadyAssigned() {
        when(tagService.isTagAlreadyAssignedToGiftCertificate(anyInt(), anyInt())).thenReturn(true);

        service.assignTagToCertificate(anyInt(), anyInt());

        verify(tagService, times(1)).isTagAlreadyAssignedToGiftCertificate(anyInt(), anyInt());
    }

    @Test
    void testDeleteWithThrow() {
        int ID = 1;
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(ID);

        assertThrows(GiftCertificateDeleteRestriction.class, () -> {
            service.delete(ID);
        });

        verify(repository, times(1)).deleteById(ID);
    }

    @Test
    void testDelete() throws GiftCertificateDeleteRestriction {
        int ID = 1;
        doNothing().when(repository).deleteById(ID);

        service.delete(ID);

        verify(repository, times(1)).deleteById(ID);
    }

    @Test
    void testChangeName() {
        int ID = 1;
        String name = "New Name";
        doNothing().when(repository).changeName(ID, name);

        service.changeName(ID, name);

        verify(repository, times(1)).changeName(ID, name);
    }


    private GiftCertificate getCertificate() {
        return new GiftCertificate(
                1,
                "name",
                "description",
                new BigDecimal("100"),
                100,
                Timestamp.valueOf("2021-06-24 11:48:23").toLocalDateTime(),
                Timestamp.valueOf("2021-06-24 11:48:23").toLocalDateTime(),
                null
        );
    }

    private GiftCertificateDTO getCertificateDTO() {
        GiftCertificateDTO dto = new GiftCertificateDTO();
        dto.setId(1);
        dto.setName("name");
        dto.setDescription("description");
        dto.setPrice(new BigDecimal("100"));
        dto.setDuration(100);
        dto.setCreateDate(Timestamp.valueOf("2021-06-24 11:48:23").toLocalDateTime());
        dto.setLastUpdateDate(Timestamp.valueOf("2021-06-24 11:48:23").toLocalDateTime());

        return dto;
    }

    private List<Tag> getTags() {
        return Collections.singletonList(
                new Tag(1, "name1")
        );
    }

    private List<TagDTO> getTagDTO() {
        TagDTO dto1 = new TagDTO();
        dto1.setId(1);
        dto1.setName("name1");

        return Collections.singletonList(
                dto1
        );
    }
}
