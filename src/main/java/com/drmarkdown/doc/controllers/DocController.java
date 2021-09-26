package com.drmarkdown.doc.controllers;

import com.drmarkdown.doc.dtos.DocDto;
import com.drmarkdown.doc.exceptions.MissingAuthorizationException;
import com.drmarkdown.doc.exceptions.UserNotAllowedException;
import com.drmarkdown.doc.services.DocService;
import com.drmarkdown.doc.services.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private DocService docService;

    @Autowired
    private TokenService tokenService;

    // - create own document
    @PostMapping("/create")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public DocDto createDocument(@RequestBody DocDto docDto) {
        docService.createDocument(docDto);
        return docDto;
    }

    // - fetch own documents
    @GetMapping("/{userId}/all")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN', 'ANONYMOUS')")
    public ResponseEntity<List<DocDto>> fetchUserDocs(@PathVariable String userId, HttpServletRequest request) throws MissingAuthorizationException {
        String jwtToken = getJwtTokenFromHeader(request);
        String callerUser = tokenService.getUserId(jwtToken);
        return ResponseEntity.ok (docService.fetchDocForUser(userId, callerUser));
    }

    // - fetch public documents
    @GetMapping("/{docId}")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN', 'ANONYMOUS')")
    public DocDto fetchDocument(@PathVariable String docId, HttpServletRequest request) throws MissingAuthorizationException {
        String jwtToken = getJwtTokenFromHeader(request);
        String callerUser = tokenService.getUserId(jwtToken);
        return docService.fetchDoc(docId, callerUser);
    }

    // - fetch 10 recent docs
    @GetMapping("/recent")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN', 'ANONYMOUS')")
    public List<DocDto> fetchURecentDocs() {
        return docService.fetchTopRecentDocs();
    }

    // - modify own doc
    @PutMapping("/update")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public DocDto updateDoc(@RequestBody DocDto docDto, HttpServletRequest request) throws UserNotAllowedException, MissingAuthorizationException {

        String jwtToken = getJwtTokenFromHeader(request);
        String userId = tokenService.getUserId(jwtToken);
        docService.updateDoc(docDto, userId);
        return docDto;
    }

    private String getJwtTokenFromHeader(HttpServletRequest request) throws MissingAuthorizationException {
        String tokenHeader = request.getHeader(AUTHORIZATION);
        if (tokenHeader == null) throw new MissingAuthorizationException("Authorization header does not contain a bearer token");
        String jwtToken = StringUtils.removeStart(tokenHeader, "Bearer ").trim();
        return jwtToken;
    }
    // - delete a doc of owner

    @DeleteMapping(value = "/{docId}/delete", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> deleteDoc(@PathVariable String docId, HttpServletRequest request) throws MissingAuthorizationException {

        String jwtToken = getJwtTokenFromHeader(request);
        String userId = tokenService.getUserId(jwtToken);
        docService.deleteDoc(userId, docId);
        return ResponseEntity.ok ("{\"message\": \"Doc Deleted successfully\"}");
    }
}
