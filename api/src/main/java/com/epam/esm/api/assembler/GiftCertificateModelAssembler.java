package com.epam.esm.api.assembler;

import com.epam.esm.api.v1.GiftCertificateController;
import com.epam.esm.api.v1.TagController;
import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.giftcertificate.GiftCertificateDTO;
import com.epam.esm.entity.giftcertificate.GiftCertificateModel;
import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.tag.TagModel;
import com.epam.esm.service.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.service.exceptions.TagNameAlreadyExistException;
import com.epam.esm.service.exceptions.TagNotFoundException;
import lombok.SneakyThrows;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateModelAssembler extends RepresentationModelAssemblerSupport<GiftCertificate, GiftCertificateModel> {

    public GiftCertificateModelAssembler() {
        super(GiftCertificateController.class, GiftCertificateModel.class);
    }

    @SneakyThrows
    @Override
    public GiftCertificateModel toModel(GiftCertificate entity) {

        GiftCertificateModel model = instantiateModel(entity);

        addHyperLinksToCertificate(model, entity);

        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setDuration(entity.getDuration());
        model.setPrice(entity.getPrice());
        model.setCreateDate(entity.getCreateDate());
        model.setLastUpdateDate(entity.getLastUpdateDate());
        model.setTags(toTagModel(entity.getTags()));

        return model;
    }

    @Override
    public CollectionModel<GiftCertificateModel> toCollectionModel(Iterable<? extends GiftCertificate> entities)
    {
        CollectionModel<GiftCertificateModel> certificateModels = super.toCollectionModel(entities);

        certificateModels.add(linkTo(methodOn(GiftCertificateController.class).all(Optional.empty(), Pageable.unpaged())).withSelfRel());

        return certificateModels;
    }

    private List<TagModel> toTagModel(List<Tag> tags) {
        if (tags.isEmpty())
            return Collections.emptyList();

        return tags.stream()
                .map(tag -> {
                    TagModel model =  TagModel.builder()
                                .id(tag.getId())
                                .name(tag.getName())
                                .build();
                    try {
                        addHyperMediaLinksToTags(model, tag);
                    } catch (TagNotFoundException e) {
                        e.printStackTrace();
                    }
                    return model;
                })
                .collect(Collectors.toList());
    }

    public void addHyperLinksToCertificate(GiftCertificateModel model, GiftCertificate entity) throws GiftCertificateNotFoundException, TagNameAlreadyExistException {
        model.add(linkTo(
                methodOn(GiftCertificateController.class)
                        .one(entity.getId(), Optional.empty()))
                .withSelfRel().expand()
        );

        model.add(linkTo(
                methodOn(GiftCertificateController.class)
                        .update(entity.getId(), new GiftCertificateDTO()))
                .withRel("update").expand()
        );

        model.add(linkTo(
                methodOn(GiftCertificateController.class)
                        .changeName(entity.getId(), new GiftCertificateDTO()))
                .withRel("change-name").expand()
        );
    }

    public void addHyperMediaLinksToTags(TagModel model, Tag tag) throws TagNotFoundException {
        model.add(linkTo(
                methodOn(TagController.class)
                        .one(tag.getId()))
                .withSelfRel());
    }
}
