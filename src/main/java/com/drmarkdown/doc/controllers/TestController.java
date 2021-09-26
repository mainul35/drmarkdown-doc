package com.drmarkdown.doc.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping(produces = "application/json")
    @PreAuthorize ("hasAnyRole('ANONYMOUS')")
    public ResponseEntity<?> testHttp2() {
        return ResponseEntity.ok ("{\"status\":\"success\"}");
    }
}
