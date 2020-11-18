package com.drmarkdown.doc.repositories;

import com.drmarkdown.doc.dtos.DocDto;
import com.drmarkdown.doc.models.DocModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocRepository extends JpaRepository<DocModel, String> {
    List<DocModel> findAllByUserIdOrderByUpdatedAtDesc(String userId);
}
