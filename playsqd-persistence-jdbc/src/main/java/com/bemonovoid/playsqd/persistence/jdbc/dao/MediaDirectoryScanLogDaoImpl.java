package com.bemonovoid.playsqd.persistence.jdbc.dao;

import com.bemonovoid.playsqd.core.dao.MediaDirectoryScanLogDao;
import com.bemonovoid.playsqd.core.model.MusicDirectoryScanLog;
import com.bemonovoid.playsqd.persistence.jdbc.entity.MediaDirectoryScanLogEntity;
import com.bemonovoid.playsqd.persistence.jdbc.repository.MusicDirectoryScanLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class MediaDirectoryScanLogDaoImpl implements MediaDirectoryScanLogDao {

    private final MusicDirectoryScanLogRepository repository;

    MediaDirectoryScanLogDaoImpl(MusicDirectoryScanLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(MusicDirectoryScanLog directoryScanLog) {
        MediaDirectoryScanLogEntity entity = MediaDirectoryScanLogEntity.builder()
                .deleteAllBeforeScan(directoryScanLog.isDeleteAllBeforeScan())
                .deleteMissing(directoryScanLog.isDeleteMissing())
                .scanDirectory(directoryScanLog.getScanDirectory())
                .scanDurationInMillis(directoryScanLog.getScanDuration().toMillis())
                .scanStatus(directoryScanLog.getScanStatus().name())
                .scanStatusDetails(directoryScanLog.getScanStatusDetails())
                .filesScanned(directoryScanLog.getFilesScanned())
                .scannedFilesMissing(directoryScanLog.getScannedFilesMissing())
                .build();
        repository.save(entity);
    }
}
