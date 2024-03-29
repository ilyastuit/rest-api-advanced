package com.epam.esm.service.tag;

import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.tag.TagDTO;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.service.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.service.exceptions.TagNameAlreadyExistException;
import com.epam.esm.service.exceptions.TagNotFoundException;
import com.epam.esm.service.giftcertificate.GiftCertificateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    private TagRepository repository;

    @Mock
    private GiftCertificateService giftCertificateService;

    private TagService service;

    @BeforeEach
    public void createMocks() {
        MockitoAnnotations.openMocks(this);
        service = new TagService(repository, giftCertificateService, new TagDTOMapperImpl());
    }

    @Test
    void testSaveSuccess() throws TagNameAlreadyExistException {
        int ID = 1;
        Tag tag = new Tag(ID);

        when(
                repository.save(
                        any(Tag.class)
                )
        ).thenReturn(tag);

        assertEquals(ID, service.save(getTagDTO()));
        verify(repository, times(1)).save(any(Tag.class));
    }

    @Test
    void testSaveThrowTagNameAlreadyExistException() {
        doThrow(DuplicateKeyException.class).when(repository).save(any(Tag.class));

        Assertions.assertThrows(TagNameAlreadyExistException.class, () -> {
            service.save(getTagDTO());
        });

        verify(repository, times(1)).save(any(Tag.class));
    }

    @Test
    void testGetByIdSuccess() throws TagNotFoundException {
        int ID = 1;
        Tag tag = getTag();
        List<Tag> list = Collections.singletonList(tag);

        when(
                repository.findById(
                        ID
                )
        ).thenReturn(list);

        Tag entity = service.getById(ID);

        assertEquals(getTagDTO().getId(), entity.getId());
        assertEquals(getTagDTO().getName(), entity.getName());
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void testGetByIdThrowTagNotFoundException() {
        int ID = 1;

        when(
                repository.findById(
                        ID
                )
        ).thenReturn(new ArrayList<>());


        Assertions.assertThrows(TagNotFoundException.class, () -> {
            service.getById(ID);
        });

        verify(repository, times(1)).findById(ID);
    }

    @Test
    void testGetAll() {
        Tag tag = getTag();
        Page<Tag> page = new PageImpl<>(Collections.singletonList(tag));
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "name"));

        when(
                repository.findAll(pageable)
        ).thenReturn(page);

        Page<Tag> fetchedPage = service.getAll(pageable);

        assertEquals(page.getTotalElements(), fetchedPage.getTotalElements());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void testGetOrderedTagsWithHighestPrice() {
        Tag tag = getTag();
        Page<Tag> page = new PageImpl<>(Collections.singletonList(tag));
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "name"));

        when(
                repository.findAllOrderedWithHighestPrice(pageable)
        ).thenReturn(page);

        Page<Tag> fetchedPage = service.getOrderedTagsWithHighestPrice(pageable);

        assertEquals(page.getTotalElements(), fetchedPage.getTotalElements());
        verify(repository, times(1)).findAllOrderedWithHighestPrice(pageable);
    }

    @Test
    void testGetMostOrdered() {
        Tag tag = getTag();
        Page<Tag> page = new PageImpl<>(Collections.singletonList(tag));
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "name"));

        when(
                repository.findAllMostOrdered(pageable)
        ).thenReturn(page);

        Page<Tag> fetchedPage = service.getMostOrdered(pageable);

        assertEquals(page.getTotalElements(), fetchedPage.getTotalElements());
        verify(repository, times(1)).findAllMostOrdered(pageable);
    }

    @Test
    void testDeleteById() throws TagNotFoundException {
        int ID = 1;
        List<Tag> list = new ArrayList<>();
        list.add(getTag());
        doNothing().when(repository).deleteById(ID);
        when(repository.findById(1)).thenReturn(list);

        service.deleteById(ID);
        verify(repository, times(1)).deleteById(ID);
    }

    @Test
    void testGetAllByCertificateId() {
        Tag tag = getTag();
        Page<Tag> page = new PageImpl<>(Collections.singletonList(tag));
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "name"));

        when(
                repository.findByGiftCertificateId(1, pageable)
        ).thenReturn(page);

        Page<Tag> fetchedPage = service.getAllByGiftCertificateId(1, pageable);

        assertEquals(page.getTotalElements(), fetchedPage.getTotalElements());
        verify(repository, times(1)).findByGiftCertificateId(1, pageable);
    }

    @Test
    void testisExistByName() {
        String name = "name";

        when(
                repository.findByName(
                        name
                )
        ).thenReturn(new ArrayList<>());

        boolean fetchedBoolean = service.isExistByName(name);

        assertFalse(fetchedBoolean);
        verify(repository, times(1)).findByName(name);
    }

    @Test
    void testGetByNameSuccess() throws TagNotFoundException {
        String name = "name";
        Tag tag = getTag();
        List<Tag> list = Collections.singletonList(tag);

        when(
                repository.findByName(name)
        ).thenReturn(list);


        TagDTO fetchedDTO = service.getByName(name);

        assertEquals(tag.getId(), fetchedDTO.getId());
        assertEquals(tag.getName(), fetchedDTO.getName());
        verify(repository, times(1)).findByName(name);
    }

    @Test
    void testGetByNameThrowTagNotFoundException() {
        String name = "name";

        when(
                repository.findByName(name)
        ).thenReturn(new ArrayList<>());

        Assertions.assertThrows(TagNotFoundException.class, () -> {
            service.getByName(name);
        });

        verify(repository, times(1)).findByName(name);
    }

    @Test
    void testIsTagAlreadyAssignedToGiftCertificate() {
        when(
                repository.findAssignedTagToCertificate(1, 1)
        ).thenReturn(new ArrayList<>());

        boolean fetchedBoolean = service.isTagAlreadyAssignedToGiftCertificate(1, 1);

        assertFalse(fetchedBoolean);
        verify(repository, times(1)).findAssignedTagToCertificate(1, 1);
    }

    @Test
    void testAssignTagToGiftCertificate() throws GiftCertificateNotFoundException, TagNotFoundException {
        List<Tag> list = new ArrayList<>();
        list.add(getTag());
        doNothing().when(repository).assignTagToGiftCertificate(1, 1);
        when(repository.findById(1)).thenReturn(list);

        service.assignTagToGiftCertificate(1, 1);

        verify(repository, times(1)).assignTagToGiftCertificate(1, 1);
    }

    private Tag getTag() {
        return new Tag(
                1,
                "name"
        );
    }

    private TagDTO getTagDTO() {
        TagDTO dto = new TagDTO();
        dto.setId(getTag().getId());
        dto.setName(getTag().getName());

        return dto;
    }
}
