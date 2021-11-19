package com.bemonovoid.playsqd.persistence.jdbc.entity;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

@ToString
@EqualsAndHashCode
public abstract class AuditableEntity {

    public static final String COL_CREATED_BY = "CREATED_BY";
    public static final String COL_CREATED_DATE = "CREATED_DATE";
    public static final String COL_LAST_MODIFIED_BY = "LAST_MODIFIED_BY";
    public static final String COL_LAST_MODIFIED_DATE = "LAST_MODIFIED_DATE";

    @CreatedBy
    @Column(COL_CREATED_BY)
    private String createdBy;

    @CreatedDate
    @Column(COL_CREATED_DATE)
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(COL_LAST_MODIFIED_BY)
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(COL_LAST_MODIFIED_DATE)
    private LocalDateTime lastModifiedDate;
}
