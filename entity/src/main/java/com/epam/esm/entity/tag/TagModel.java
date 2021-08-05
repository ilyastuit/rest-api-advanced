package com.epam.esm.entity.tag;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;


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
}
