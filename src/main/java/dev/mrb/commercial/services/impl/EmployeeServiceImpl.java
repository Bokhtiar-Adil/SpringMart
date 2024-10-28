package dev.mrb.commercial.services.impl;

import dev.mrb.commercial.mappers.Mapper;
import dev.mrb.commercial.model.dtos.EmployeeDto;
import dev.mrb.commercial.model.dtos.OfficeDto;
import dev.mrb.commercial.model.entities.AccountEntity;
import dev.mrb.commercial.model.entities.EmployeeEntity;
import dev.mrb.commercial.model.entities.OfficeEntity;
import dev.mrb.commercial.repositories.AccountRepository;
import dev.mrb.commercial.repositories.EmployeeRepository;
import dev.mrb.commercial.repositories.OfficeRepository;
import dev.mrb.commercial.security.AccountRegistrationSecurityConfig;
import dev.mrb.commercial.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;
    private final OfficeRepository officeRepository;
    private final Mapper<EmployeeEntity, EmployeeDto> employeeMapper;
    private final Mapper<OfficeEntity, OfficeDto> officeMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public boolean exists(Long employeeId) {
        return employeeRepository.existsById(employeeId);
    }

    @Override
    public boolean checkOrderEditPermission(Long employeeId) {
        // validity check logic
        return true;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<EmployeeEntity> employees;

        employees = employeeRepository.findAll();

        return getListOfDtosFromEntities(employees);
    }

    @Override
    public String addNewEmployee(EmployeeDto employeeDto) {
        EmployeeEntity employeeEntity;
        EmployeeEntity savedEntity;

        employeeEntity = employeeMapper.mapFrom(employeeDto);
        savedEntity = employeeRepository.save(employeeEntity);
        if (savedEntity == null)
            return "Error occurred while saving new employee data";

        return "ok";
    }

    @Override
    public EmployeeDto getEmployee(Long id) {
        Optional<EmployeeEntity> employee;

        employee = employeeRepository.findById(id);
        if (employee.isEmpty())
            return null;
        
        EmployeeDto employeeDto = employeeMapper.mapTo(employee.get());

        return employeeDto;
    }

    @Override
    public List<EmployeeDto> getEmployeesByName(String name) {
        List<EmployeeEntity> employees;

        employees = employeeRepository.findEmployeesByName(name);

        return getListOfDtosFromEntities(employees);
    }

    @Override
    public String updateEmployeeDataByHimself(EmployeeDto employeeDto) {
        Optional<EmployeeEntity> employee;

        employee = employeeRepository.findById(employeeDto.getEmployeeId());
        if (employee.isEmpty()) return "Invalid id";

        if (employeeDto.getContactNo()!=null) employee.get().setContactNo(employeeDto.getContactNo());
        if (employeeDto.getFirstName()!=null) employee.get().setFirstName(employeeDto.getFirstName());
        if (employeeDto.getLastName()!=null) employee.get().setLastName(employeeDto.getLastName());

        EmployeeEntity savedEntity = employeeRepository.save(employee.get());

        return "ok";
    }

    @Override
    public String updateEmployeeDataBySuperior(Long superiorId, Long id, EmployeeDto employeeDto) {
        Optional<EmployeeEntity> employee;

        if (!employeeRepository.existsById(superiorId))
            return "Invalid superior id";

        employee = employeeRepository.findById(id);
        if (employee.isEmpty()) return null;

        if (employeeDto.getFirstName()!=null)
            employee.get().setFirstName(employeeDto.getFirstName());
        if (employeeDto.getLastName()!=null)
            employee.get().setLastName(employeeDto.getLastName());
        if (employeeDto.getContactNo()!=null)
            employee.get().setContactNo(employeeDto.getContactNo());
        if (employeeDto.getEmail()!=null)
            employee.get().setEmail(employeeDto.getEmail());
        if (employeeDto.getDesignation() != null)
            employee.get().setDesignation(employeeDto.getDesignation());
        if (employeeDto.getOfficeId() != null)
            employee.get().setOffice(officeRepository.find());
        if (employeeDto.getSpecialInfo() != null)
            employee.get().setSpecialInfo(employeeDto.getSpecialInfo());

        EmployeeEntity savedEntity = employeeRepository.save(employee.get());
        if (savedEntity == null)
            return "Error saving updated info";

        return "ok";

    }

    @Override
    public String deleteEmployee(Long id) {
        Optional<EmployeeEntity> toBeDeletedEmployee;

        toBeDeletedEmployee = employeeRepository.findById(id);
        if (toBeDeletedEmployee == null)
            return "Invalid id";

        accountRepository.deleteByEmployee(toBeDeletedEmployee.get());
        employeeRepository.deleteById(id);

        return "ok";
    }

    private List<EmployeeDto> getListOfDtosFromEntities(List<EmployeeEntity> employees)
    {
        List<EmployeeDto> employeeDtos = new ArrayList<>();

        for (EmployeeEntity employee : employees) {
            EmployeeDto employeeDto = employeeMapper.mapTo(employee);
            employeeDtos.add(employeeDto);
        }

        return employeeDtos;
    }
}
