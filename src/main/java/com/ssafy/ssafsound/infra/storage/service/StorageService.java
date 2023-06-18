package com.ssafy.ssafsound.infra.storage.service;

import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

public interface StorageService {

    String makeFileName(String originalFileName, String uploadDir, String memberId);

    String putObject(MultipartFile multipartFile, MetaData uploadDir, Long memberId);

    void deleteObject(String storageFileName);
}
