package com.likelion.likelionS3.post.api.dto.response;

import com.likelion.likelionS3.post.domain.Post;
import lombok.Builder;

@Builder
public record PostInfoResponseDto(
        String title,
        String contents,
        String writer,
        String imageUrl
) {
    public static PostInfoResponseDto from(Post post) {
        return PostInfoResponseDto.builder()
                .title(post.getTitle())
                .contents(post.getContents())
                .writer(post.getMember().getName())
                .imageUrl(post.getImageUrl())
                .build();

    }
}
