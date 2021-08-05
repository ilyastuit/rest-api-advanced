package com.epam.esm.api.assembler;

import com.epam.esm.api.v1.GiftCertificateController;
import com.epam.esm.api.v1.TagController;
import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.tag.TagModel;
import lombok.SneakyThrows;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagModelAssembler extends RepresentationModelAssemblerSupport<Tag, TagModel> {

    public TagModelAssembler() {
        super(GiftCertificateController.class, TagModel.class);
    }

    @SneakyThrows
    @Override
    public TagModel toModel(Tag entity) {
        TagModel model = instantiateModel(entity);

        model.setId(entity.getId());
        model.setName(entity.getName());

        model.add(
                linkTo(
                        methodOn(TagController.class)
                        .one(entity.getId())
                ).withSelfRel()
        );

        return model;
    }

    @Override
    public CollectionModel<TagModel> toCollectionModel(Iterable<? extends Tag> entities) {
        CollectionModel<TagModel> tagModels = super.toCollectionModel(entities);

        tagModels.add(linkTo(methodOn(TagController.class).all(Pageable.unpaged())).withSelfRel().expand());

        return tagModels;
    }
}
