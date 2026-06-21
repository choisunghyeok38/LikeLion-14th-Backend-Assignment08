package com.likelion.likelionS3.post.api;

import com.likelion.likelionS3.common.response.code.SuccessCode;
import com.likelion.likelionS3.common.template.ApiResTemplate;
import com.likelion.likelionS3.post.api.dto.request.PostSaveRequestDto;
import com.likelion.likelionS3.post.api.dto.request.PostUpdateRequestDto;
import com.likelion.likelionS3.post.api.dto.response.PostInfoResponseDto;
import com.likelion.likelionS3.post.application.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Tag(name = "POST API", description = "게시글 관리하는 api ")
public class PostController {

    private final PostService postService;
    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "게시물 저장", description = "게시물 저장. 이미지는 선택사항입니다.")
    public ApiResTemplate<Void> postSave(
            @RequestPart("data") PostSaveRequestDto postSaveRequestDto,
            @RequestPart(value = "image", required = false)MultipartFile image) {
        postService.postSave(postSaveRequestDto, image);
        return ApiResTemplate.successWithNoContent(SuccessCode.POST_SAVE_SUCCESS);
    }

    // 사용자 id를 기준으로 해당 사용자가 작성한 게시글 목록 조회
    @GetMapping("/{memberId}")
    @Operation(summary = "게시물 memberId로 조회", description = "게시물 memberId로 조회")
    public ApiResTemplate<Page<PostInfoResponseDto>> myPostFindAll(@PathVariable("memberId") Long memberId, @ParameterObject @PageableDefault(size = 10, sort = "postId", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<PostInfoResponseDto> posts = postService.postFindMember(memberId, pageable);
        return ApiResTemplate.successResponse(SuccessCode.GET_SUCCESS, posts);
    }

    // 게시물 id를 기준으로 사용자가 작성한 게시물 수정
    @PatchMapping(value = "/{postId}", consumes = "multipart/form-data") // consumes = "multipart/form-data" 추가
    @Operation(summary = "게시물 Id로 수정", description = "게시물 제목, 내용, 이미지 수정")
    public ApiResTemplate<Void> postUpdate(@PathVariable("postId") Long postId,
                                           @RequestPart ("data") PostUpdateRequestDto postUpdateRequestDto,
                                           @RequestPart(value = "image", required = false) MultipartFile image) { // 추가
        postService.postUpdate(postId, postUpdateRequestDto, image); // image 추가
        return ApiResTemplate.successWithNoContent(SuccessCode.POST_UPDATE_SUCCESS);
    }

    // 게시물 id를 기준으로 사용자가 작성한 게시물 삭제
    @DeleteMapping("/{postId}")
    @Operation(summary = "게시물 삭제", description = "게시물 Id로 삭제")
    public ApiResTemplate<Void> postDelete(@PathVariable("postId") Long postId) {
        postService.postDelete(postId);
        return ApiResTemplate.successWithNoContent(SuccessCode.POST_DELETE_SUCCESS);
    }
}
