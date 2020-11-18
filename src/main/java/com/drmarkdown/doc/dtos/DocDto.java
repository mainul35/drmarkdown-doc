package com.drmarkdown.doc.dtos;

import com.drmarkdown.doc.models.DocModel;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Data
public class DocDto extends BaseDto<DocModel> {
    private String id;
    private Date createdAt;
    private Date updatedAt;
    private String content;
    private String userId;
    private String title;
    private Boolean available;

    @Override
    public void mapEntityToDto(DocModel docModel) {
        this.setId(docModel.getId());
        this.setCreatedAt(docModel.getCreatedAt());
        this.setUpdatedAt(docModel.getUpdatedAt());
        this.setContent(docModel.getContent());
        this.setUserId(docModel.getUserId());
        this.setTitle(docModel.getTitle());
        this.setAvailable(docModel.getAvailable());
    }
}
