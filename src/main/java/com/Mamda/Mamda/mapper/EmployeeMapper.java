package com.Mamda.Mamda.mapper;

import com.Mamda.Mamda.dto.EmployeeDto;
import com.Mamda.Mamda.entity.Employee;

public class EmployeeMapper {
    public static EmployeeDto mapToEmployeeDto(Employee employee) {
        return new EmployeeDto(employee.getId(),
                               employee.getLastName(),
                               employee.getFirstName(),
                               employee.getEmail()
        );
    }

    public static Employee mapToEmployee(EmployeeDto employeeDto) {
        return new Employee(employeeDto.getId(),
                            employeeDto.getLastName(),
                            employeeDto.getFirstName(),
                            employeeDto.getEmail()
        );
    }
}
