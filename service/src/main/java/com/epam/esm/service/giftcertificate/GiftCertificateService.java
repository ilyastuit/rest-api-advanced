package com.epam.esm.service.giftcertificate;

import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.giftcertificate.GiftCertificateDTO;
import com.epam.esm.service.exceptions.GiftCertificateDeleteRestriction;
import com.epam.esm.service.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.service.exceptions.TagNameAlreadyExistException;
import com.epam.esm.repository.giftcertificate.GiftCertificateRepository;
import com.epam.esm.service.exceptions.TagNotFoundException;
import com.epam.esm.service.tag.TagDTOMapper;
import com.epam.esm.service.tag.TagService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateService {

    private final GiftCertificateRepository certificateRepository;
    private final TagService tagService;
    private final GiftCertificateDTOMapper dtoMapper;
    private final TagDTOMapper tagDTOMapper;

    public GiftCertificateService(
            GiftCertificateRepository certificateRepository,
            TagService tagService,
            GiftCertificateDTOMapper dtoMapper,
            TagDTOMapper tagDTOMapper
    ) {
        this.certificateRepository = certificateRepository;
        this.tagService = tagService;
        this.dtoMapper = dtoMapper;
        this.tagDTOMapper = tagDTOMapper;
    }

    public int save(GiftCertificateDTO giftCertificateDTO) throws TagNameAlreadyExistException {
        GiftCertificate giftCertificate = dtoMapper.giftCertificateDTOToGiftCertificate(giftCertificateDTO);

        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());

        return this.certificateRepository.save(giftCertificate).getId();
    }

    public int update(int id, GiftCertificateDTO giftCertificateDTO) throws TagNameAlreadyExistException {
        GiftCertificate giftCertificate = dtoMapper.giftCertificateDTOToGiftCertificate(giftCertificateDTO);
        giftCertificate.setId(id);
        return this.certificateRepository.save(giftCertificate).getId();
    }

    public void assignTagToCertificate(int certificateId, int tagId) throws GiftCertificateNotFoundException, TagNotFoundException {
        if (!this.tagService.isTagAlreadyAssignedToGiftCertificate(certificateId, tagId)) {
            this.tagService.assignTagToGiftCertificate(certificateId, tagId);
        }
    }

    public void changeName(int certificateId, String newName) {
        this.certificateRepository.changeNameById(certificateId, newName);
    }

    public void delete(int id) throws GiftCertificateDeleteRestriction {
        try {
            this.certificateRepository.deleteById(id);
        } catch (DataIntegrityViolationException exception) {
            throw new GiftCertificateDeleteRestriction(id);
        }
    }

    public boolean isExistById(int id) {
        return getFromList(this.certificateRepository.findById(id)) != null;
    }

    public GiftCertificate getOne(int id, boolean withTags) throws GiftCertificateNotFoundException {
        GiftCertificate giftCertificate = getFromList(this.certificateRepository.findById(id));

        if (giftCertificate == null) {
            throw new GiftCertificateNotFoundException(id);
        }

        return giftCertificate;
    }

    public Page<GiftCertificate> getAll(Optional<String> tags, Pageable pageable) {
        return this.certificateRepository.findAll(pageable);
    }

    public Page<GiftCertificate> getAllByTagNames(String[] tagNames, Pageable pageable) {
        return this.certificateRepository.findAllByTagNames(tagNames, pageable);
    }

    public Page<GiftCertificate> getAllByNameOrDescription(String text, Pageable pageable) {
        return certificateRepository.findAllByNameOrDescription(text, pageable);
    }

    public Page<GiftCertificate> getAllByNameOrDescriptionWithTags(String text, Pageable pageable) {
        return this.certificateRepository.findAllByNameOrDescription(text, pageable);
    }

    private GiftCertificate getFromList(List<GiftCertificate> certificateList) {
        if (certificateList != null) {
            return certificateList.stream().findAny().orElse(null);
        }
        return null;
    }
}
