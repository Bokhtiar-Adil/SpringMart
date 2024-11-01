package dev.mrb.commercial.controllers;

import dev.mrb.commercial.events.AccountVerificationEvent;
import dev.mrb.commercial.model.dtos.EmployeeDto;
import dev.mrb.commercial.model.entities.AccountEntity;
import dev.mrb.commercial.services.AccountService;
import dev.mrb.commercial.services.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping(path = "/all-employees")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @PostMapping(path = "/add-new-employee-details")
    public ResponseEntity<String> addNewEmployeeDetails(@RequestBody String firstName,
                                                        @RequestBody String lastName,
                                                        @RequestBody String email,
                                                        @RequestBody String contactNo,
                                                        @RequestBody Long officeId,
                                                        @RequestBody String designation,
                                                        @RequestBody String roles,
                                                        @RequestBody String specialInfo) {
        EmployeeDto employeeDto;
        String status;

        employeeDto = new EmployeeDto(
                0L,
                firstName,
                lastName,
                contactNo,
                email,
                officeId,
                null,
                designation,
                roles,
                specialInfo,
                null);
        status = employeeService.addNewEmployee(employeeDto);
        if (!status.equalsIgnoreCase("ok"))
            return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping(path = "/get-employee/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id) {
        EmployeeDto foundEmployee;

        foundEmployee = employeeService.getEmployee(id);
        if (foundEmployee != null) return new ResponseEntity<>(foundEmployee, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/get-employees-by-name/{name}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByName(@PathVariable String name) {
        List<EmployeeDto> foundEmployees;

        foundEmployees = employeeService.getEmployeesByName(name);
        if (!foundEmployees.isEmpty()) return new ResponseEntity<>(foundEmployees, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(path = "/{id}/edit-details")
    public ResponseEntity<String> editEmployeeDataByHimself(@PathVariable Long id,
                                                            @RequestBody String firstName,
                                                            @RequestBody String lastName,
                                                            @RequestBody String email,
                                                            @RequestBody String contactNo) {
        EmployeeDto employeeDto;
        String status;
        
        employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(id);
        employeeDto.setFirstName(firstName);
        employeeDto.setLastName(lastName);
        employeeDto.setEmail(email);
        employeeDto.setContactNo(contactNo);

        status = employeeService.updateEmployeeDataByHimself(employeeDto);
        if (status.equalsIgnoreCase("ok"))
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        else
            return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
    }

    @PutMapping(path = "/edit-employee-as-superior/{superiorId}/{id}")
    public ResponseEntity<String> editEmployeeDataBySuperior(@PathVariable Long superiorId,
                                                             @PathVariable Long id,
                                                             @RequestBody String firstName,
                                                             @RequestBody String lastName,
                                                             @RequestBody String email,
                                                             @RequestBody String contactNo,
                                                             @RequestBody Long officeId,
                                                             @RequestBody String designation,
                                                             @RequestBody String roles,
                                                             @RequestBody String specialInfo
                                                             ) {
        EmployeeDto employeeDto;
        String status;

        if (!employeeService.exists(superiorId))
            return new ResponseEntity<>("Invalid superior id", HttpStatus.BAD_REQUEST);

        employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(id);
        employeeDto.setFirstName(firstName);
        employeeDto.setLastName(lastName);
        employeeDto.setEmail(email);
        employeeDto.setContactNo(contactNo);
        employeeDto.setOfficeId(officeId);
        employeeDto.setDesignation(designation);
        employeeDto.setRoles(roles);
        employeeDto.setSpecialInfo(specialInfo);

        status = employeeService.updateEmployeeDataByHimself(employeeDto);
        if (status.equalsIgnoreCase("ok"))
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        else
            return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(path = "/delete-employee/{superiorId}/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long superiorId, @PathVariable Long id) {
        String status;

        if (!employeeService.exists(superiorId))
            return new ResponseEntity<>("Invalid superior id", HttpStatus.BAD_REQUEST);

        status = employeeService.deleteEmployee(id);
        if (status.equalsIgnoreCase("ok"))
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        else
            return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
    }
}
