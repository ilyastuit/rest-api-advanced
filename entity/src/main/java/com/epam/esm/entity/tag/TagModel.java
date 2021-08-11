package com.epam.esm.entity.tag;

import com.epam.esm.entity.giftcertificate.GiftCertificateModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "tags")
public class TagModel extends RepresentationModel<TagModel> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<GiftCertificateModel> certificates;
}
