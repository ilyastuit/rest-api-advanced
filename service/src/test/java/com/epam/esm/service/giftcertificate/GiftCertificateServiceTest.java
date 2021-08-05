package com.epam.esm.service.giftcertificate;

import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.giftcertificate.GiftCertificateDTO;
import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.tag.TagDTO;
import com.epam.esm.repository.giftcertificate.GiftCertificateRepository;
import com.epam.esm.service.exceptions.GiftCertificateDeleteRestriction;
import com.epam.esm.service.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.service.exceptions.TagNameAlreadyExistException;
import com.epam.esm.service.tag.TagDTOMapper;
import com.epam.esm.service.tag.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
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
    @Mock
    private TagDTOMapper tagDTOMapper;

    private GiftCertificateService service;

    @BeforeEach
    public void createMocks() {
        MockitoAnnotations.openMocks(this);
        service = new GiftCertificateService(repository, tagService, new GiftCertificateDTOMapperImpl(), tagDTOMapper);
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
        verify(repository, times(1)).update(anyInt(), any(MapSqlParameterSource.class));
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

        GiftCertificate entity = service.getOne(ID, true);

        assertEquals(giftCertificate.getId(), entity.getId());
        assertEquals(giftCertificate.getName(), entity.getName());
        assertEquals(giftCertificate.getDescription(), entity.getDescription());
        assertEquals(giftCertificate.getPrice(), entity.getPrice());
        assertEquals(giftCertificate.getDuration(), entity.getDuration());
        assertEquals(giftCertificate.getCreateDate(), entity.getCreateDate());
        assertEquals(giftCertificate.getLastUpdateDate(), entity.getLastUpdateDate());
        assertEquals(giftCertificate.getTags().size(), entity.getTags().size());

        verify(repository, times(1)).findByIdWithTags(ID);
    }

    @Test
    void testGetOneSuccess() throws GiftCertificateNotFoundException {
        int ID = 1;
        GiftCertificate giftCertificate = getCertificate();
        giftCertificate.setTags(getTags());
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findById(ID)
        ).thenReturn(list);

        GiftCertificate entity = service.getOne(ID, false);

        assertEquals(giftCertificate.getId(), entity.getId());
        assertEquals(giftCertificate.getName(), entity.getName());
        assertEquals(giftCertificate.getDescription(), entity.getDescription());
        assertEquals(giftCertificate.getPrice(), entity.getPrice());
        assertEquals(giftCertificate.getDuration(), entity.getDuration());
        assertEquals(giftCertificate.getCreateDate(), entity.getCreateDate());
        assertEquals(giftCertificate.getLastUpdateDate(), entity.getLastUpdateDate());

        verify(repository, times(1)).findById(ID);
    }

    @Test
    void testGetOneSuccessWithoutTags() throws GiftCertificateNotFoundException {
        int ID = 1;
        GiftCertificate giftCertificate = getCertificate();
        List<GiftCertificate> list = Collections.singletonList(giftCertificate);

        when(
                repository.findById(ID)
        ).thenReturn(list);

        GiftCertificate entity = service.getOne(ID, false);

        assertEquals(giftCertificate.getId(), entity.getId());
        assertEquals(giftCertificate.getName(), entity.getName());
        assertEquals(giftCertificate.getDescription(), entity.getDescription());
        assertEquals(giftCertificate.getPrice(), entity.getPrice());
        assertEquals(giftCertificate.getDuration(), entity.getDuration());
        assertEquals(giftCertificate.getCreateDate(), entity.getCreateDate());
        assertEquals(giftCertificate.getLastUpdateDate(), entity.getLastUpdateDate());

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
    void testGetAllWithTagsWithoutSort() {
        GiftCertificate giftCertificate = getCertificate();
        Page<GiftCertificate> page = new PageImpl<>(Collections.singletonList(giftCertificate));
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "name"));

        when(
                repository.findAllWithTags(pageable)
        ).thenReturn(page);

        Page<GiftCertificate> fetchedPage = service.getAll(Optional.of("true"), pageable);

        assertEquals(page.getTotalElements(), fetchedPage.getTotalElements());
        verify(repository, times(1)).findAllWithTags(pageable);
    }

    @Test
    void testGetAllWithoutTagsWithoutSort() {
        GiftCertificate giftCertificate = getCertificate();
        Page<GiftCertificate> page = new PageImpl<>(Collections.singletonList(giftCertificate));
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "name"));

        when(
                repository.findAll(pageable)
        ).thenReturn(page);

        Page<GiftCertificate> fetchedPage = service.getAll(Optional.of("false"), pageable);

        assertEquals(page.getTotalElements(), fetchedPage.getTotalElements());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void testGetAllByTagName() {
        GiftCertificate giftCertificate = getCertificate();
        Page<GiftCertificate> page = new PageImpl<>(Collections.singletonList(giftCertificate));
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "name"));

        when(
                repository.findAllWithTagsByTagNames(new String[] {"tag name"}, pageable)
        ).thenReturn(page);

        Page<GiftCertificate> fetchedPage = service.getAllByTagNames(new String[] {"tag name"}, pageable);

        assertEquals(page.getTotalElements(), fetchedPage.getTotalElements());
        verify(repository, times(1)).findAllWithTagsByTagNames(new String[] {"tag name"}, pageable);
    }

    @Test
    void testGetAllByNameOrDescriptionWithoutSort() {
        String name = "name";
        GiftCertificate giftCertificate = getCertificate();
        Page<GiftCertificate> page = new PageImpl<>(Collections.singletonList(giftCertificate));
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "name"));

        when(
                repository.findAllByNameOrDescription(name, pageable)
        ).thenReturn(page);

        Page<GiftCertificate> fetchedPage = service.getAllByNameOrDescription(name, pageable);

        assertEquals(page.getTotalElements(), fetchedPage.getTotalElements());
        verify(repository, times(1)).findAllByNameOrDescription(name, pageable);
    }

    @Test
    void testGetAllByNameOrDescriptionWithTags() {
        String name = "name";
        GiftCertificate giftCertificate = getCertificate();
        Page<GiftCertificate> page = new PageImpl<>(Collections.singletonList(giftCertificate));
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "name"));

        when(
                repository.findAllWithTagsByNameOrDescription(name, pageable)
        ).thenReturn(page);

        Page<GiftCertificate> fetchedPage = service.getAllByNameOrDescriptionWithTags(name, pageable);

        assertEquals(page.getTotalElements(), fetchedPage.getTotalElements());
        verify(repository, times(1)).findAllWithTagsByNameOrDescription(name, pageable);
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
        dto.setTags(getTagDTO());

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
