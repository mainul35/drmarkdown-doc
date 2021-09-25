package com.drmarkdown.doc.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
public abstract class GenericModel implements Serializable {

    @Id
    private String id = UUID.randomUUID().toString();

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now ();

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Version
    private Integer version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
