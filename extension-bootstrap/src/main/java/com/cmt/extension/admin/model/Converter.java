package com.cmt.extension.admin.model;

import com.cmt.extension.admin.model.dto.AppDTO;
import com.cmt.extension.admin.model.dto.AppView;
import com.cmt.extension.admin.model.dto.SpiDTO;
import com.cmt.extension.admin.model.entity.Extension;
import com.cmt.extension.admin.model.entity.Spi;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface Converter {
    Converter INSTANCE = Mappers.getMapper(Converter.class);

    List<AppDTO> map(List<AppView> list);

    List<SpiDTO> map2dto(List<Spi> spis);

    SpiDTO map2dto(Spi spi);

    List<SpiConfigDTO> mapConfigs(List<Extension> extensions);

    @Mapping(target = "extensionId",source = "id")
    SpiConfigDTO mapConfig(Extension extension);
}
