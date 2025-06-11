package store.agong.authentication.global.util;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class BaseTimeEntity {

    protected LocalDateTime createdAt;
    protected String createdBy;

    protected LocalDateTime updatedAt;
    protected String updatedBy;

    protected LocalDateTime deletedAt;
    protected String deletedBy;

    protected boolean isDeleted;

    public void markCreated(String createdBy) {
        this.createdAt = LocalDateTime.now();
        this.createdBy = createdBy;
    }

    public void markUpdated(String updatedBy) {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = updatedBy;
    }

    public void markDeleted(String deletedBy) {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }
}
