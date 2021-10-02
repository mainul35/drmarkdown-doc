package com.drmarkdown.doc.repositories;

import com.drmarkdown.doc.dtos.DocDto;
import com.drmarkdown.doc.models.DocModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocRepository extends JpaRepository<DocModel, String> {
    List<DocModel> findAllByUserIdOrderByUpdatedAtDesc(String userId);

    @Modifying
    @Query("delete from doc d where d.userId = ?1 and d.id = ?2")
    int deleteByUserIdAndDocId(String userId, String docId);

    Page<DocModel> findAllByAvailable(boolean b, PageRequest updatedAt);
}
