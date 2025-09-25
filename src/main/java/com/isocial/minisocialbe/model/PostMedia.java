package com.isocial.minisocialbe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_media")
@Getter
@Setter
@NoArgsConstructor

public class PostMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "media_url")
    private String mediaUrl;

    @Column(name = "media_type")
    private String mediaType;

    @Column(name = "public_id", nullable = false)
    private String publicId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public PostMedia(String mediaUrl, String mediaType, String publicId) {
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
        this.publicId = publicId;
    }

}
