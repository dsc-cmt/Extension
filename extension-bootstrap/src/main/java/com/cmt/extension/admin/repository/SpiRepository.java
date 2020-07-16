package com.cmt.extension.admin.repository;

import com.cmt.extension.admin.model.entity.SpiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpiRepository extends JpaRepository<SpiEntity, Long> {
    Optional<SpiEntity> findByAppIdAndSpiInterface(Long appId, String spiInterface);

    Optional<List<SpiEntity>> findAllByAppId(Long appId);
}
