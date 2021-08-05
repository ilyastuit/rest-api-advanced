package com.epam.esm.service.tag;

import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.tag.TagDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagDTOMapper {

    TagDTO tagToDTO(Tag tag);
    Tag dtoToTag(TagDTO tagDTO);
}
