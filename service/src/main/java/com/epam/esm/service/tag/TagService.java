package com.epam.esm.service.tag;

import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.tag.TagDTO;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.service.exceptions.TagNameAlreadyExistException;
import com.epam.esm.service.exceptions.TagNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository repository;
    private final TagDTOMapper dtoMapper;

    public TagService(TagRepository repository, TagDTOMapper dtoMapper) {
        this.repository = repository;
        this.dtoMapper = dtoMapper;
    }

    public int save(TagDTO tagDTO) throws TagNameAlreadyExistException {
        int id = 0;
        try {
            id = this.repository.save(dtoMapper.dtoToTag(tagDTO));
        } catch (DuplicateKeyException exception) {
            throw new TagNameAlreadyExistException(tagDTO.getName());
        }

        return id;
    }

    public TagDTO getById(int id) throws TagNotFoundException {
        Tag tag = getFromList(this.repository.findById(id));

        if (tag == null) {
            throw new TagNotFoundException(id);
        }
        return dtoMapper.tagToDTO(tag);
    }

    public List<TagDTO> getAll() {
        return dtoMapper.map(this.repository.findAll());
    }

    public void deleteById(int id) {
        this.repository.deleteById(id);
    }

    public List<TagDTO> getAllByGiftCertificateId(Integer certificateId) {
        return dtoMapper.map(this.repository.findByGiftCertificateId(certificateId));
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

    public void assignTagToGiftCertificate(int certificateId, int tagId) {
        this.repository.assignTagToGiftCertificate(certificateId, tagId);
    }

    public List<TagDTO> getOrderedTagsWithHighestPrice() {
        return dtoMapper.map(repository.findAllOrderedWithHighestPrice());
    }

    public void updateTags(int certificateId, List<Tag> tags) throws TagNameAlreadyExistException {
        List<TagDTO> tagDTOList = dtoMapper.map(tags);
        try {
            for (TagDTO tagDTO: tagDTOList) {
                int tagId;
                if (!this.isExistByName(tagDTO.getName())) {
                    tagId = this.save(tagDTO);
                } else {
                    tagId = this.getByName(tagDTO.getName()).getId();
                }
                if (!this.isTagAlreadyAssignedToGiftCertificate(certificateId, tagId)) {
                    this.assignTagToGiftCertificate(certificateId, tagId);
                }
            }
        } catch (TagNotFoundException ignored) {
        }
    }

    private Tag getFromList(List<Tag> tags) {
        return tags.stream().findAny().orElse(null);
    }

    public List<TagDTO> getMostOrdered() {
        return dtoMapper.map(repository.findAllMostOrdered());
    }
}
