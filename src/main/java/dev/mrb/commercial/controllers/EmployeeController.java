package dev.mrb.commercial.controllers;

import dev.mrb.commercial.model.dtos.EmployeeDto;
import dev.mrb.commercial.services.EmployeeService;
import lombok.RequiredArgsConstructor;
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
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.FOUND);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<EmployeeDto> addEmployee(@RequestBody EmployeeDto employeeDto) {
        return new ResponseEntity<>(employeeService.addNewEmployee(employeeDto), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id) {
        EmployeeDto foundEmployee = employeeService.getEmployee(id);
        if (foundEmployee!=null) return new ResponseEntity<>(foundEmployee, HttpStatus.FOUND);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/{name}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByName(@PathVariable String name) {
        List<EmployeeDto> foundEmployees = employeeService.getEmployeeByName(name);
        if (!foundEmployees.isEmpty()) return new ResponseEntity<>(foundEmployees, HttpStatus.FOUND);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(path = "/{id}/edit")
    public ResponseEntity<EmployeeDto> editEmployeeDataByHimself(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
        return new ResponseEntity<>(employeeService.updateEmployeeDataByHimself(id, employeeDto), HttpStatus.OK);
    }

    @PutMapping(path = "/{superiorId}/{id}/edit")
    public ResponseEntity<EmployeeDto> editEmployeeDataBySuperior(@PathVariable Long superiorId, @PathVariable Long id,
                                                                  @RequestBody EmployeeDto employeeDto) {
        return new ResponseEntity<>(employeeService.updateEmployeeDataBySuperior(superiorId, id, employeeDto), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }



    // some more
}