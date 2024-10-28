package dev.mrb.commercial.mappers.impl;

import dev.mrb.commercial.mappers.Mapper;
import dev.mrb.commercial.model.dtos.EmployeeDto;
import dev.mrb.commercial.model.entities.EmployeeEntity;
import dev.mrb.commercial.repositories.OfficeRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmployeeMapperImpl implements Mapper<EmployeeEntity, EmployeeDto> {

    private final OfficeRepository officeRepository;

    @Override
    public EmployeeDto mapTo(EmployeeEntity employeeEntity) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(employeeEntity.getEmployeeId());
        employeeDto.setDesignation(employeeEntity.getDesignation());
        employeeDto.setOfficeId(employeeEntity.getOffice().getOfficeId());
        employeeDto.setOfficeAddress(employeeEntity.getOffice().getAddress());
        employeeDto.setFirstName(employeeEntity.getFirstName());
        employeeDto.setLastName(employeeEntity.getLastName());
        employeeDto.setContactNo(employeeEntity.getContactNo());
        employeeDto.setEmail(employeeEntity.getEmail());
        employeeDto.setUsername(null);
        employeeDto.setSpecialInfo(employeeEntity.getSpecialInfo());
        employeeDto.setRoles(null);

        return employeeDto;
    }

    @Override
    public EmployeeEntity mapFrom(EmployeeDto employeeDto) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setFirstName(employeeDto.getFirstName());
        employeeEntity.setLastName(employeeDto.getLastName());
        employeeEntity.setContactNo(employeeDto.getContactNo());
        employeeEntity.setEmail(employeeEntity.getEmail());
        employeeEntity.setOffice(officeRepository.findById(employeeDto.getOfficeId()).get());
        employeeEntity.setDesignation(employeeEntity.getDesignation());
        employeeEntity.setSpecialInfo(employeeDto.getSpecialInfo());

        return employeeEntity;
    }
}
