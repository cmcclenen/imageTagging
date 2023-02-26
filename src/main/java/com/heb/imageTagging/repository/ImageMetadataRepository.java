package com.heb.imageTagging.repository;

import com.heb.imageTagging.model.ImageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ImageMetadataRepository extends JpaRepository<ImageMetadata,Integer>, JpaSpecificationExecutor<ImageMetadata> {

    @Query(value = "SELECT new com.heb.imageTagging.Model.ImageMetadata FROM image_metadata WHERE id = ?1", nativeQuery = true)
    ImageMetadata findImageMetaDataById(int id);

    List<ImageMetadata> findImageMetadatasByTagsTagName(String tagName);

}
