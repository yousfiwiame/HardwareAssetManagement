package com.Mamda.Mamda.repository;

import com.Mamda.Mamda.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
