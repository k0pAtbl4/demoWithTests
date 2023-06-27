package com.example.demowithtests.util.config.mapper;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.dto.EmployeeDto;
import com.example.demowithtests.dto.EmployeeReadDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    EmployeeReadDto toReadDto(Employee entity);
    EmployeeDto toDto(Employee entity);
    @InheritInverseConfiguration
    Employee toEntity(EmployeeDto dto);

    List<EmployeeReadDto> listToReadDto(List<Employee> entityList);
    @InheritInverseConfiguration
    List<Employee> listToDomain(List<EmployeeDto> entityList);
}
