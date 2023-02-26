package com.heb.imageTagging.service;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import com.heb.imageTagging.model.Tag;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@NoArgsConstructor
@Service
public class GoogleCloudService {

    private static final Logger logger = LoggerFactory.getLogger(GoogleCloudService.class);

    public Set<Tag> getImageTags(byte[] data) throws Exception {
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            ByteString imgBytes = ByteString.copyFrom(data);

            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            requests.add(request);

            // Performs label detection on the image file
            BatchAnnotateImagesResponse batchResponse = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = batchResponse.getResponsesList();
            AnnotateImageResponse response = responses.get(0);
            if (response.hasError()) {
                logger.error("Error: %s%n" + response.getError().getMessage());
                throw new Exception(response.getError().getMessage());
            }

            Set<Tag> tags = new HashSet<>();
            for (EntityAnnotation annotation : response.getLabelAnnotationsList()) {
                Tag tag = new Tag(annotation.getDescription());
                tags.add(tag);
            }
            return tags;
        }
    }

    //Helper Method to print tags
    public void printImageTagging(byte[] data) throws Exception {
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            ByteString imgBytes = ByteString.copyFrom(data);

            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            requests.add(request);

            // Performs label detection on the image file
            BatchAnnotateImagesResponse batchResponse = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = batchResponse.getResponsesList();
            AnnotateImageResponse response = responses.get(0);
            if (response.hasError()) {
                logger.error("Error: %s%n" + response.getError().getMessage());
                throw new Exception(response.getError().getMessage());
            }
            for (EntityAnnotation annotation : response.getLabelAnnotationsList()) {
                annotation
                        .getAllFields()
                        .forEach((k, v) -> logger.info(k.toString() + " : " + v));
            }
            List<Tag> tags = new ArrayList<>();
            for (EntityAnnotation annotation : response.getLabelAnnotationsList()) {
                Tag tag = new Tag(annotation.getDescription());
                tags.add(tag);
            }
            for(Tag tag: tags) {
                logger.info(tag.getTagName());
            }
        }
    }

}
