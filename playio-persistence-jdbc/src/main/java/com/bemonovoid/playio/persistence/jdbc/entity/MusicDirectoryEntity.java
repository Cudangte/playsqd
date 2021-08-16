package com.bemonovoid.playio.persistence.jdbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table(MusicDirectoryEntity.TABLE_NAME)
@Getter
@Builder
@AllArgsConstructor(onConstructor=@__({@PersistenceConstructor}))
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MusicDirectoryEntity extends AuditableEntity {

    public static final String TABLE_NAME = "MUSIC_DIRECTORY";

    @Id
    private final long id;

    private final String type;

    private final String path;


}
