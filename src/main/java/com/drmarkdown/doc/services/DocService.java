package com.drmarkdown.doc.services;

import com.drmarkdown.doc.dtos.DocDto;
import com.drmarkdown.doc.exceptions.UserNotAllowedException;

import java.util.List;

public interface DocService {
    void updateDoc(DocDto docDto, String userId) throws UserNotAllowedException;

    List<DocDto> fetchTopRecentDocs();

    DocDto fetchDoc(String docId, String userId);

    List<DocDto> fetchDocForUser(String userId, String callerUser);

    void createDocument(DocDto docDto);
}
