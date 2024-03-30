package dev.mrb.commercial.services.impl;

import dev.mrb.commercial.mappers.Mapper;
import dev.mrb.commercial.model.dtos.EmployeeDto;
import dev.mrb.commercial.model.dtos.OfficeDto;
import dev.mrb.commercial.model.entities.AccountEntity;
import dev.mrb.commercial.model.entities.EmployeeEntity;
import dev.mrb.commercial.model.entities.OfficeEntity;
import dev.mrb.commercial.repositories.AccountRepository;
import dev.mrb.commercial.repositories.EmployeeRepository;
import dev.mrb.commercial.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;
    private final Mapper<EmployeeEntity, EmployeeDto> mapper;
    private final Mapper<OfficeEntity, OfficeDto> officeMapper;
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
        List<EmployeeEntity> employees = employeeRepository.findAll();
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        for (EmployeeEntity employee : employees) {
            EmployeeDto employeeDto = mapToDtoFromEntityWithSelectedInfo(employee.getEmployeeId(),
                    employee.getFirstName(), employee.getLastName(), employee.getEmail(),
                    employee.getContactNo(), officeMapper.mapTo(employee.getOffice()),
                    employee.getDesignation(),null, employee.getSpecialInfo(), null); 
            employeeDto.setRoles(accountRepository.findRolesByEmployee(employee));
            employeeDtos.add(employeeDto);            
        }
        return employeeDtos;
    }

    @Override
    public EmployeeDto addNewEmployee(EmployeeDto employeeDto) {
        EmployeeEntity employeeEntity = mapToEntityFromDto(employeeDto);
        EmployeeEntity savedEntity = employeeRepository.save(employeeEntity);
        AccountEntity newAccount = new AccountEntity();
        newAccount.setEmployee(true);
        newAccount.setEmployeeDetails(employeeEntity);
        newAccount.setUsername(employeeDto.getFirstName() + " " + employeeDto.getLastName());
        newAccount.setPassword(employeeDto.getPassword()); // need to encode
        newAccount.setEmail(employeeDto.getEmail());
        newAccount.setEnabled(false); // need to enable
        newAccount.setRoles(employeeDto.getRoles());
        accountRepository.save(newAccount);
        EmployeeDto savedEmployeeDto = mapToDtoFromEntityWithSelectedInfo(savedEntity.getEmployeeId(),
                savedEntity.getFirstName(), savedEntity.getLastName(), savedEntity.getEmail(), savedEntity.getContactNo(),
                officeMapper.mapTo(savedEntity.getOffice()), savedEntity.getDesignation(), null,
                savedEntity.getSpecialInfo(), null);
        savedEmployeeDto.setRoles(accountRepository.findRolesByEmployee(savedEntity));
        return savedEmployeeDto;
    }

    @Override
    public EmployeeDto getEmployee(Long id) {
        EmployeeEntity employee = employeeRepository.findById(id).get();
        EmployeeDto employeeDto = mapToDtoFromEntityWithSelectedInfo(employee.getEmployeeId(),
                employee.getFirstName(), employee.getLastName(), employee.getEmail(),
                employee.getContactNo(), officeMapper.mapTo(employee.getOffice()),
                employee.getDesignation(),null, employee.getSpecialInfo(), null);
        employeeDto.setRoles(accountRepository.findRolesByEmployee(employee));
        return employeeDto;
    }

    @Override
    public List<EmployeeDto> getEmployeeByName(String name) {
        List<EmployeeEntity> employees = employeeRepository.findEmployeeByName(name);
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        for (EmployeeEntity employee : employees) {
            EmployeeDto employeeDto = mapToDtoFromEntityWithSelectedInfo(employee.getEmployeeId(),
                    employee.getFirstName(), employee.getLastName(), employee.getEmail(),
                    employee.getContactNo(), officeMapper.mapTo(employee.getOffice()),
                    employee.getDesignation(),null, employee.getSpecialInfo(), null);
            employeeDto.setRoles(accountRepository.findRolesByEmployee(employee));
            employeeDtos.add(employeeDto);
        }
        return employeeDtos;
    }

    @Override
    public EmployeeDto updateEmployeeDataByHimself(Long id, EmployeeDto employeeDto) {
        Optional<EmployeeEntity> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) return null;
        else {
            if (employeeDto.getContactNo()!=null) employee.get().setContactNo(employeeDto.getContactNo());
            if (employeeDto.getFirstName()!=null) employee.get().setFirstName(employeeDto.getFirstName());
            if (employeeDto.getLastName()!=null) employee.get().setLastName(employeeDto.getLastName());
        }
        EmployeeEntity savedEntity = employeeRepository.save(employee.get());
        EmployeeDto savedEmployeeDto = mapToDtoFromEntityWithSelectedInfo(savedEntity.getEmployeeId(),
                savedEntity.getFirstName(), savedEntity.getLastName(), savedEntity.getEmail(), savedEntity.getContactNo(),
                officeMapper.mapTo(savedEntity.getOffice()), savedEntity.getDesignation(), null,
                savedEntity.getSpecialInfo(), null);
        savedEmployeeDto.setRoles(accountRepository.findRolesByEmployee(savedEntity));
        return savedEmployeeDto;
    }

    @Override
    public EmployeeDto updateEmployeeDataBySuperior(Long superiorId, Long id, EmployeeDto employeeDto) {
        if (employeeRepository.existsById(superiorId)) {
            Optional<EmployeeEntity> employee = employeeRepository.findById(id);
            if (employee.isEmpty()) return null;
            else {
                if (employeeDto.getDesignation()!=null) employee.get().setDesignation(employeeDto.getDesignation());
                if (employeeDto.getOffice()!=null) employee.get().setOffice(officeMapper.mapFrom(employeeDto.getOffice()));
                if (employeeDto.getSpecialInfo()!=null) employee.get().setSpecialInfo(employeeDto.getSpecialInfo());
            }
            EmployeeEntity savedEntity = employeeRepository.save(employee.get());
            EmployeeDto savedEmployeeDto = mapToDtoFromEntityWithSelectedInfo(savedEntity.getEmployeeId(),
                    savedEntity.getFirstName(), savedEntity.getLastName(), savedEntity.getEmail(), savedEntity.getContactNo(),
                    officeMapper.mapTo(savedEntity.getOffice()), savedEntity.getDesignation(), null,
                    savedEntity.getSpecialInfo(), null);
            savedEmployeeDto.setRoles(accountRepository.findRolesByEmployee(savedEntity));
            return savedEmployeeDto;
        }
        else return null;
    }

    @Override
    public void deleteEmployee(Long id) {
        EmployeeEntity toBeDeletedEmployee = employeeRepository.findById(id).get();
        accountRepository.deleteByEmployee(toBeDeletedEmployee);
        employeeRepository.deleteById(id);
    }
    
    private EmployeeDto mapToDtoFromEntityWithSelectedInfo(Long employeeId, String firstName, String lastName,
                                           String email, String contactNo, OfficeDto office,
                                           String designation, String roles, String specialInfo, 
                                           String password) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(employeeId);
        employeeDto.setDesignation(designation);
        employeeDto.setOffice(office);
        employeeDto.setFirstName(firstName);
        employeeDto.setLastName(lastName);
        employeeDto.setContactNo(contactNo);
        employeeDto.setEmail(email);
        employeeDto.setPassword(password);
        employeeDto.setSpecialInfo(specialInfo);
        employeeDto.setRoles(roles);        
        return employeeDto;
    }

    private EmployeeEntity mapToEntityFromDto(EmployeeDto employeeDto) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setEmployeeId(employeeDto.getEmployeeId());
        employeeEntity.setDesignation(employeeEntity.getDesignation());
        employeeEntity.setOffice(officeMapper.mapFrom(employeeDto.getOffice()));
        employeeEntity.setFirstName(employeeDto.getFirstName());
        employeeEntity.setLastName(employeeDto.getLastName());
        employeeEntity.setContactNo(employeeDto.getContactNo());
        employeeEntity.setEmail(employeeEntity.getEmail());
        employeeEntity.setSpecialInfo(employeeDto.getSpecialInfo());
        return employeeEntity;
    }
}
