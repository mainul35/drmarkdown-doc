package com.drmarkdown.doc.dtos;

import com.drmarkdown.doc.models.DocModel;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class DocDto extends BaseDto<DocModel> {
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
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
