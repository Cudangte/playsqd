package com.bemonovoid.playio.persistence.jdbc.dao;

import com.bemonovoid.playio.core.dao.MusicDirectoryScanLogDao;
import com.bemonovoid.playio.core.model.MusicDirectoryScanLog;
import com.bemonovoid.playio.persistence.jdbc.entity.MusicDirectoryScanLogEntity;
import com.bemonovoid.playio.persistence.jdbc.repository.MusicDirectoryScanLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class MusicDirectoryScanLogDaoImpl implements MusicDirectoryScanLogDao {

    private final MusicDirectoryScanLogRepository repository;

    MusicDirectoryScanLogDaoImpl(MusicDirectoryScanLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(MusicDirectoryScanLog directoryScanLog) {
        MusicDirectoryScanLogEntity entity = MusicDirectoryScanLogEntity.builder()
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
