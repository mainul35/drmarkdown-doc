package com.drmarkdown.doc.models;

import com.drmarkdown.doc.dtos.DocDto;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Data
@Entity(name = "doc")
public class DocModel extends GenericModel {
    @Column(columnDefinition = "TEXT")
    private String content;
    @Column
    private String userId;
    @Column
    private String title;
    @Column
    private Boolean available;

    public DocModel() {
        super();
    }

    public DocModel(DocDto docDto) {
        this.setContent(docDto.getContent());
        this.setUserId(docDto.getUserId());
        this.setTitle(docDto.getTitle());
        this.setAvailable(docDto.getAvailable());
        this.setUpdatedAt(docDto.getUpdatedAt());
    }

    public DocModel mapDto(DocDto docDto) {
        this.setContent(docDto.getContent());
        this.setUserId(docDto.getUserId());
        this.setTitle(docDto.getTitle());
        this.setAvailable(docDto.getAvailable());
        this.setUpdatedAt(docDto.getUpdatedAt());
        return this;
    }

}
