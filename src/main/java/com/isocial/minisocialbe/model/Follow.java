package com.isocial.minisocialbe.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "follows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Follow {
    @EmbeddedId
    private FollowId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("follower")
    @JoinColumn(name = "follower_id", insertable = false, updatable = false)
    private User followerUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("following")
    @JoinColumn(name = "following_id", insertable = false, updatable = false)
    private User followingUser;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
