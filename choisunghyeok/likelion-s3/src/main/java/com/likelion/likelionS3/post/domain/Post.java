package com.likelion.likelionS3.post.domain;

import com.likelion.likelionS3.member.domain.Member;
import com.likelion.likelionS3.post.api.dto.request.PostUpdateRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    private String title;

    private String contents;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private Post(String title, String contents, String imageUrl, Member member) {
        this.title = title;
        this.contents = contents;
        this.imageUrl = imageUrl;
        this.member = member;
    }

    public void update(PostUpdateRequestDto postUpdateRequestDto, String imageUrl) {
        // dto에서 url을 꺼내오는게 아니니까 String imageUrl 추가
        this.title = postUpdateRequestDto.title();
        this.contents = postUpdateRequestDto.contents();
        if (imageUrl != null) { // 조건 2. 이미지를 보내지 않은 경우 기존 이미지 유지
            this.imageUrl = imageUrl;
        }
    }
}
