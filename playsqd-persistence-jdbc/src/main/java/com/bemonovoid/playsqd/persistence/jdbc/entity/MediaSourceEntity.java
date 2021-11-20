package com.bemonovoid.playsqd.persistence.jdbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(MediaSourceEntity.TABLE_NAME)
@Getter
@Builder
@AllArgsConstructor(onConstructor=@__({@PersistenceConstructor}))
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MediaSourceEntity extends AuditableEntity {

    public static final String TABLE_NAME = "MEDIA_SOURCE";

    static final String COL_AUTO_SCAN_ON_RESTART = "AUTO_SCAN_ON_RESTART";

    @Id
    private final long id;

    private final String name;

    private final String path;

    @Column(COL_AUTO_SCAN_ON_RESTART)
    private final boolean autoScanOnRestart;

}
