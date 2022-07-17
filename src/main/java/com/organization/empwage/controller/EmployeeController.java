package com.organization.empwage.controller;

import com.organization.empwage.dto.EmployeeDto;
import com.organization.empwage.dto.EmployeeDtoPOST;
import com.organization.empwage.dto.ResponseDto;
import com.organization.empwage.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    public ResponseDto<EmployeeDto> getById(@PathVariable Long id){
        return employeeService.getById(id);
    }

    @GetMapping("/get-all")
    public ResponseDto<List<EmployeeDto>> getAll(){
        return employeeService.getAll();
    }

    @PostMapping
    public ResponseDto<EmployeeDto> save(@RequestBody @Valid EmployeeDtoPOST employeeDtoPOST){
        return employeeService.save(employeeDtoPOST);
    }

    @PutMapping
    public ResponseDto<EmployeeDto> update(@RequestBody @Valid EmployeeDto employeeDto){
        return employeeService.update(employeeDto);
    }

    @DeleteMapping("/{id}")
    public ResponseDto<EmployeeDto> deleteById(@PathVariable Long id){
        return employeeService.deleteById(id);
    }
}
