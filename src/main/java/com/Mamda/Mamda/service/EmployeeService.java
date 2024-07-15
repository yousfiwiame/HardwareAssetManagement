package com.Mamda.Mamda.service;

import com.Mamda.Mamda.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    EmployeeDto createEmployee(EmployeeDto employeeDto);

    EmployeeDto getEmployeeById(int employeeId);

    List<EmployeeDto> getAllEmployees();

    EmployeeDto updateEmployee(int employeeId, EmployeeDto updatedEmployee);
}
