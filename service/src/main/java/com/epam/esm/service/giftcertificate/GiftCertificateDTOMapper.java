package com.epam.esm.service.giftcertificate;

import com.epam.esm.config.mapper.IgnoreUnmappedMapperConfig;
import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.giftcertificate.GiftCertificateDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface GiftCertificateDTOMapper {

    GiftCertificate giftCertificateDTOToGiftCertificate(GiftCertificateDTO giftCertificateDTO);
}
