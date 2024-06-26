package ru.rkod.rkod.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rkod.rkod.dto.EmployeeDto;
import ru.rkod.rkod.exception.NotFoundException;
import ru.rkod.rkod.mapper.EmployeeMapper;
import ru.rkod.rkod.model.Employee;
import ru.rkod.rkod.repository.EmployeeRepository;
import ru.rkod.rkod.service.EmployeeService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EmployeeServiceImp implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImp(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    @Transactional
    public EmployeeDto create(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.toEmployee(employeeDto);
        return EmployeeMapper.toEmployeeDto(employeeRepository.save(employee));
    }

    @Override
    public EmployeeDto updateEmployee(EmployeeDto employeeDto, int id) {
        Employee employee = EmployeeMapper.toEmployee(employeeDto);
        Employee employeeOriginal = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("по вашему id не найден пользователь"));
        Optional.ofNullable(employee.getName()).ifPresent(employeeOriginal::setName);
        Optional.ofNullable(employee.getLastName()).ifPresent(employeeOriginal::setLastName);
        Optional.ofNullable(employee.getMiddleName()).ifPresent(employeeOriginal::setMiddleName);
        Optional.ofNullable(employee.getPosition()).ifPresent(employeeOriginal::setPosition);
        Optional.ofNullable(employee.getDepartment()).ifPresent(employeeOriginal::setDepartment);
        Optional.ofNullable(employee.getEmail()).ifPresent(employeeOriginal::setEmail);
        Optional.ofNullable(employee.getDateOfEmployment()).ifPresent(employeeOriginal::setDateOfEmployment);
        return EmployeeMapper.toEmployeeDto(employeeRepository.save(employeeOriginal));
    }

    @Override
    public void employeeDelete(int id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(()->new NotFoundException("по вашему id не найден пользователь"));
        employeeRepository.delete(employee);
    }
}