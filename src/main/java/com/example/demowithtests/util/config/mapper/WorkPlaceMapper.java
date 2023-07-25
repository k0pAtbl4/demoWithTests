package com.example.demowithtests.util.config.mapper;

import com.example.demowithtests.domain.WorkPlace;
import com.example.demowithtests.dto.WorkPlaceDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WorkPlaceMapper {

    WorkPlaceMapper INSTANCE = Mappers.getMapper(WorkPlaceMapper.class);

    WorkPlaceDto toDto(WorkPlace workPlace);

    WorkPlace fromDto(WorkPlaceDto workPlaceDto);
}
