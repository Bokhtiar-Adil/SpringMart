package dev.mrb.commercial.model.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDto {

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNo;
    private OfficeDto office;
    private String designation;
    private String roles;
    private String specialInfo = null;

    private String username;
    private String password;

}
