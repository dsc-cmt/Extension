package com.cmt.extension.admin.repository;

import com.cmt.extension.admin.model.entity.AppEntity;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepository extends JpaRepository<AppEntity, Long> {
    Optional<AppEntity> findByAppName(String appName);
}
