package com.epam.esm.entity.user;

import com.epam.esm.entity.order.OrderModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "users")
public class UserModel extends RepresentationModel<UserModel> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;
    private String email;
    private String password;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createDate;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime lastUpdateDate;

    @JsonDeserialize(as= ArrayList.class, contentAs= OrderModel.class)
    private List<OrderModel> orders;

}
