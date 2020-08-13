package com.cmt.extension.admin.model;

import com.cmt.extension.admin.model.dto.AppDTO;
import com.cmt.extension.admin.model.dto.AppView;
import com.cmt.extension.admin.model.dto.SpiDTO;
import com.cmt.extension.admin.model.entity.ExtensionEntity;
import com.cmt.extension.admin.model.entity.SpiEntity;
import com.cmt.extension.core.configcenter.model.Extension;
import com.cmt.extension.core.configcenter.model.Spi;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface Converter {
    Converter INSTANCE = Mappers.getMapper(Converter.class);

    List<AppDTO> map(List<AppView> list);

    List<SpiDTO> map2dto(List<SpiEntity> spis);

    SpiDTO map2dto(SpiEntity spi);

    List<SpiConfigDTO> mapConfigs(List<ExtensionEntity> extensions);

    @Mapping(target = "extensionId",source = "id")
    SpiConfigDTO mapConfig(ExtensionEntity extension);

    Spi mapSpi(SpiEntity spiEntity);

    @Mapping(target = "isDefault",expression = "java(com.cmt.extension.admin.model.type.YesOrNoEnum.getBooleanByCode(entity.getIsDefault()))")
    Extension mapExtension(ExtensionEntity entity);
}
