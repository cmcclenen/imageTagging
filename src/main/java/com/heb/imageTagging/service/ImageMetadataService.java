package com.heb.imageTagging.service;

import com.heb.imageTagging.model.ImageMetadata;
import com.heb.imageTagging.model.ImageTaggingResponse;
import com.heb.imageTagging.model.Tag;
import com.heb.imageTagging.repository.ImageMetadataRepository;
import com.heb.imageTagging.repository.TagRepository;
import com.heb.imageTagging.util.ImageNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ImageMetadataService {

    @Autowired
    private final ImageMetadataRepository imageMetadataRepository;

    @Autowired
    private final TagRepository tagRepository;

    public void addImageMetadata(ImageMetadata imageMetadata) {
        imageMetadataRepository.save(imageMetadata);
    }
    public void addImageMetadataAndTags(ImageMetadata imageMetadata, Set<Tag> tags) throws Exception {
        for(Tag tag : tags){
            tagRepository.save(tag);
        }
        imageMetadata.setTags(tags);
        imageMetadataRepository.save(imageMetadata);
    }

    public ImageTaggingResponse getImageTaggingResponseByImageId(Integer imageId) {
        ImageMetadata imageMetadata = imageMetadataRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException(String.format("Cannot Find Image with ID %s", imageId)));
        if(imageMetadata.isTaggingEnabled()){
            Set<Tag> tags = tagRepository.findTagsByImageMetadataId(imageId);
            return new ImageTaggingResponse(imageMetadata,tags);
        }
        return new ImageTaggingResponse(imageMetadata);
    }
    public List<ImageTaggingResponse> getAllImageTaggingResponses() {
        List<ImageMetadata> imageMetadata = imageMetadataRepository.findAll();
        List<ImageTaggingResponse> allImageResponses = new ArrayList<>();
        for (ImageMetadata image: imageMetadata) {
            allImageResponses.add(getImageTaggingResponseByImageId(image.getId()));
        }
        return allImageResponses;
    }

    public Set<ImageTaggingResponse> getImagesTaggingResponsesMatchingTags(List<String> tagNames) {
        Set<ImageTaggingResponse> response = new HashSet<>();
        for (String tagName: tagNames ) {
            for (ImageMetadata imageMetadata : imageMetadataRepository.findImageMetadatasByTagsTagName(tagName.toLowerCase())) {
                response.add(new ImageTaggingResponse(imageMetadata));
            }
        }
        return response;
    }

}
