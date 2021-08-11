package com.epam.esm.service.tag;

import com.epam.esm.config.mapper.IgnoreUnmappedMapperConfig;
import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.tag.TagDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface TagDTOMapper {

    TagDTO tagToDTO(Tag tag);

    Tag dtoToTag(TagDTO tagDTO);
}
