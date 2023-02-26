package com.heb.imageTagging.controller;

import com.heb.imageTagging.model.CreateImageTaggingRequest;
import com.heb.imageTagging.model.ImageMetadata;
import com.heb.imageTagging.model.ImageTaggingResponse;
import com.heb.imageTagging.model.Tag;
import com.heb.imageTagging.service.GoogleCloudService;
import com.heb.imageTagging.service.ImageMetadataService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/images")
@RequiredArgsConstructor

public class ImageMetadataController {

    @Autowired
    private final ImageMetadataService imageMetadataService;
    @Autowired
    private final GoogleCloudService googleCloudService;
    private static final Logger logger = LoggerFactory.getLogger(ImageMetadataController.class);

    @PostMapping
    public ResponseEntity addImageMetaData(CreateImageTaggingRequest createImageTaggingRequest) throws IOException {
        //Validate request
        try {
            validateCreateImageRequest(createImageTaggingRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        ImageMetadata imageMetadata = new ImageMetadata(createImageTaggingRequest);

        //If tagging was requested, conduct tagging and save tags along with image.
        //Otherwise, just save image.
        if(imageMetadata.isTaggingEnabled()) {
            try {
                //Try to generate tags
                Set<Tag> tags = googleCloudService.getImageTags(imageMetadata.getImageData());
                //Save Image and its Tags
                imageMetadataService.addImageMetadataAndTags(imageMetadata, tags);
                return ResponseEntity.ok().body(new ImageTaggingResponse(imageMetadata, tags));
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        imageMetadataService.addImageMetadata(imageMetadata);
        return ResponseEntity.ok().body(new ImageTaggingResponse(imageMetadata));
    }

    @GetMapping
    public ResponseEntity getImageData(@RequestParam(value = "objects", required = false) List<String> tags){
        if (tags != null) {
            Set<ImageTaggingResponse> response = imageMetadataService.getImagesTaggingResponsesMatchingTags(tags);
            if (response.isEmpty()) {
                return ResponseEntity.ok().body("We weren't able to find any images with those tags! Please try again later");
            }
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.ok().body(imageMetadataService.getAllImageTaggingResponses());
    }

    @GetMapping("/{imageId}")
    public ResponseEntity getImageMetadataById(@PathVariable(value="imageId") Integer imageId) {
        return ResponseEntity.ok().body(imageMetadataService.getImageTaggingResponseByImageId(imageId));
    }

    public void validateCreateImageRequest(CreateImageTaggingRequest createImageTaggingRequest) throws Exception {
        logger.info("Entering validateCreateImageRequest");

        if (createImageTaggingRequest.getFile() == null && createImageTaggingRequest.getUrl() == null) {
            logger.info("No image file or source URL found in request");
            throw new Exception("No file found. Please include an image or url and try again.");
        }
        else if (createImageTaggingRequest.getFile() != null && createImageTaggingRequest.getUrl() != null) {
            logger.info("User supplied both a file and a url");
            throw new Exception("You supplied a file and a url. Please include only one and try again.");
        } else if (createImageTaggingRequest.getTaggingEnabled() == null) {
            logger.info("No tagging selection found");
            throw new Exception("No tagging selection found. Please indicate whether you would like us to perform tagging or not and try again.");
        }
        // Try to get the bytes now. If it throws an error, we can catch it now and immediately
        // notify customer that something is wrong with the file they uploaded
        if (createImageTaggingRequest.getUrl() == null) {
            createImageTaggingRequest.getFile().getBytes();
        }
        //Otherwise, lets try to convert the url to a URL object to see if something is wrong with it
        // If it fails, we can immediately tell user that something was wrong with the URL they provided
        else {
            URL url = new URL(createImageTaggingRequest.getUrl());
        }

    }
}

