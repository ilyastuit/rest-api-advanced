package com.epam.esm.service.giftcertificate;

import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.giftcertificate.GiftCertificateDTO;
import com.epam.esm.entity.tag.Tag;
import com.epam.esm.service.exceptions.GiftCertificateDeleteRestriction;
import com.epam.esm.service.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.service.exceptions.TagNameAlreadyExistException;
import com.epam.esm.repository.giftcertificate.GiftCertificateRepository;
import com.epam.esm.service.tag.TagService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateService {

    private final GiftCertificateRepository certificateRepository;
    private final TagService tagService;
    private final GiftCertificateDTOMapper dtoMapper;

    public GiftCertificateService(GiftCertificateRepository certificateRepository, TagService tagService, GiftCertificateDTOMapper dtoMapper) {
        this.certificateRepository = certificateRepository;
        this.tagService = tagService;
        this.dtoMapper = dtoMapper;
    }

    public int save(GiftCertificateDTO giftCertificateDTO) throws TagNameAlreadyExistException {
        GiftCertificate giftCertificate = dtoMapper.giftCertificateDTOToGiftCertificate(giftCertificateDTO);

        giftCertificate.setCreateDate(LocalDateTime.now());

        MapSqlParameterSource params = prepareParams(giftCertificate);
        params.addValue("create_date", Timestamp.valueOf(giftCertificate.getCreateDate()));

        int id = this.certificateRepository.save(params);
        this.updateTags(id, giftCertificate.getTags());
        return id;
    }

    public int update(int id, GiftCertificateDTO giftCertificateDTO) throws TagNameAlreadyExistException {
        GiftCertificate giftCertificate = dtoMapper.giftCertificateDTOToGiftCertificate(giftCertificateDTO);

        this.updateTags(id, giftCertificate.getTags());
        return this.certificateRepository.update(id, prepareParams(giftCertificate));
    }

    public void assignTagToCertificate(int certificateId, int tagId) {
        if (!this.tagService.isTagAlreadyAssignedToGiftCertificate(certificateId, tagId)) {
            this.tagService.assignTagToGiftCertificate(certificateId, tagId);
        }
    }

    public void changeName(int certificateId, String newName) {
        this.certificateRepository.changeName(certificateId, newName);
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

    public GiftCertificateDTO getOne(int id) throws GiftCertificateNotFoundException {
        GiftCertificate giftCertificate = getFromList(this.certificateRepository.findById(id));

        if (giftCertificate == null) {
            throw new GiftCertificateNotFoundException(id);
        }

        return dtoMapper.giftCertificateToGiftCertificateDTO(giftCertificate);
    }

    public GiftCertificate getOne(int id, boolean withTags) throws GiftCertificateNotFoundException {
        GiftCertificate giftCertificate = null;
        if (withTags) {
            giftCertificate = getFromList(this.certificateRepository.findByIdWithTags(id));
        } else {
            giftCertificate = getFromList(this.certificateRepository.findById(id));
        }

        if (giftCertificate == null) {
            throw new GiftCertificateNotFoundException(id);
        }

        return giftCertificate;
    }

    public Page<GiftCertificate> getAll(Optional<String> tags, Pageable pageable) {
        if (tags.isPresent() && Boolean.parseBoolean(tags.get())) {
            return this.certificateRepository.findAllWithTags(pageable);
        } else {
            return this.certificateRepository.findAll(pageable);
        }
    }

    public Page<GiftCertificate> getAllWithTags(Pageable pageable) {
        return this.certificateRepository.findAllWithTags(pageable);
    }

    public Page<GiftCertificate> getAllByTagNames(String[] tagNames, Pageable pageable) {
        return this.certificateRepository.findAllWithTagsByTagNames(tagNames, pageable);
    }

    public Page<GiftCertificate> getAllByNameOrDescription(String text, Pageable pageable) {
        return certificateRepository.findAllByNameOrDescription(text, pageable);
    }

    public Page<GiftCertificate> getAllByNameOrDescriptionWithTags(String text, Pageable pageable) {
        return this.certificateRepository.findAllWithTagsByNameOrDescription(text, pageable);
    }

    private void updateTags(int certificateId, List<Tag> tags) throws TagNameAlreadyExistException {
        this.tagService.updateTags(certificateId, tags);
    }

    private GiftCertificate getFromList(List<GiftCertificate> certificateList) {
        return certificateList.stream().findAny().orElse(null);
    }

    private MapSqlParameterSource prepareParams(GiftCertificate giftCertificate) {
        giftCertificate.setLastUpdateDate(LocalDateTime.now());

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", giftCertificate.getName());
        params.addValue("description", giftCertificate.getDescription());
        params.addValue("price", giftCertificate.getPrice());
        params.addValue("duration", giftCertificate.getDuration());
        params.addValue("last_update_date", Timestamp.valueOf(giftCertificate.getLastUpdateDate()));

        if (giftCertificate.getTags() != null && !giftCertificate.getTags().isEmpty()) {
            params.addValue("tags", giftCertificate.getTags());
        }

        return params;
    }
}
