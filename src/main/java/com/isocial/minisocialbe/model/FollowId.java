package com.isocial.minisocialbe.model;

import jakarta.persistence.Column;
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
    @Column(name = "follower_id")
    private Long follower;

    @Column(name = "following_id")
    private Long following;
}
