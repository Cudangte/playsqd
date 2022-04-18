package com.bemonovoid.playsqd.persistence.jdbc.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

public record AuditingEntity(@CreatedBy @Column(COL_CREATED_BY) String createdBy,
                             @CreatedDate @Column(COL_CREATED_DATE) LocalDateTime createdDate,
                             @LastModifiedBy @Column(COL_LAST_MODIFIED_BY) String lastModifiedBy,
                             @LastModifiedDate @Column(COL_LAST_MODIFIED_DATE) LocalDateTime lastModifiedDate) {

    static final String COL_CREATED_BY = "CREATED_BY";
    static final String COL_CREATED_DATE = "CREATED_DATE";
    static final String COL_LAST_MODIFIED_BY = "LAST_MODIFIED_BY";
    static final String COL_LAST_MODIFIED_DATE = "LAST_MODIFIED_DATE";

    public static AuditingEntity created() {
        return new AuditingEntity("system", LocalDateTime.now(), null, null);
    }


}
