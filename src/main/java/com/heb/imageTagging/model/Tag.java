package com.heb.imageTagging.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
public class Tag {

    @Id
    private String tagName;

    @ManyToMany(mappedBy = "tags")
    private Set<ImageMetadata> imageMetadata;

    public Tag(String description) {
        this.tagName = description.toLowerCase();
    }
}
