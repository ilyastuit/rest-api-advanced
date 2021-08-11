package com.epam.esm.service.tag;

import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.tag.TagDTO;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.service.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.service.exceptions.TagNameAlreadyExistException;
import com.epam.esm.service.exceptions.TagNotFoundException;
import com.epam.esm.service.giftcertificate.GiftCertificateService;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository repository;
    private final GiftCertificateService giftCertificateService;
    private final TagDTOMapper dtoMapper;

    public TagService(TagRepository repository, @Lazy GiftCertificateService giftCertificateService, TagDTOMapper dtoMapper) {
        this.repository = repository;
        this.giftCertificateService = giftCertificateService;
        this.dtoMapper = dtoMapper;
    }

    public int save(TagDTO tagDTO) throws TagNameAlreadyExistException {
        int id = 0;
        try {
            id = this.repository.save(dtoMapper.dtoToTag(tagDTO)).getId();
        } catch (DuplicateKeyException exception) {
            throw new TagNameAlreadyExistException(tagDTO.getName());
        }

        return id;
    }

    public Tag getById(int id) throws TagNotFoundException {
        Tag tag = getFromList(this.repository.findById(id));

        if (tag == null) {
            throw new TagNotFoundException(id);
        }
        return tag;
    }

    public Page<Tag> getAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    public void deleteById(int id) throws TagNotFoundException {
        this.getById(id);
        this.repository.deleteById(id);
    }

    public Page<Tag> getAllByGiftCertificateId(Integer certificateId, Pageable pageable) {
        return this.repository.findByGiftCertificateId(certificateId, pageable);
    }

    public boolean isExistByName(String name) {
        return getFromList(this.repository.findByName(name)) != null;
    }

    public TagDTO getByName(String name) throws TagNotFoundException {
        Tag tag = getFromList(this.repository.findByName(name));

        if (tag == null) {
            throw new TagNotFoundException(name);
        }

        return dtoMapper.tagToDTO(tag);
    }

    public boolean isTagAlreadyAssignedToGiftCertificate(int certificateId, int tagId) {
        return getFromList(this.repository.findAssignedTagToCertificate(certificateId, tagId)) != null;
    }

    public void assignTagToGiftCertificate(int certificateId, int tagId) throws GiftCertificateNotFoundException, TagNotFoundException {
        giftCertificateService.getOne(certificateId, false);
        this.getById(tagId);
        this.repository.assignTagToGiftCertificate(certificateId, tagId);
    }

    public Page<Tag> getOrderedTagsWithHighestPrice(Pageable pageable) {
        return repository.findAllOrderedWithHighestPrice(pageable);
    }

    public Page<Tag> getMostOrdered(Pageable pageable) {
        return repository.findAllMostOrdered(pageable);
    }

    private Tag getFromList(List<Tag> tags) {
        if (tags != null) {
            return tags.stream().findAny().orElse(null);
        }
        return null;
    }
}
