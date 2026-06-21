package com.likelion.likelionS3.image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor    // final 필드 생성자 자동 생성 (Lombok)
public class S3Uploader {

    private final AmazonS3 amazonS3;  // S3Config에서 만든 빈 자동 주입

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;            // application.yml에서 버킷명 주입

    public String upload(MultipartFile file) throws IOException {

        // ① 중복 방지 파일명 생성
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // ② S3에 전달할 파일 정보 세팅
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // ③ 실제 업로드
        amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);

        // ④ 업로드된 파일 URL 반환
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    // delete 메소드 추가
//    public void delete(String imageUrl) {
//        String fileName = imageUrl.substring(imageUrl.indexOf(".com/") + 5 );
//        amazonS3.deleteObject(bucket, fileName); // 어느 버킷의 어느 키
//    }

    public void delete(String imageUrl) {
        try {
            // ① ".com/" 이후의 key(파일명) 문자열 추출
            String fileName = imageUrl.substring(imageUrl.indexOf(".com/") + 5);

            // ② [핵심] 한글 파일명이나 공백이 깨져서 삭제 안 되는 현상 방지 (URL 디코딩)
            String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);

            // ③ 디코딩된 정확한 key로 S3 오브젝트 삭제
            amazonS3.deleteObject(bucket, decodedFileName);

        } catch (Exception e) {
            // S3 삭제 실패 시 에러 로그를 남겨서 추적이 가능하도록 예외 처리
            System.err.println("S3 파일 삭제 중 오류 발생: " + e.getMessage());
        }
    }

}