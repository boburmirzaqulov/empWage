package com.organization.empwage.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private Long id;
    private String surname;
    private String name;
    private String patronymic;
    private Date birthDate;
    private String phoneNumber;
    private Long departmentCode;
    private BigDecimal salary;
}
