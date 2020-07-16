package com.cmt.extension.admin.model;

import com.cmt.extension.admin.model.dto.AppDTO;
import com.cmt.extension.admin.model.dto.AppView;
import com.cmt.extension.admin.model.entity.SpiEntity;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.DoubleStream;

@Mapper
public interface Converter {
    Converter INSTANCE = Mappers.getMapper(Converter.class);

    List<AppDTO> map(List<AppView> list);

    SpiConfigDTO map(SpiEntity spi);
}
