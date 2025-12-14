package com.isocial.minisocialbe.controller;

import com.isocial.minisocialbe.dto.post.PostResponseDto;
import com.isocial.minisocialbe.service.post.TrendingService;
import com.isocial.minisocialbe.service.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trending")
@RequiredArgsConstructor
public class TrendingController {
    private final TrendingService trendingService;

    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getTrendingPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getId();
        Pageable pageable = PageRequest.of(page, size);

        Page<PostResponseDto> trending = trendingService.getTrendingPosts(userId, pageable);
        return ResponseEntity.ok(trending);
    }
}
