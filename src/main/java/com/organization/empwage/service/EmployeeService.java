package com.organization.empwage.service;

import com.organization.empwage.dto.EmployeeDto;
import com.organization.empwage.dto.EmployeeDtoPOST;
import com.organization.empwage.dto.ResponseDto;
import com.organization.empwage.exception.ValidationException;
import com.organization.empwage.helper.AppCode;
import com.organization.empwage.helper.AppMessages;
import com.organization.empwage.mapping.EmployeeMapping;
import com.organization.empwage.model.Employee;
import com.organization.empwage.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeMapping employeeMapping;
    private final EmployeeRepository employeeRepository;

    public ResponseDto<EmployeeDto> getById(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isEmpty()) {
            throw new ValidationException(AppMessages.NOT_FOUND);
        }
        Employee employee = optionalEmployee.get();
        EmployeeDto employeeDto = employeeMapping.toDto(employee);
        return new ResponseDto<>(true, 0, employeeDto);
    }

    public ResponseDto<EmployeeDto> save(EmployeeDtoPOST employeeDtoPOST) {
        Employee employeeS = employeeMapping.toEntity(employeeDtoPOST);
        employeeRepository.save(employeeS);
        return new ResponseDto<>(true, 0, employeeMapping.toDto(employeeS));
    }

    public ResponseDto<List<EmployeeDto>> getAll() {
        List<EmployeeDto> result = employeeRepository.getAll().stream()
                .map(employeeMapping::toDto)
                .collect(Collectors.toList());
        return new ResponseDto<>(true, AppCode.OK, result);
    }

    public ResponseDto<EmployeeDto> update(EmployeeDto employeeDto) {
        Employee employee = employeeMapping.toEntity(employeeDto);
        employeeRepository.update(employee);
        return new ResponseDto<>(true, AppCode.OK, employeeMapping.toDto(employee));
    }

    public ResponseDto<EmployeeDto> deleteById(Long id) {
        Employee employee = employeeRepository.deleteById(id);
        return new ResponseDto<>(true, AppCode.OK, employeeMapping.toDto(employee));
    }
}
