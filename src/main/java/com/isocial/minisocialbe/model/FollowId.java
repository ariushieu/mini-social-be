package com.isocial.minisocialbe.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor

@EqualsAndHashCode
public class FollowId implements Serializable {

    private Integer follower;
    private Integer following;
}
