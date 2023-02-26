package com.heb.imageTagging.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateImageTaggingRequest {

    private MultipartFile file;
    private String url;
    private String label;
    private Boolean taggingEnabled;
}
