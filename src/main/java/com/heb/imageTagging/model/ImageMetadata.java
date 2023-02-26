package com.heb.imageTagging.model;

import lombok.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Set;
import java.net.URL;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class ImageMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private byte[] imageData;
    private String label;
    private boolean taggingEnabled;

    @ManyToMany
    @JoinTable(
            name = "image_tag",
            joinColumns = @JoinColumn(name = "image_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_name"))
    private Set<Tag> tags;



    public ImageMetadata(CreateImageTaggingRequest createImageRequest) throws IOException {
        this.setImageData(createImageRequest.getFile(), createImageRequest.getUrl());
        this.label = createImageRequest.getLabel() != null ? createImageRequest.getLabel() :
                "A picture uploaded by our lovely user";
        this.taggingEnabled = createImageRequest.getTaggingEnabled();
    }

    //Helper method to set our image data byte[]. If the imageFile wasn't provided,
    //we need to get the image from the url to a byte[].
    private void setImageData(MultipartFile imageFile, String url) throws IOException {
        if(imageFile == null) {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.connect();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(conn.getInputStream(), baos);
            this.imageData = baos.toByteArray();
        } else {this.imageData = imageFile.getBytes();}
    }
}
