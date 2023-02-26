package com.heb.imageTagging.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class ImageTaggingResponse {

    private int id;
    private byte[] imageData;
    private String label;
    private boolean taggingEnabled;
    private Set<String> tags;


    //Generate Response when Tagging not enabled
    public ImageTaggingResponse(ImageMetadata imageMetadata, Set<Tag> tags) {
        this.id = imageMetadata.getId();
        this.imageData = imageMetadata.getImageData();
        this.label = imageMetadata.getLabel();
        this.taggingEnabled = imageMetadata.isTaggingEnabled();
        this.setTags(tags);
    }

    public ImageTaggingResponse(ImageMetadata imageMetadata) {
        this.id = imageMetadata.getId();
        this.imageData = imageMetadata.getImageData();
        this.label = imageMetadata.getLabel();
        this.taggingEnabled = imageMetadata.isTaggingEnabled();
        setTags(imageMetadata.getTags());
    }
    private void setTags(Set<Tag> tags) {
        this.tags = new HashSet<>();
        for (Tag tag: tags) {
            this.tags.add(tag.getTagName());
        }
    }
}

