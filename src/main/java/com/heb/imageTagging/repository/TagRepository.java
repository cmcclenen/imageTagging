package com.heb.imageTagging.repository;

import com.heb.imageTagging.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag,Integer> , JpaSpecificationExecutor<Tag> {

    Set<Tag> findTagsByImageMetadataId(int imageId);

}
