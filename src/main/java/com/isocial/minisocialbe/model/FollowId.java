package com.isocial.minisocialbe.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


@EqualsAndHashCode
public class FollowId implements Serializable {

    private Long follower;
    private Long following;
}
