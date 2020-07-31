package com.cmt.extension.admin.repository;

import com.cmt.extension.admin.model.dto.AppView;
import com.cmt.extension.admin.model.entity.AppEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppRepository extends JpaRepository<AppEntity, Long> {
    @Query(value = "select a.app_name as appName, u.user_name as creator, a.date_create as createTime" +
            " from extension_app a join extension_user u on a.creator_id= u.id", nativeQuery = true)
    List<AppView> findAllApps();

    Optional<AppEntity> findByAppName(String appName);
}
