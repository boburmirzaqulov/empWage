package com.organization.empwage.mapping;

import com.organization.empwage.dto.EmployeeDto;
import com.organization.empwage.dto.EmployeeDtoPOST;
import com.organization.empwage.exception.ValidationException;
import com.organization.empwage.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface EmployeeMapping {
    @Mapping(target = "birthDate", dateFormat = "yyyy-MM-dd")
    EmployeeDto toDto(Employee employee);

    EmployeeDto toDto(EmployeeDtoPOST employeeDtoPOST);

    @Mapping(target = "salary", source = "employeeDto.salary", qualifiedByName = "toSalaryToEntity")
    @Mapping(target = "birthDate", source = "employeeDto.birthDate", qualifiedByName = "toBirthDateToEntity")
    Employee toEntity(EmployeeDto employeeDto);

    @Named("toSalaryToEntity")
    default BigDecimal toSalaryToEntity(BigDecimal salary){
        if (salary == null) return BigDecimal.valueOf(0);
        return salary;
    }
    @Named("toBirthDateToEntity")
    default Date toBirthDateToEntity(String birthDate){
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(birthDate);
        } catch (ParseException e) {
            throw new ValidationException("birthDate - yyyy-MM-dd format");
        }
    }

    @Mapping(target = "salary", source = "employeeDtoPOST.salary", qualifiedByName = "toSalaryToEntity")
    @Mapping(target = "birthDate", source = "employeeDtoPOST.birthDate", qualifiedByName = "toBirthDateToEntity")
    Employee toEntity(EmployeeDtoPOST employeeDtoPOST);
}
